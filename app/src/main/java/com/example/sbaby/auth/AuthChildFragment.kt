package com.example.sbaby.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.FirebaseDataSource
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentCodeLogInBinding
import org.koin.android.ext.android.getKoin

class AuthChildFragment : Fragment(R.layout.fragment_code_log_in) {

    private val authManager: FirebaseAuthManager by getKoin().inject()
    private val firebaseDataSource: FirebaseDataSource by getKoin().inject()
    private val binding: FragmentCodeLogInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            logIn.setOnClickListener {

            }
            signUp.setOnClickListener {
                //share link with parents
            }
        }
    }
}