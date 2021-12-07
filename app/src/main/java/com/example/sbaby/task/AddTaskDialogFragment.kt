package com.example.sbaby.task

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.TO_DO
import com.example.sbaby.TaskModel
import com.example.sbaby.databinding.AddTaskItemBinding
import java.util.*

class AddTaskDialogFragment(val add: TaskFragment.addTask) : DialogFragment() {

    private val binding: AddTaskItemBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.add_task_item, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.okButton.setOnClickListener {
            val newTask = TaskModel(
                UUID.randomUUID().toString(),
                binding.titleTextView.text.toString(),
                0,
                binding.descriptionTextView.text.toString(),
                binding.priseEditText.text.toString().toInt(),
                TO_DO
            )
            add.save(newTask)
            this.dismiss()
        }
    }
}