package com.example.sbaby.task

import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentTaskBinding
import com.example.sbaby.simpleController

class TaskFragment : MvRxBaseFragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by fragmentViewModel()
    private val binding: FragmentTaskBinding by viewBinding()

    override fun epoxyController() = simpleController(viewModel) { state ->
        val user = state.user.invoke() ?: return@simpleController
        val taskList = state.taskList.invoke() ?: return@simpleController
    }
}
