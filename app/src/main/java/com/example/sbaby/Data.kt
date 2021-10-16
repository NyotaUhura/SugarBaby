package com.example.sbaby

data class UserModel(
    val id: String,
    val name: String
)

data class TaskModel(
    val id: String,
    val title: String,
    val deadline: Long,
    val description: String,
    val profit: Int,
    val status: Status
)

enum class Status {
    DONE, TO_DO, IN_PROGRESS
}
