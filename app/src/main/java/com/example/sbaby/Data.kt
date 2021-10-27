package com.example.sbaby

import android.graphics.drawable.Drawable
import java.util.*

sealed class User()

data class Parent(
    val id: String,
    val name: String,
    val photo: String,
    var childList: MutableList<Child>,
    val isPremium: Boolean,
    val currChild: Int = 0
): User()

data class Child(
    val id: String,
    val name: String,
    var money: Int,
    val photo: String,
    val process: Int,
    val level: Int,
    val taskList: List<TaskModel>
): User()

data class TaskModel(
    val id: String,
    val title: String,
    val deadline: Long,
    val description: String,
    val profit: Int,
    var status: Status

)
sealed class Status

object TO_DO: Status()

class DONE(val doneTime: Long): Status()
