package com.example.sbaby.task

import com.airbnb.mvrx.*
import com.example.sbaby.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

val familyId = "H0l8gT7eovB0GtYlInVC"
val userId = "ZOnCcabgU3PlVTRcWxrM"
val isParent = false

data class TaskState(
    val user: Async<User> = Uninitialized,
    val selectedChild: Async<Child> = Uninitialized,
    val family: Async<Family> = Uninitialized,
    val taskList: Async<List<TaskModel>> = Uninitialized
) : MavericksState

class TaskViewModel(
    initialState: TaskState,
    private val taskRepository: FirebaseDataSource,
) : MavericksViewModel<TaskState>(initialState) {

    init {
        loadData()
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
        withState { state ->
            val user = state.user.invoke()

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

    private fun loadData() {
        viewModelScope.launch {
            setState {
                copy(user = Loading(), taskList = Loading(), family = Loading())
            }
            val user = taskRepository.getUser()
            val family = taskRepository.getFamily(user.familyId)
            setState {
                copy(user = Success(user), family = Success(family))
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
            val rep: FirebaseDataSource by viewModelContext.activity.inject<FirebaseDataSource>()
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

