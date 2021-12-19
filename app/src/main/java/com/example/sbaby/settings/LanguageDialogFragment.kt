package com.example.sbaby.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.databinding.CardChangeLanguageBinding
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources

import android.preference.PreferenceManager
import java.util.*


class LanguageDialogFragment(context: Context) : DialogFragment() {
    private val binding: CardChangeLanguageBinding by viewBinding()
    var newLang = "null"
    val thisContext = context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.card_change_language, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            englishButton.setOnClickListener {
                newLang = "en"
            }
            ukrainianButton.setOnClickListener {
                newLang = "uk"
            }
            okButton.setOnClickListener {
                LocaleHelper.setLocale(thisContext,newLang)
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
    }
}

object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    fun onCreate(context: Context) {
        val lang: String?
        lang = if (getLanguage(context)!!.isEmpty()) {
            getPersistedData(context, Locale.getDefault().getLanguage())
        } else {
            getLanguage(context)
        }
        setLocale(context, lang)
    }

    fun onCreate(context: Context, defaultLanguage: String) {
        val lang = getPersistedData(context, defaultLanguage)
        setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().getLanguage())
    }

    fun setLocale(context: Context, language: String?) {
        persist(context, language)
        updateResources(context, language)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
    }
}