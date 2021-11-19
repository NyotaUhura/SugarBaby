package com.example.sbaby.task

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.databinding.CardBuyPremiumBinding

class PremiumDialogFragment : DialogFragment() {
    private val binding: CardBuyPremiumBinding by viewBinding()

    companion object {
        const val TAG = "PremiumDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.card_buy_premium, container)
    }
}