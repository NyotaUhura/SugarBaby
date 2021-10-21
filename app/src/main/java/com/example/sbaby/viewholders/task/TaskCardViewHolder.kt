package com.example.sbaby.viewholders.task

import android.graphics.Color
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.R
import com.example.sbaby.TaskModel
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ParentTaskItemBinding

@EpoxyModelClass(layout = R.layout.parent_task_item)
abstract class TaskCardViewHolder : ViewBindingEpoxyModelWithHolder<ParentTaskItemBinding>() {

    @EpoxyAttribute
    lateinit var task: TaskModel

    override fun ParentTaskItemBinding.bind() {
        taskTitle.text = task.title
        taskTime.text = task.deadline.toString()
        taskBodyText.text = task.description
        circle.setBackgroundColor(
            when (task.deadline) {
                0L -> Color.BLUE
                else -> Color.RED
            }
        )
        moneyText.text = "${task.profit}$"
    }
}
