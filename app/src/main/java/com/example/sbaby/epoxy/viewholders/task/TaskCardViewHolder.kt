package com.example.sbaby.epoxy.viewholders.task

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.R
import com.example.sbaby.TaskModel
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.*

@EpoxyModelClass(layout = R.layout.item_task)
abstract class TaskCardViewHolder : ViewBindingEpoxyModelWithHolder<ItemTaskBinding>() {

    @EpoxyAttribute
    lateinit var task: TaskModel

    @EpoxyAttribute
    open var isParent: Boolean = false

    @EpoxyAttribute
    open var isDone: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ItemTaskBinding.bind() {
        if (isParent) buildParentUI(this)
        else buildChildUI(this)

        taskTitleTextView.text = task.title
        taskBodyTextView.text = task.description
        moneyTextView.text = "${task.profit}"

        when (task.deadline) {
            0L -> {
                circle.setBackgroundResource(R.drawable.task_circle_yellow)
                taskTimeTextView.visibility = View.GONE
            }
            else -> {
                circle.setBackgroundResource(R.drawable.task_circle_red)
                taskTimeTextView.visibility = View.VISIBLE
                val simpleDateFormat = SimpleDateFormat("dd MMM HH:mm")
                taskTimeTextView.text = "till ${simpleDateFormat.format(Date(task.deadline))}"
            }
        }
    }

    private fun buildParentUI(bind: ItemTaskBinding) {
        bind.doneButton.visibility = View.GONE
        bind.undoButton.visibility = View.VISIBLE
        bind.editButton.visibility = View.VISIBLE
        bind.deleteButton.visibility = View.VISIBLE
        if (isDone) {
            bind.undoButton.visibility = View.VISIBLE
            bind.deleteButton.visibility = View.GONE
        } else {
            bind.undoButton.visibility = View.GONE
            bind.deleteButton.visibility = View.VISIBLE
        }
        bind.undoButton.setOnClickListener {
            onClickListeners.undoneButtonOnclick(task.id)
        }
    }

    private fun buildChildUI(bind: ItemTaskBinding) {
        bind.doneButton.visibility = View.VISIBLE
        bind.undoButton.visibility = View.GONE
        bind.editButton.visibility = View.GONE
        bind.deleteButton.visibility = View.GONE
        bind.doneButton.setOnClickListener {
            onClickListeners.doneButtonOnclick(task.id)
        }
    }

    interface buttonsOnclick {
        fun undoneButtonOnclick(id: String)
        fun doneButtonOnclick(id: String)
        fun deleteButtonOnclick(id: String)
        fun editButtonOnClick(id: String)
    }
}
