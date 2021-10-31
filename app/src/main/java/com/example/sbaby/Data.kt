package com.example.sbaby

import com.google.firebase.firestore.DocumentReference

data class Family(
    val id: String = "",
    val surname: String = "",
    val isPremium: Boolean = false
)

sealed class User(
    open val id: String,
    open val familyId: String,
    open val photo: String
)

sealed class UserFirebase

data class Parent(
    override val id: String,
    override val familyId: String,
    val name: String,
    override val photo: String,
    var childList: List<Child>,
) : User(id, familyId, photo)

data class ParentFirebaseModel(
    val id: String,
    val name: String,
    val familyId: String,
    val photo: String,
    var childList: List<DocumentReference>
) : UserFirebase() {
    fun toParentModel(children: List<Child>): Parent {
        return Parent(
            id = id,
            name = name,
            photo = photo,
            childList = children,
            familyId = familyId
        )
    }
}

data class ChildFirebaseModel(
    val id: String = "",
    val familyId: String = "",
    val name: String = "",
    var money: Int = 0,
    val photo: String = "",
    val process: Int = 0,
    val level: Int = 0,
    val taskList: List<TaskFirebaseModel> = listOf()
) : UserFirebase() {
    fun toChildModel(): Child {
        return Child(
            id = id,
            familyId = familyId,
            name = name,
            money = money,
            photo = photo,
            process = process,
            level = level,
            taskList = taskList.map { it.toTaskModel() }
        )
    }
}

data class Child(
    override val id: String,
    override val familyId: String,
    val name: String,
    var money: Int,
    override val photo: String,
    val process: Int,
    val level: Int,
    val taskList: List<TaskModel>
) : User(id, familyId, photo)

data class GiftModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val availableCount: Int,
    val isAgree: Boolean,
)

data class TaskModel(
    val id: String,
    val title: String,
    val deadline: Long,
    val description: String,
    val profit: Int,
    var status: Status
)

data class TaskFirebaseModel(
    val id: String = "",
    val title: String = "",
    val deadline: Long = 0L,
    val description: String = "",
    val profit: Int = 0,
    var status: String = ""
) {
    fun toTaskModel(): TaskModel {
        val statusString = status.substring(0, status.indexOf('='))
        val sealedStatus = if (statusString == "DONE") {
            DONE(status.substring(status.indexOf('=') + 1).toLong())
        } else {
            TO_DO
        }
        return TaskModel(
            id = id,
            title = title,
            deadline = deadline,
            description = description,
            profit = profit,
            status = sealedStatus
        )
    }
}


sealed class Status

object TO_DO : Status()

class DONE(val doneTime: Long) : Status()

sealed class Result<out TData : Any> {
    data class Success<out TData : Any>(val data: TData) : Result<TData>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
