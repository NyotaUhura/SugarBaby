package com.example.sbaby.task

import android.util.Log
import com.airbnb.mvrx.*
import com.example.sbaby.*
import java.util.*


data class TaskState(
    val user: Async<User> = Uninitialized,
    val parent: Async<Parent> = Uninitialized,
    val child: Async<Child> = Uninitialized,
    val taskList: Async<List<TaskModel>> = Uninitialized
) : MavericksState

class TaskViewModel(
    initialState: TaskState,
    private val taskRepository: TaskRepository,
) : MavericksViewModel<TaskState>(initialState) {

    init {
        setState {
            copy(user = Loading(), taskList = Loading())
        }
        val parent = taskRepository.getParent()
        val child = taskRepository.getChild()
        val taskList = taskRepository.getTaskList()
        setState {
            copy(user = Success(child), taskList = Success(taskList))
        }
    }

    fun changeUndoneTaskStatus(id: String) {
        withState { state: TaskState ->
            val taskList = state.taskList.invoke() ?: return@withState
            val childUser = state.user.invoke() ?: return@withState

            when (childUser) {
                is Child -> {
                    val newTaskList = mutableListOf<TaskModel>()
                    taskList.forEach {
                        if (it.id.equals(id)) {
                            val newUser = childUser.copy(
                                money = childUser.money + it.profit,
                                process = childUser.process + it.profit
                            )
                            newTaskList.add(it.copy(status = DONE(Date().time)))
                            setState { copy(user = Success(newUser)) }
                        } else {
                            newTaskList.add(it)
                        }
                    }
                    setState { copy(taskList = Success(newTaskList)) }
                }
            }
        }
    }

    fun countlevel(user: Child): String {
        return (user.process / 1000 + 1).toString()
    }

    fun countProcessPercent(user: Child): Int {
        return ((user.process % 1000) / 10)
    }

    fun filterGifts(isDone: Boolean, isProgress: Boolean) {
        val taskList = taskRepository.getTaskList()
        val newList = taskList.filter { task ->
            if (isDone || isProgress) {
                task.status is DONE == isDone || task.status is TO_DO == isProgress
            } else {
                false
            }
        }
        Log.d("R", newList.count().toString())
        setState {
            copy(taskList = Success(newList))
        }

    }

    fun changeName(newName: String) {
        withState { state ->
            val userChild = state.user.invoke() ?: return@withState
            when (userChild) {
                is Child -> {
                    val newUser = userChild.copy(name = newName)
                    setState { copy(user = Success(newUser)) }
                }
            }
        }
    }

    fun changePhoto(newPhoto: String) {
        withState { state ->
            val user = state.user.invoke() ?: return@withState
            when (user) {
                is Child -> {
                    val newUser = user.copy(photo = newPhoto)
                    setState { copy(user = Success(newUser)) }
                }
                is Parent -> {
                    val newUser = user.copy(photo = newPhoto)
                    setState { copy(user = Success(newUser)) }
                }
            }
        }
    }

    /*
        fun changeDoneTaskStatus(id: String) {
            withState { state: TaskState ->
                val parentUser = state.user.invoke() ?: return@withState
                when (parentUser) {
                    is Parent -> {
                        val child = parentUser.childList[parentUser.currChild]
                        val taskList = child.taskList
                        val newTaskList = mutableListOf<TaskModel>()
                        lateinit var newChild: Child
                        taskList.forEach {
                            if(it.id.equals(id)) {
                                newTaskList.add(it.copy(status = TO_DO))
                                if(child.money < 0) newChild = child.copy(money = 0)
                                else newChild = child.copy(money = child.money - it.profit)
                            }
                            else{
                                newTaskList.add(it)
                            }
                        }
                        val newNewChild = newChild.copy(
                            taskList = newTaskList
                        )
                        val newChildList = mutableListOf<Child>()
                        parentUser.childList.forEach {
                            var i = 0
                            if(i == 0) {
                                newChildList.add(newNewChild)
                                i++
                            }
                            else
                                newChildList.add(it)
                        }
                        val newParent = parentUser.copy(childList = newChildList)
                        setState{copy(parent = Success(newParent))}
                    }
                }
            }
        }
     */
    companion object : MavericksViewModelFactory<TaskViewModel, TaskState> {

        override fun create(viewModelContext: ViewModelContext, state: TaskState): TaskViewModel {
            val rep = TaskRepository()
            return TaskViewModel(state, rep)
        }
    }
}
/*
fun changeUserName() {
    withState { state ->
        val user = state.user.invoke() ?: return@withState
        val newUser = user.copy(name = Random.nextInt().toString())
        setState { copy(user = Success(newUser)) }
    }
}
 */

