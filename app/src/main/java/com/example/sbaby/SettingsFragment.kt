package com.example.sbaby

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.auth.FirebaseAuthManager
import com.example.sbaby.databinding.FragmentSettingsBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.settings.SettingsCardViewHolder
import com.example.sbaby.epoxy.viewholders.settings.settingsCardViewHolder
import org.koin.android.ext.android.getKoin

data class Setting(
    val id: Int,
    val image: Int,
    val name: String,
)

class SettingsFragment : MvRxBaseFragment(R.layout.fragment_settings) {
    private val binding: FragmentSettingsBinding by viewBinding()
    private val authManager: FirebaseAuthManager by getKoin().inject()

    private val buttons: SettingsCardViewHolder.buttonsOnclick =
        object : SettingsCardViewHolder.buttonsOnclick {
            override fun editProfileOnClick() {
                TODO("Not yet implemented")
            }

            override fun viewFamilyOnClick() {
                TODO("Not yet implemented")
            }

            override fun changeLanguageOnClick() {
                TODO("Not yet implemented")
            }

            override fun changePasswordOnClick() {
                TODO("Not yet implemented")
            }

            override fun privatePolicyOnClick() {
                TODO("Not yet implemented")
            }

            override fun rateOnClick() {
                TODO("Not yet implemented")
            }

            override fun logOutOnClick() {
                authManager.logOut()
            }

        }

    private val settingsList = mutableListOf<Setting>()

    override fun epoxyController() = simpleController() {
        settingsList.add(Setting(1, R.drawable.ic_edit, "Edit profile photo"))
        settingsList.add(Setting(2, R.drawable.ic_family, "Family"))
        settingsList.add(Setting(3, R.drawable.ic_language, "Language"))
        settingsList.add(Setting(4, R.drawable.ic_password, "Change password"))
        settingsList.add(Setting(5, R.drawable.ic_private, "Privacy Policy"))
        settingsList.add(Setting(6, R.drawable.ic_rate, "Rate the app"))
        settingsList.add(Setting(7, R.drawable.ic_log_out, "Log out"))
        settingsList.forEach { s ->
            settingsCardViewHolder {
                id(s.id)
                setting(s)
                onClickListeners(buttons)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.requestModelBuild()
    }
}
