package com.example.sbaby.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.FirebaseDataSource
import com.example.sbaby.R
import com.example.sbaby.Result
import com.example.sbaby.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class AuthParentSingUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding: FragmentSignUpBinding by viewBinding()
    private val authManager: FirebaseAuthManager by getKoin().inject()
    private val firebaseDataSource: FirebaseDataSource by getKoin().inject()
    private var isPasswordVisible = false
    private var isPasswordVisible1 = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            signUp.setOnClickListener {
                singUp()
            }
            visibleButton.setOnClickListener {
                if (isPasswordVisible) {
                    isPasswordVisible = false
                    passwordEnterField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    visibleButton.setImageResource(R.drawable.ic_visible)
                } else {
                    isPasswordVisible = true
                    passwordEnterField.transformationMethod = PasswordTransformationMethod.getInstance()
                    visibleButton.setImageResource(R.drawable.ic_invisible)
                }
            }
            visibleButton1.setOnClickListener {
                if (isPasswordVisible1) {
                    isPasswordVisible1 = false
                    passwordAgainEnterField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    visibleButton1.setImageResource(R.drawable.ic_visible)
                } else {
                    isPasswordVisible1 = true
                    passwordAgainEnterField.transformationMethod = PasswordTransformationMethod.getInstance()
                    visibleButton1.setImageResource(R.drawable.ic_invisible)
                }
            }
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
                            lifecycleScope.launchWhenCreated {
                                var familyId: String? = familyEnterField.text.toString()
                                if (familyId!!.isEmpty()) familyId = null
                                val res = saveUser(result.data, familyId)
                                if (res) {
                                    Snackbar.make(requireView(), "Done", 2000).show()
                                    bindAddChildFragment()
                                } else {
                                    Snackbar.make(requireView(), "Something is wrong", 2000).show()
                                }
                            }
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

    private suspend fun saveUser(id: String, familyId: String?): Boolean {
        return firebaseDataSource.saveMockUser(id, true, familyId)
    }

    private fun bindAddChildFragment() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace(R.id.fragment_container, AddChildFragment())
        }
    }
}