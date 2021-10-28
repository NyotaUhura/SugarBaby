package com.example.sbaby.task

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.*
import com.example.sbaby.databinding.FragmentTaskBinding
import com.example.sbaby.viewholders.task.TaskCardViewHolder
import com.example.sbaby.viewholders.task.taskCardViewHolder

class TaskFragment : MvRxBaseFragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by fragmentViewModel()
    private val binding: FragmentTaskBinding by viewBinding()

    private val buttons: TaskCardViewHolder.buttonsOnclick =
        object : TaskCardViewHolder.buttonsOnclick {
            override fun doneButtonOnclick(id: String) {
                viewModel.changeUndoneTaskStatus(id)
            }

            override fun deleteButtonOnclick(id: String) {
                TODO("Not yet implemented")
            }

            override fun editButtonOnClick(id: String) {
                TODO("Not yet implemented")
            }

            override fun undoneButtonOnclick(id: String) {
                // viewModel.changeDoneTaskStatus(id)
            }
        }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val taskList = state.taskList.invoke()
        taskList?.forEach { taskModel ->
            when (state.user.invoke()) {
                is Parent -> {
                    taskCardViewHolder {
                        id(taskModel.id)
                        task(taskModel)
                        isParent(true)
                        onClickListeners(buttons)
                        when (taskModel.status) {
                            is DONE -> isDone(true)
                            is TO_DO -> isDone(false)
                        }
                    }
                }
                is Child -> {
                    if (taskModel.status is TO_DO) {
                        taskCardViewHolder {
                            id(taskModel.id)
                            task(taskModel)
                            isParent(false)
                            onClickListeners(buttons)
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAsync(TaskState::user, onSuccess = { user ->
            when (user) {
                is Parent -> {
                    buildParentUi(user)
                }
                is Child -> {
                    buildChildUi(user)
                }
            }
        })
    }

    private fun buildParentUi(user: Parent) {
        if (user.isPremium) binding.itemPremium.visibility = View.GONE
        else binding.premiumCard.visibility = View.VISIBLE
        val child = user.childList[user.currChild]
        bindChild(child)

        binding.shareButton.visibility = View.GONE
        binding.addTaskButton.visibility = View.VISIBLE
        binding.doneCheckbox.visibility = View.VISIBLE
        binding.inProgressCheckbox.visibility = View.VISIBLE
        binding.changeButton.setBackgroundResource(R.drawable.ic_change)
        binding.doneCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(
                binding.doneCheckbox.isChecked,
                binding.inProgressCheckbox.isChecked
            )
        }
        binding.inProgressCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(
                binding.doneCheckbox.isChecked,
                binding.inProgressCheckbox.isChecked
            )
        }
    }

    private fun buildChildUi(user: Child) {
        bindChild(user)
        binding.shareButton.visibility = View.VISIBLE
        binding.addTaskButton.visibility = View.GONE
        binding.doneCheckbox.visibility = View.GONE
        binding.inProgressCheckbox.visibility = View.GONE
        binding.changeButton.setBackgroundResource(R.drawable.ic_edit_photo);
    }

    private fun bindChild(child: Child) {
        binding.nameTextView.text = child.name
        binding.moneyTextView.text = child.money.toString()
        binding.levelProcessBar.progress = viewModel.countProcessPercent(child)
        binding.levelTextView.text =
            getString(R.string.helper) + viewModel.countlevel(child) + getString(R.string.level)
    }

}
