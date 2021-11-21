package com.example.sbaby.di

import com.example.sbaby.FirebaseDataSource
import com.example.sbaby.auth.FirebaseAuthManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

val module = module {
    val db = Firebase.firestore
    single { FirebaseDataSource(db, get()) }
    single { Firebase.auth }
    single { FirebaseAuthManager(get()) }
}
