package com.example.sbaby

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.auth.FirebaseAuthManager
import com.example.sbaby.databinding.FragmentSettingsBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.settings.SettingsCardViewHolder
import com.example.sbaby.epoxy.viewholders.settings.settingsCardViewHolder
import com.example.sbaby.settings.LanguageDialogFragment
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
            override fun viewFamilyOnClick() {
                TODO("Not yet implemented")
            }

            override fun changeLanguageOnClick() {
                val dialog = LanguageDialogFragment(context!!)
                dialog.show(childFragmentManager, "DialogFragmentWithSetter")
            }

            override fun changePasswordOnClick() {
                TODO("Not yet implemented")
            }

            override fun privatePolicyOnClick() {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/NyotaUhura/SugarBaby")
                startActivity(i)
            }

            override fun rateOnClick() {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/NyotaUhura/SugarBaby")
                startActivity(i)
            }

            override fun logOutOnClick() {
                authManager.logOut()
            }

        }

    private val settingsList = mutableListOf<Setting>()

    override fun epoxyController() = simpleController() {
        settingsList.add(Setting(2, R.drawable.ic_family, getString(R.string.Family)))
        settingsList.add(Setting(3, R.drawable.ic_language, getString(R.string.Language)))
        settingsList.add(Setting(4, R.drawable.ic_password, getString(R.string.Change_password)))
        settingsList.add(Setting(5, R.drawable.ic_private, getString(R.string.Privacy_Policy)))
        settingsList.add(Setting(6, R.drawable.ic_rate, getString(R.string.Rate_the_app)))
        settingsList.add(Setting(7, R.drawable.ic_log_out, getString(R.string.Log_out)))
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
