package com.example.sbaby


// TODO: Add references to children
data class UserModel(
    val id: String,
    val name: String
)

data class GiftModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val availableCount: Int,
    val IsAgree: Boolean,
)

data class TaskModel(
    val id: String,
    val title: String,
    val deadline: Long,
    val description: String,
    val profit: Int,
    val status: Status
)
sealed class Status

object TO_DO: Status()

class DONE(val doneTime: Long): Status()

