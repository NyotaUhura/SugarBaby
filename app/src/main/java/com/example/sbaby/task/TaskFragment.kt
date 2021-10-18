package com.example.sbaby.task

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentTaskBinding
import com.example.sbaby.simpleController
import com.example.sbaby.viewholders.task.TaskCardViewHolder
import com.example.sbaby.viewholders.task.TaskCardViewHolder_
import com.example.sbaby.viewholders.task.taskCardViewHolder
import com.example.sbaby.viewholders.task.userNameViewHolder

class TaskFragment : MvRxBaseFragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by fragmentViewModel()
    private val binding: FragmentTaskBinding by viewBinding()

    override fun epoxyController() = simpleController(viewModel) { state ->
        val taskList = state.taskList.invoke()
        taskList?.forEach { taskModel ->
            taskCardViewHolder {
                id(taskModel.id)
                task(taskModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameButton.setOnClickListener {
            viewModel.changeUserName()
        }

        viewModel.onAsync(TaskState::user, onSuccess = { user ->
            binding.userNameTextView.text = user.name
        })
    }
}
