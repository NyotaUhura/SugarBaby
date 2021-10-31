package com.example.sbaby

import com.example.sbaby.task.userId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseDataSource(private val fireStore: FirebaseFirestore) {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FAMILIES_COLLECTION = "families"
    }

    private var user: User? = null
    private lateinit var family: Family

    suspend fun getUser(): User? {
        if (user != null) {
            loadUser(userId)
        }
        return user
    }

    fun getGiftList(): List<GiftModel> {
        return listOf(
            GiftModel(
                "1", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "2", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
            GiftModel(
                "3", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "4", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
            GiftModel(
                "5", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "6", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
            GiftModel(
                "7", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "8", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
            GiftModel(
                "9", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "10", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
            GiftModel(
                "11", "Watch a film", "Free film", 20, 1, true
            ),
            GiftModel(
                "12", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false
            ),
        )
    }

    suspend fun getFamily(id: String): Family {
        if (!this::family.isInitialized || family.id != id) {
            loadFamily(id)
        }
        return family
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
                is ChildFirebaseModel -> firebaseUserRes.data.toChildModel()
                is ParentFirebaseModel -> {
                    val children = mutableListOf<Child>()
                    firebaseUserRes.data.childList.forEach { ref ->
                        val childRes = loadUserByRef(ref)
                        if (childRes is Result.Success) {
                            if (childRes.data is ChildFirebaseModel) {
                                children.add(childRes.data.toChildModel())
                            }
                        }
                    }
                    firebaseUserRes.data.toParentModel(children)
                }
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
            fireStore.collection(USERS_COLLECTION).get()
                .addOnSuccessListener { snapShot ->
                    snapShot.documents.forEach { doc ->
                        if (doc.id == userId) {
                            val isParent = doc.getBoolean("isParent") ?: false
                            val user = if (isParent) {
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
                    con.resume(Result.Error(NullPointerException()))
                }
        }
}
