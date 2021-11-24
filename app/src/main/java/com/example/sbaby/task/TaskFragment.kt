package com.example.sbaby.task

import android.content.Intent
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.*
import com.example.sbaby.databinding.FragmentTaskBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.task.TaskCardViewHolder
import com.example.sbaby.epoxy.viewholders.task.taskCardViewHolder

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
        val taskList = state.taskList
        val user = state.user.invoke() ?: return@simpleController
        if (taskList is Success) {
            val tasks = taskList.invoke()
            when (user) {
                is Parent -> {
                    renderParentTasks(tasks)
                }
                is Child -> {
                    renderChildTasks(tasks)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.onEach { state ->
            val family = state.family.invoke() ?: return@onEach
            val user = state.user.invoke() ?: return@onEach
            binding.photoImageView.load(user.photo)
            binding.premiumButton.setOnClickListener {
                val dialog = PremiumDialogFragment()
                val bundle = Bundle()
                dialog.arguments = bundle
                dialog.show(childFragmentManager, "DialogFragmentWithSetter")
            }
            when (user) {
                is Parent -> {
                    val currentChild = state.selectedChild.invoke()
                    bindParentUi(family.isPremium, currentChild)
                }
                is Child -> {
                    bindChildUi(user)
                }
            }
        }
    }

    private fun EpoxyController.renderChildTasks(tasks: List<TaskModel>) {
        tasks.forEach { taskModel ->
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

    private fun EpoxyController.renderParentTasks(tasks: List<TaskModel>) {
        tasks.forEach { taskModel ->
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
    }

    private fun bindParentUi(isPremium: Boolean, currentChild: Child?) {
        if (isPremium) binding.itemPremium.visibility = View.GONE
        else binding.premiumCard.visibility = View.VISIBLE

        if (currentChild != null) bindChild(currentChild)
        with(binding) {
            shareButton.visibility = View.GONE
            addTaskButton.visibility = View.VISIBLE
            doneCheckbox.visibility = View.VISIBLE
            inProgressCheckbox.visibility = View.VISIBLE
            changeButton.setBackgroundResource(R.drawable.ic_change)
            doneCheckbox.setOnCheckedChangeListener { _, _ ->
                viewModel.filterGifts(
                    doneCheckbox.isChecked,
                    inProgressCheckbox.isChecked
                )
            }
            inProgressCheckbox.setOnCheckedChangeListener { _, _ ->
                viewModel.filterGifts(
                    doneCheckbox.isChecked,
                    inProgressCheckbox.isChecked
                )
            }
        }
    }

    private fun bindChildUi(user: Child) {
        bindChild(user)
        with(binding) {
            shareButton.visibility = View.VISIBLE
            addTaskButton.visibility = View.GONE
            doneCheckbox.visibility = View.GONE
            inProgressCheckbox.visibility = View.GONE
            changeButton.setBackgroundResource(R.drawable.ic_edit_photo)
            changeButton.setOnClickListener {
                val dialog = EditChildInfoDialogFragment(edit)
                val bundle = Bundle()
                bundle.putString("name", user.name)
                bundle.putString("photo", user.photo)
                dialog.arguments = bundle
                dialog.show(childFragmentManager, "DialogFragmentWithSetter")
            }
            shareButton.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val textInfo =
                        "Wow! I am " + user.process / 1000 + 1 + " level Helper. Download and check out how much fun it is! Help your parents to get gifts and make your dreams come true! \n https://github.com/NyotaUhura/SugarBaby/tree/develop"
                    putExtra(Intent.EXTRA_TEXT, textInfo)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun bindChild(child: Child) {
        with(binding) {
            nameTextView.text = child.name
            moneyTextView.text = child.money.toString()
            levelProcessBar.progress = viewModel.countProcessPercent(child)
            levelTextView.text =
                getString(R.string.helper) + child.level + getString(R.string.level)
        }
    }

    interface editProfile {
        fun editName(name: String)
    }

    private val edit =
        object : editProfile {
            override fun editName(name: String) {
                viewModel.changeName(name)
            }
        }
}
