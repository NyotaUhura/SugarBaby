package com.example.sbaby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.auth.FirebaseAuthManager
import com.example.sbaby.databinding.FragmentSettingsBinding
import com.example.sbaby.databinding.FragmentSignUpBinding
import org.koin.android.ext.android.getKoin

class SettingsFragment : Fragment() {
    private val binding: FragmentSettingsBinding by viewBinding()
    private val authManager: FirebaseAuthManager by getKoin().inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logOutButton.setOnClickListener {
            authManager.logOut()
        }
    }
}
