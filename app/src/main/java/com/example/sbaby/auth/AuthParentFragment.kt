package com.example.sbaby.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.Result
import com.example.sbaby.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class AuthParentFragment : Fragment(R.layout.fragment_log_in) {

    private val authManager: FirebaseAuthManager by getKoin().inject()
    private val binding: FragmentLogInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDoneKeyAction()
        with(binding) {
            logIn.setOnClickListener {
                loginIn()
            }

            signUp.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.fragment_container, AuthParentSingUpFragment())
                }
            }
            invisibleButton.setOnClickListener {
                if (invisibleButton.visibility == View.VISIBLE) {
                    passwordEnterField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    invisibleButton.visibility = View.INVISIBLE
                    visibleButton.visibility = View.VISIBLE
                }
            }
            visibleButton.setOnClickListener {
                if (visibleButton.visibility == View.VISIBLE) {
                    passwordEnterField.transformationMethod = PasswordTransformationMethod.getInstance()
                    invisibleButton.visibility = View.VISIBLE
                    visibleButton.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setDoneKeyAction() {
        binding.emailEnterField.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.passwordEnterField.requestFocus()
                true
            } else false
        }
        binding.passwordEnterField.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                loginIn()
                true
            } else false
        }
    }

    private fun loginIn() {
        lifecycleScope.launchWhenCreated {
            val email = binding.emailEnterField.text.toString()
            val password = binding.passwordEnterField.text.toString()
            val result = authManager.loginIn(email, password)
            when (result) {
                is Result.Success -> {
                    Snackbar.make(requireView(), getString(R.string.sing_in_succ_mes), 2000).show()
                    (activity as AuthActivity).finishAuth(true)
                }
                is Result.Error -> {
                    val error = result.exception.localizedMessage ?: "Something is wrong"
                    Snackbar.make(requireView(), error, 2000).show()
                }
            }
        }
    }
}