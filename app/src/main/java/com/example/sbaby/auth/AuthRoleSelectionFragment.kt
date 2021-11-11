package com.example.sbaby.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentSelectionParentChildBinding
import com.example.sbaby.startFragment

class AuthRoleSelectionFragment : Fragment(R.layout.fragment_selection_parent_child) {

    private val binding: FragmentSelectionParentChildBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            childButton.setOnClickListener {
                startFragment(AuthChildFragment())
            }
            parentButton.setOnClickListener {
                startFragment(AuthParentFragment())
            }
        }
    }
}