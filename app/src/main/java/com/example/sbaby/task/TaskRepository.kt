package com.example.sbaby.task

import com.example.sbaby.*

class TaskRepository {

    fun getTaskList(): List<TaskModel> {
        return listOf(
            TaskModel(
                "1", "Wash", 12L, "Do something", 12, TO_DO
            ),
            TaskModel(
                "2", "Do", 12L, "Do something", 5, DONE(123L)
            )
        )
    }

    fun getRewardList() {

    }


    fun getUser(): UserModel {
        return UserModel("124", "Artem")
    }
}