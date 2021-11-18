package com.example.sbaby.gift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.sbaby.R


class CreateGiftDialogFragment: DialogFragment(){
    companion object {
        const val TAG = "CreateGiftFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_create_gift, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val num = view.findViewById<NumberPicker>(R.id.priceNumberPicker)
        num.minValue = 1
        num.maxValue = 999
    }

}