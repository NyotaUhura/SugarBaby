package com.example.sbaby.di

import com.example.sbaby.FirebaseDataSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val module = module {
    val db = Firebase.firestore
    single { FirebaseDataSource(db) }
}
