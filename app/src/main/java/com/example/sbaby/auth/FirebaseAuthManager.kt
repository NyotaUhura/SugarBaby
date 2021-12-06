package com.example.sbaby.auth

import android.util.Log
import com.example.sbaby.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class FirebaseAuthManager(private val auth: FirebaseAuth) {
    private val _firebaseUserId = MutableStateFlow(auth.currentUser?.uid)
    val firebaseUserId: StateFlow<String?> = _firebaseUserId.asStateFlow()

    fun getUserID() = firebaseUserId.value

    fun isLoginIn() = firebaseUserId.value == null

    fun logOut() {
        auth.signOut()
        updateUser()
    }

    suspend fun singUp(email: String, password: String) = suspendCancellableCoroutine<Result<String>> { con ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) con.resume(Result.Success(id))
                    else con.resume(Result.Error(NullPointerException()))
                    updateUser()
                } else {
                    val error = task.exception ?: java.lang.NullPointerException()
                    con.resume(Result.Error(error))
                }
            }
    }

    suspend fun loginIn(email: String, password: String) = suspendCancellableCoroutine<Result<String>> { con ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) con.resume(Result.Success(id))
                    else con.resume(Result.Error(NullPointerException()))
                    updateUser()
                } else {
                    Log.d("FailAuth", "${task.exception?.localizedMessage}")
                    con.resume(Result.Error(task.exception!!))
                }
            }
    }

    private fun updateUser() {
        _firebaseUserId.value = auth.currentUser?.uid
    }
}