package com.example.sbaby.auth

import android.util.Log
import com.example.sbaby.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class FirebaseAuthManager(private val auth: FirebaseAuth) {

    fun getUserID(): String? {
        return auth.currentUser?.uid
    }

    fun isLoginIn() = getUserID() != null

    suspend fun singUp(email: String, password: String) = suspendCancellableCoroutine<Result<String>> { con ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) con.resume(Result.Success(id))
                    else con.resume(Result.Error(NullPointerException()))
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
                } else {
                    Log.d("FailAuth", "${task.exception?.localizedMessage}")
                    con.resume(Result.Error(task.exception!!))
                }
            }
    }

}