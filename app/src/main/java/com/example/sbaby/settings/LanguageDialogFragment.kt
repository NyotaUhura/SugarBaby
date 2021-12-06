package com.example.sbaby.settings

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

class LanguageDialogFragment : DialogFragment() {
    private val binding: CardChangeLanguageBinding by viewBinding()
    var newLang = "null"

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
        binding.englishButton.setOnClickListener {
            newLang = "eng"
        }
        binding.ukrainianButton.setOnClickListener {
            newLang = "ukr"
        }
        binding.okButton.setOnClickListener {
            //save
        }
    }
}