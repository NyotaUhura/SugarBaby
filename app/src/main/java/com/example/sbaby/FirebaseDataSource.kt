package com.example.sbaby

import android.util.Log
import com.example.sbaby.auth.FirebaseAuthManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

//id for current user coz dont have authentication
//val userId = "jvJztwD5bN7K5Xbmq9I6"

class FirebaseDataSource(private val fireStore: FirebaseFirestore, private val authManager: FirebaseAuthManager) {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FAMILIES_COLLECTION = "families"
        private const val GIFTS_COLLECTION = "gifts"
    }

    private var user: User? = null
    private lateinit var family: Family
    private var gifts: MutableList<GiftModel> = mutableListOf()

    suspend fun getUser(): User? {
        if (user == null) {
            val userId = authManager.getUserID() ?: return null
            loadUser(userId)
        }
        return user
    }

    suspend fun getGiftList(): List<GiftModel> {
        if (gifts.isEmpty()) loadGiftList()
        return gifts.toList()
    }

    suspend fun getFamily(id: String): Family {
        if (!this::family.isInitialized || family.id != id) {
            loadFamily(id)
        }
        return family
    }

    suspend fun saveMockUser(userId: String, isParent: Boolean, familyId: String?): Boolean {
        val data = when (isParent) {
            true -> {
                val user = ParentFirebaseModel()
                var familyIdSafe = familyId
                if (familyIdSafe == null) {
                    val id = UUID.randomUUID()
                    val familyRes = createFamily(id.toString())
                    when (familyRes) {
                        is Result.Success -> {
                            familyIdSafe = id.toString()
                        }
                        is Result.Error -> {
                            return false
                        }
                    }
                }
                mapOf(
                    "name" to user.name,
                    "photo" to user.photo,
                    "familyId" to familyIdSafe!!
                )
            }
            false -> {
                val user = ChildFirebaseModel()
                mapOf(
                    "level" to user.level,
                    "gifts" to user.gifts,
                    "photo" to user.photo,
                    "money" to user.money,
                    "process" to user.process,
                    "familyId" to user.familyId
                )
            }
        }
        val res = createUser(userId, data)
        return if (res is Result.Success) res.data
        else false
    }

    suspend fun saveUser(user: User): Boolean {
        val data = when (user) {
            is Child -> {
                mapOf(
                    "level" to user.level,
                    "gifts" to user.gifts,
                    "photo" to user.photo,
                    "money" to user.money,
                    "process" to user.process,
                    "taskList" to user.taskList.map { it.mapToFirebaseModel() }
                )
            }
            is Parent -> {
                mapOf(
                    "photo" to user.photo
                )
            }
            else -> {
                throw IllegalAccessException()
            }
        }
        val res = saveTaskToDB(user.id, data)
        return if (res is Result.Success) res.data
        else false
    }

    suspend fun updateGift(gift: GiftModel): List<GiftModel> {
        val oldGift = gifts.firstOrNull { it.id == gift.id }
        if (oldGift != null) {
            val newList = gifts.map {
                if (it.id == gift.id) gift
                else it
            }
            gifts = newList.toMutableList()
        } else {
            gifts.add(gift)
        }
        addGiftDoc(gift)
        return gifts.toList()
    }

    private suspend fun createUser(id: String, data: Map<String, Any>) =
        suspendCancellableCoroutine<Result<Boolean>> { con ->
            fireStore.collection(USERS_COLLECTION).document(id).set(data)
                .addOnSuccessListener { con.resume(Result.Success(true)) }
                .addOnFailureListener { con.resume(Result.Error(it)) }
        }

    suspend fun createFamily(id: String) = suspendCancellableCoroutine<Result<Boolean>> { con ->
        val mockData = mapOf<String, Any>(
            "isPremium" to false,
            "childs" to listOf<DocumentReference>(),
            "surname" to "undefined"
        )
        fireStore.collection(FAMILIES_COLLECTION).document(id).set(mockData)
            .addOnSuccessListener {
                con.resume(Result.Success(true))
            }
            .addOnFailureListener {
                con.resume(Result.Error(it))
            }
    }

    private suspend fun addGiftDoc(gift: GiftModel) =
        suspendCancellableCoroutine<Result<Boolean>> { con ->
            fireStore.collection(GIFTS_COLLECTION).document(gift.id).set(gift)
                .addOnSuccessListener { con.resume(Result.Success(true)) }
                .addOnFailureListener { con.resume(Result.Error(it)) }
        }

    private suspend fun saveTaskToDB(id: String, data: Map<String, Any>) =
        suspendCancellableCoroutine<Result<Boolean>> { con ->
            fireStore.collection(USERS_COLLECTION).document(id).update(data)
                .addOnSuccessListener { con.resume(Result.Success(true)) }
                .addOnFailureListener { con.resume(Result.Error(it)) }

        }

    private suspend fun loadGiftList() {
        if (user == null) return
        val ids = mutableListOf<String>()
        when (user) {
            is Child -> ids.addAll((user as Child).gifts)
            is Parent -> (user as Parent).childList.forEach { child ->
                ids.addAll(child.gifts)
            }
        }
        val giftsRes = loadGiftResult(ids)
        if (giftsRes is Result.Success) {
            gifts = giftsRes.data.toMutableList()
        }
    }

    private suspend fun loadGiftResult(ids: List<String>) =
        suspendCancellableCoroutine<Result<List<GiftModel>>> { con ->
            fireStore.collection(GIFTS_COLLECTION).get()
                .addOnSuccessListener { snapShot ->
                    val gifts = mutableListOf<GiftModel>()
                    snapShot.documents.forEach { doc ->
                        if (ids.contains(doc.id)) {
                            val gift = doc.toObject(GiftModel::class.java)
                            if (gift != null) gifts.add(gift)
                        }
                    }
                    con.resume(Result.Success(gifts))
                }
                .addOnFailureListener {
                    con.resume(Result.Error(it))
                }
        }

    private suspend fun loadFamily(id: String) {
        val familyRes = loadFamilyResult(id)
        if (familyRes is Result.Success) {
            family = familyRes.data
        }
    }

    private suspend fun loadFamilyResult(id: String) =
        suspendCancellableCoroutine<Result<Family>> { con ->
            fireStore.collection(FAMILIES_COLLECTION).get()
                .addOnSuccessListener { snapShot ->
                    snapShot.documents.forEach { doc ->
                        if (doc.id == id) {
                            val family = doc.toObject(Family::class.java)?.copy(id = doc.id)
                            if (family != null) {
                                con.resume(Result.Success(family))
                            } else {
                                con.resume(Result.Error(NullPointerException()))
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    con.resume(Result.Error(it))
                }
        }

    private suspend fun loadUser(userId: String) {
        val firebaseUserRes = loadUserDoc(userId)
        if (firebaseUserRes is Result.Success) {
            val userModel = when (firebaseUserRes.data) {
                is ChildFirebaseModel -> firebaseUserRes.data.mapToChildModel()
                is ParentFirebaseModel -> {
                    val children = mutableListOf<Child>()
                    firebaseUserRes.data.childList.forEach { ref ->
                        val childRes = loadUserByRef(ref)
                        if (childRes is Result.Success) {
                            if (childRes.data is ChildFirebaseModel) {
                                children.add(childRes.data.mapToChildModel())
                            }
                        }
                    }
                    firebaseUserRes.data.toParentModel(children)
                }
                else -> throw IllegalAccessException()
            }
            user = userModel
        }
    }

    private suspend fun loadUserByRef(ref: DocumentReference) =
        suspendCancellableCoroutine<Result<UserFirebase>> { con ->
            ref.get()
                .addOnSuccessListener { snapShot ->
                    val isParent = snapShot.getBoolean("isParent") ?: false
                    val user = if (isParent) {
                        snapShot.toObject(ParentFirebaseModel::class.java)
                    } else {
                        snapShot.toObject(ChildFirebaseModel::class.java)
                    }
                    if (user != null) con.resume(Result.Success(user))
                    else con.resume(Result.Error(NullPointerException()))
                }
                .addOnFailureListener {
                    con.resume(Result.Error(it))
                }
        }

    private suspend fun loadUserDoc(userId: String) =
        suspendCancellableCoroutine<Result<UserFirebase>> { con ->
            fireStore.collection(USERS_COLLECTION).document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc.data != null) {
                        val isParent = doc.getBoolean("isParent") ?: false
                        val user = if (!isParent) {
                            doc.toObject(ChildFirebaseModel::class.java)?.copy(id = doc.id)
                        } else {
                            doc.toObject(ParentFirebaseModel::class.java)?.copy(id = doc.id)
                        }
                        if (user != null) {
                            con.resume(Result.Success(user))
                        } else {
                            con.resume(Result.Error(NullPointerException()))
                        }
                    } else {
                        //saveUserMock()
                    }
                }
                .addOnFailureListener {
                    Log.d("FailAuth", it.message.toString())
                    con.resume(Result.Error(it))
                }
            /*fireStore.collection(USERS_COLLECTION).get()
                .addOnSuccessListener { snapShot ->
                    snapShot.documents.forEach { doc ->
                        if (doc.id == userId) {
                            val isParent = doc.getBoolean("isParent") ?: false
                            val user = if (!isParent) {
                                doc.toObject(ChildFirebaseModel::class.java)?.copy(id = doc.id)
                            } else {
                                doc.toObject(ParentFirebaseModel::class.java)?.copy(id = doc.id)
                            }
                            if (user != null) {
                                con.resume(Result.Success(user))
                            } else {
                                con.resume(Result.Error(NullPointerException()))
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("FailAuth", it.message.toString())
                    con.resume(Result.Error(it))
                }*/
        }
}
