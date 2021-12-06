package com.example.sbaby

import com.google.firebase.firestore.DocumentReference

private const val defaultPhoto =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Orange_question_mark.svg/1200px-Orange_question_mark.svg.png"

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
) : User(id, familyId, photo) {
    fun mapToFirebaseModel(firebase: FirebaseDataSource): ParentFirebaseModel {
        return ParentFirebaseModel(
            id = id,
            name = name,
            familyId = familyId,
            photo = photo,
            childList = firebase.getRefChild(childList)
        )
    }
}

data class ParentFirebaseModel(
    val id: String = "",
    val name: String = "No name",
    val familyId: String = "",
    val photo: String = defaultPhoto,
    var childList: List<DocumentReference> = listOf()
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
    val name: String = "No name",
    var money: Int = 0,
    val photo: String = defaultPhoto,
    val process: Int = 0,
    val level: Int = 0,
    val taskList: List<TaskFirebaseModel> = listOf(),
    val gifts: List<String> = listOf()
) : UserFirebase() {
    fun mapToChildModel(): Child {
        return Child(
            id = id,
            familyId = familyId,
            name = name,
            money = money,
            photo = photo,
            process = process,
            level = level,
            taskList = taskList.map { it.mapToTaskModel() },
            gifts = gifts
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
    val taskList: List<TaskModel>,
    val gifts: List<String>
) : User(id, familyId, photo)

data class GiftModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Int = 0,
    val availableCount: Int = 0,
    val agree: Boolean = false,
)

data class TaskModel(
    val id: String,
    val title: String,
    val deadline: Long,
    val description: String,
    val profit: Int,
    var status: Status
) {
    fun mapToFirebaseModel(): TaskFirebaseModel {
        return TaskFirebaseModel(
            id = id,
            title = title,
            deadline = deadline,
            description = description,
            profit = profit,
            status = when (status) {
                is DONE -> "DONE=${(status as DONE).doneTime}"
                is TO_DO -> "TO_DO"
                else -> throw IllegalAccessException()
            }
        )
    }
}

data class TaskFirebaseModel(
    val id: String = "",
    val title: String = "",
    val deadline: Long = 0L,
    val description: String = "",
    val profit: Int = 0,
    var status: String = ""
) {
    fun mapToTaskModel(): TaskModel {
        val statusString = if (status.contains('=')) {
            status.substring(0, status.indexOf('='))
        } else {
            status
        }
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
