package com.example.sbaby.task

import android.util.Log
import com.airbnb.mvrx.*
import com.example.sbaby.*
import com.example.sbaby.auth.FirebaseAuthManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

data class TaskState(
    val user: Async<User> = Uninitialized,
    val selectedChild: Async<Child> = Uninitialized,
    val family: Async<Family> = Uninitialized,
    val taskList: Async<List<TaskModel>> = Uninitialized
) : MavericksState

class TaskViewModel(
    initialState: TaskState,
    private val firebaseDataSource: FirebaseDataSource,
    private val authManager: FirebaseAuthManager
) : MavericksViewModel<TaskState>(initialState) {

    init {
        loadData()
    }

    fun addTask(task: TaskModel) {
        withState { state: TaskState ->
            val user = state.user.invoke() ?: return@withState
            Log.e("user", user.toString())
            when (user) {
                is Parent -> {
                    val child = user.childList[0]
                    Log.e("child", child.toString())
                    val taskList = child.taskList
                    val newTaskList = taskList as MutableList<TaskModel>
                    newTaskList.add(task)
                    val newUser = child.copy(
                        taskList = newTaskList as List<TaskModel>,
                    )
                    Log.e("newUser", newUser.toString())
                    updateChildInParent(newUser)
                }
            }
        }
    }

    fun changeUndoneTaskStatus(id: String) {
        withState { state: TaskState ->
            val taskList = state.taskList.invoke() ?: return@withState
            val user = state.user.invoke() ?: return@withState
            val newTask = taskList.first { it.id == id }.copy(
                status = DONE(Date().time)
            )
            when (user) {
                is Child -> {
                    val newTaskList = taskList.map {
                        if (it.id == id) {
                            newTask
                        } else {
                            it
                        }
                    }
                    val newUser = user.copy(
                        taskList = newTaskList,
                        money = user.money + newTask.profit,
                        level = ((user.process + newTask.profit) / 1000) + 1,
                        process = user.process + newTask.profit
                    )
                    updateUser(newUser)
                }
            }
        }
    }

    fun changeDoneTaskStatus(id: String) {
        withState { state: TaskState ->
            val user = state.user.invoke() ?: return@withState
            when (user) {
                is Parent -> {
                    val newChildList = user.childList.map {
                        if (it.id == id) {
                            val taskList = state.taskList.invoke() ?: return@withState
                            val newTask = taskList.first { it.id == id }.copy(
                                status = TO_DO
                            )
                            if (it.id == id) {
                                newTask
                            } else {
                                it
                            }
                            it.money -= newTask.profit
                            it.process -= newTask.profit
                            it.level = it.process / 1000 + 1

                        } else {
                            it
                        }
                    }
                    val newUser = user.copy(
                        childList = newChildList as List<Child>
                    )
                    updateUser(newUser)
                }
            }
        }
    }

    fun deleteTask(id: String) {
        withState { state: TaskState ->
            val user = state.user.invoke() ?: return@withState
            when (user) {
                is Parent -> {
                    val newChildList = user.childList.map {
                        if (it.id == id) {
                            val taskList = state.taskList.invoke() ?: return@withState
                            if (it.id == id) {
                            } else {
                                it
                            }
                        } else {
                            it
                        }
                    }
                    val newUser = user.copy(
                        childList = newChildList as List<Child>
                    )
                    updateUser(newUser)
                }
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            val res = firebaseDataSource.saveUser(user)
            if (res) {
                val taskList = when (user) {
                    is Child -> {
                        user.taskList
                    }
                    // TODO: 09.11.2021 Rebase logic
                    is Parent -> user.childList[0].taskList
                    else -> throw IllegalAccessError()
                }
                setState { copy(user = Success(user), taskList = Success(taskList)) }
            }
        }
    }

    private fun updateChildInParent(user: User) {
        viewModelScope.launch {
            val res = firebaseDataSource.saveUser(user)
            if (res) {
                val taskList = when (user) {
                    is Child -> {
                        user.taskList
                    }
                    else -> throw IllegalAccessError()
                }
                setState { copy(taskList = Success(taskList)) }
            }
        }
    }

    fun countProcessPercent(user: Child): Int {
        return ((user.process % 1000) / 10)
    }

    fun filterGifts(isDone: Boolean, isProgress: Boolean) {
        withState { state ->
            val user = state.selectedChild.invoke() ?: return@withState
            Log.e("qqqqqqqqqqq", user.toString())
            val taskList = when (user) {
                is Child -> user.taskList
                else -> throw NullPointerException()
            }
            val newList = taskList.filter { task ->
                if (isDone || isProgress) {
                    task.status is DONE == isDone || task.status is TO_DO == isProgress
                } else {
                    false
                }
            }
            setState {
                copy(taskList = Success(newList))
            }
            viewModelScope.launch {
                val newUser = user.copy(taskList = newList)
                val res = firebaseDataSource.saveUser(user)
                if (res) setState { copy(user = Success(newUser)) }
            }

        }
    }

    fun changeName(newName: String) {
        withState { state ->
            val userChild = state.user.invoke() ?: return@withState
            when (userChild) {
                is Child -> {
                    val newUser = userChild.copy(name = newName)
                    updateUser(newUser)
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

    private fun loadData() {
        viewModelScope.launch {
            setState {
                copy(user = Loading(), taskList = Loading(), family = Loading())
            }
            val user = firebaseDataSource.getUser()
            if (user != null) {
                val family = firebaseDataSource.getFamily(user.familyId)
                setState {
                    copy(user = Success(user), family = Success(family))
                }
                if (user is Child) {
                    setState { copy(taskList = Success(user.taskList)) }
                } else {
                    withState { state ->
                        var selectedChild = state.selectedChild.invoke()
                        if (selectedChild == null) {
                            if ((user as Parent).childList.isNotEmpty()) {
                                setState { copy(selectedChild = Success(user.childList[0])) }
                            }
                        }
                        selectedChild = state.selectedChild.invoke() ?: return@withState
                        setState { copy(taskList = Success(selectedChild.taskList)) }
                    }
                }
            } else {
                setState { copy(user = Fail(NullPointerException())) }
            }
        }
    }

    companion object : MavericksViewModelFactory<TaskViewModel, TaskState> {

        override fun create(viewModelContext: ViewModelContext, state: TaskState): TaskViewModel {
            val rep: FirebaseDataSource by viewModelContext.activity.inject<FirebaseDataSource>()
            val authManager: FirebaseAuthManager by viewModelContext.activity.inject()
            return TaskViewModel(state, rep, authManager)
        }
    }
}

