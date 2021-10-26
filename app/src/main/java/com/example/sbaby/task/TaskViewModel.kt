package com.example.sbaby.task

import com.airbnb.mvrx.*
import com.example.sbaby.*
import java.util.*
import kotlin.random.Random


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

    fun changeTaskStatus(id: String){
        withState { state: TaskState ->
            val taskList = state.taskList.invoke()?: return@withState
            val childUser = state.child.invoke()?: return@withState
            var money = childUser.money
            taskList.forEach {
                if(it.id.equals(id)){
                    when(it.status){
                        is TO_DO -> {
                          //  val newTask = it.copy(status = DONE(Date().time))
                            val newUser = childUser.copy(money = money + it.profit)
                            setState { copy(user = Success(newUser)) }
                        }
                        is DONE -> {
                            it.status = TO_DO
                            childUser.money-= it.profit
                        }
                    }
                    return@forEach
                }
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

    companion object : MavericksViewModelFactory<TaskViewModel, TaskState> {

        override fun create(viewModelContext: ViewModelContext, state: TaskState): TaskViewModel {
            val rep = TaskRepository()
            return TaskViewModel(state, rep)
        }
    }
}