package com.example.sbaby.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.FirebaseDataSource
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentCodeLogInBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class AuthChildFragment : Fragment(R.layout.fragment_code_log_in) {

    private val authManager: FirebaseAuthManager by getKoin().inject()
    private val firebaseDataSource: FirebaseDataSource by getKoin().inject()
    private val binding: FragmentCodeLogInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            logIn.setOnClickListener {
                val id = codeEnterField.text.toString()
                loging(id)
            }
            signUp.setOnClickListener {
                //share link with parents
            }
        }
    }

    private fun loging(id: String) {
        lifecycleScope.launchWhenCreated {
            val res = firebaseDataSource.isChildExist(id)
            if (res) {
                authManager.saveChildId(id)
                (activity as AuthActivity).finishAuth(true)
            } else {
                Snackbar.make(requireView(), "Wrong token", 2000).show()
            }
        }
    }
}