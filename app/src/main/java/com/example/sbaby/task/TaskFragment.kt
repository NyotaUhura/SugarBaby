package com.example.sbaby.task

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.google.android.material.snackbar.Snackbar

class TaskFragment : MvRxBaseFragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by fragmentViewModel()
    private val binding: FragmentTaskBinding by viewBinding()

    private val buttons: TaskCardViewHolder.buttonsOnclick =
        object : TaskCardViewHolder.buttonsOnclick {
            override fun doneButtonOnclick(id: String) {
                viewModel.changeUndoneTaskStatus(id)
                Snackbar.make(requireView(), "Congratulations! You have completed task!", Snackbar.LENGTH_LONG).show()
            }

            override fun deleteButtonOnclick(id: String) {
                viewModel.deleteTask(id)
            }

            override fun editButtonOnClick(id: String) {
                TODO("Not yet implemented")
            }

            override fun undoneButtonOnclick(id: String) {
                viewModel.changeDoneTaskStatus(id)
            }
        }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val user = state.user.invoke() ?: return@simpleController
        when(user) {
            is Parent -> {
                val selectedChild = state.selectedChild.invoke()?: return@simpleController
                renderParentTasks(selectedChild.taskList)
            }
            is Child -> {
                val taskList = state.taskList.invoke()?: return@simpleController
                renderChildTasks(taskList)
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
            addTaskButton.setOnClickListener {
                val dialog = AddTaskDialogFragment(add)
                dialog.show(childFragmentManager, "DialogFragmentWithSetter")
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
                        getString(R.string.Wow_I_am) + user.level + getString(R.string.share1)+  "\n " + getString(R.string.share2)
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

        fun editPhoto(bitmap: Bitmap)
    }

    private val edit =
        object : editProfile {
            override fun editName(name: String) {
                viewModel.changeName(name)
            }

            override fun editPhoto(bitmap: Bitmap) {
                binding.photoImageView.setImageBitmap(bitmap)
            }
        }

    interface addTask {
        fun save(task: TaskModel)
    }

    private val add =
        object : addTask {
            override fun save(task: TaskModel) {
                viewModel.addTask(task)
            }
        }
}
