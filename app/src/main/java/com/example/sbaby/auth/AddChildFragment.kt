package com.example.sbaby.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentAddChildBinding

class AddChildFragment : Fragment(R.layout.fragment_add_child) {

    private val binding: FragmentAddChildBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}