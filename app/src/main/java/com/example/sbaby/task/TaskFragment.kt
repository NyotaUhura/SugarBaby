package com.example.sbaby.task

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.sbaby.*
import com.example.sbaby.databinding.FragmentTaskBinding
import com.example.sbaby.viewholders.task.TaskCardViewHolder
import com.example.sbaby.viewholders.task.taskCardViewHolder
import java.util.*

class TaskFragment : MvRxBaseFragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by fragmentViewModel()
    private val binding: FragmentTaskBinding by viewBinding()

    private val buttons: TaskCardViewHolder.buttonsOnclick = object :TaskCardViewHolder.buttonsOnclick{
        override fun doneButtonOnclick(id: String) {
           viewModel.changeTaskStatus(id)
        }
        override fun undoneButtonOnclick(id: String) {

        }
    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val taskList = state.taskList.invoke()
        taskList?.forEach { taskModel ->
            taskCardViewHolder {
                id(taskModel.id)
                task(taskModel)
                when(state.user.invoke()) {
                    is Parent -> {
                        isParent(true)
                    }
                    is Child -> {
                        isParent(false)
                    }
                }
                onClickListeners(buttons)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAsync(TaskState::user, onSuccess = { user ->
            when (user) {
                is Parent -> {
                    if(user.isPremium) binding.itemPremium.visibility = View.GONE
                    else binding.premiumCard.visibility = View.VISIBLE
                    val child = user.childList[0]
                    binding.nameTextView.text = child.name
                    binding.moneyTextView.text = child.money.toString()
                    binding.levelProcessBar.progress = child.process
                    binding.levelTextView.text = "Helper ${child.level} level"
                    buildParentUi()
                }
                is Child -> {
                    binding.nameTextView.text = user.name
                    binding.moneyTextView.text = user.money.toString()
                    binding.levelProcessBar.progress = user.process
                    binding.levelTextView.text = "Helper ${user.level} level"
                    buildChildUi()
                }
            }
        })
    }

    private fun buildParentUi(){
        binding.shareButton.visibility = View.GONE
        binding.addTaskButton.visibility = View.VISIBLE
        binding.doneCheckbox.visibility = View.VISIBLE
        binding.inProgressCheckbox.visibility = View.VISIBLE
        binding.changeButton.setBackgroundResource(R.drawable.ic_change);
    }

    private fun buildChildUi(){
        binding.shareButton.visibility = View.VISIBLE
        binding.addTaskButton.visibility = View.GONE
        binding.doneCheckbox.visibility = View.GONE
        binding.inProgressCheckbox.visibility = View.GONE
        binding.changeButton.setBackgroundResource(R.drawable.ic_edit_photo);
    }
}
