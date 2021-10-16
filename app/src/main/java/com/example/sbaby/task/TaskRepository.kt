package com.example.sbaby.task

import com.example.sbaby.Status
import com.example.sbaby.TaskModel
import com.example.sbaby.UserModel

class TaskRepository {

    fun getTaskList(): List<TaskModel> {
        return listOf(
            TaskModel(
                "1", "Wash", 12L, "Do something", 12, Status.IN_PROGRESS
            ),
            TaskModel(
                "2", "Do", 12L, "Do something", 5, Status.DONE
            )
        )
    }

    fun getUser(): UserModel {
        return UserModel("124", "Artem")
    }
}