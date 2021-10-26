package com.example.sbaby.task

import com.example.sbaby.*

class TaskRepository {

    fun getTaskList(): List<TaskModel> {
        return listOf(
            TaskModel(
                "1", "Wash the dishes", 1343805819061L, "You need to wash all the dishes \n" +
                        "after diner. Use “Vanish” to clean \n" +
                        "them better!", 10, TO_DO
            ),
            TaskModel(
                "2", "Do", 0L, "Do something", 5, TO_DO
            ),
            TaskModel(
                "3", "fff", 0L, "Do something", 10, TO_DO
            ),
            TaskModel(
                "4", "ggg", 14L, "Do something", 17, DONE(123L)
            )
        )
    }

    fun getRewardList() {

    }


    fun getParent(): Parent {
        return Parent("1", "Parent", "", getChildList() , true)
    }

    fun getChild(): Child {
        return Child("1", "Anton", 123, "", 70, 4, getTaskList())
    }

    fun getChildList(): List<Child> {
        return listOf(
            Child("1", "Artem", 123, "", 70, 4, getTaskList()),
            Child("2", "Anton2", 1233, "", 79, 3, getTaskList())
        )
    }
}
