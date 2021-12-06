package com.example.sbaby.epoxy.viewholders.settings

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.R
import com.example.sbaby.Setting
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ItemSettingsBinding

@EpoxyModelClass(layout = R.layout.item_settings)
abstract class SettingsCardViewHolder : ViewBindingEpoxyModelWithHolder<ItemSettingsBinding>() {
    @EpoxyAttribute
    lateinit var setting: Setting

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ItemSettingsBinding.bind() {
        imageView.setImageResource(setting.image)
        textView.text = setting.name
        settingsCard.setOnClickListener {
            when (setting.id) {
                2 -> onClickListeners.viewFamilyOnClick()
                3 -> onClickListeners.changeLanguageOnClick()
                4 -> onClickListeners.changePasswordOnClick()
                5 -> onClickListeners.privatePolicyOnClick()
                6 -> onClickListeners.rateOnClick()
                7 -> onClickListeners.logOutOnClick()
            }
        }
    }

    interface buttonsOnclick {
        fun viewFamilyOnClick()
        fun changeLanguageOnClick()
        fun changePasswordOnClick()
        fun privatePolicyOnClick()
        fun rateOnClick()
        fun logOutOnClick()
    }
}