package com.example.sbaby.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.Result
import com.example.sbaby.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class AuthParentSingUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding()
    private val authManager: FirebaseAuthManager by getKoin().inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUp.setOnClickListener {
            singUp()
        }
    }

    private fun singUp() {
        lifecycleScope.launchWhenCreated {
            with(binding) {
                if (passwordAgainEnterField.text.toString() == passwordEnterField.text.toString()) {
                    val email = emailEnterField.text.toString()
                    val password = passwordEnterField.text.toString()
                    val result = authManager.singUp(email, password)
                    when (result) {
                        is Result.Success -> {
                            Snackbar.make(requireView(), getString(R.string.login_in), 2000).show()
                            bindAddChildFragment()
                        }
                        is Result.Error -> {
                            val error = result.exception.localizedMessage ?: getString(R.string.helper)
                            Snackbar.make(requireView(), error, 2000).show()
                        }
                    }
                } else {
                    Snackbar.make(requireView(), getString(R.string.passwords_not_match), 2000).show()
                }
            }
        }
    }

    private fun bindAddChildFragment() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace(R.id.fragment_container, AddChildFragment())
        }
    }
}