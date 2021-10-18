package com.example.sbaby.task

import com.airbnb.mvrx.*
import com.example.sbaby.TaskModel
import com.example.sbaby.UserModel
import kotlin.random.Random


data class TaskState(
    val user: Async<UserModel> = Uninitialized,
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

        val user = taskRepository.getUser()
        val taskList = taskRepository.getTaskList()
        setState {
            copy(user = Success(user), taskList = Success(taskList))
        }
    }

    fun changeUserName() {
        withState { state ->
            val user = state.user.invoke() ?: return@withState
            val newUser = user.copy(name = Random.nextInt().toString())
            setState { copy(user = Success(newUser)) }
        }

    }

    fun removeTask() {
        setState { copy(taskList = Success(listOf())) }
    }

    companion object : MavericksViewModelFactory<TaskViewModel, TaskState> {

        override fun create(viewModelContext: ViewModelContext, state: TaskState): TaskViewModel {
            val rep = TaskRepository()
            return TaskViewModel(state, rep)
        }
    }
}
