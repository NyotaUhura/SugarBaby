package com.example.sbaby.gift

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sbaby.R
import com.example.sbaby.databinding.CardCreateGiftBinding
import com.example.sbaby.databinding.CardEditProfileBinding
import com.example.sbaby.task.TaskFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*


class CreateGiftDialogFragment(val edit : GiftFragment.editGift): DialogFragment(){
    private val binding: CardCreateGiftBinding by viewBinding()
    private var title: String? = null
    private var price: Int = 5
    private lateinit var id: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.card_create_gift, container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val mArgs = arguments
            //TODO: fields from id
            Log.d("args", mArgs.toString())
            if(mArgs?.containsKey("title") == true && mArgs.containsKey("price")  && mArgs.containsKey("id")){
                title = mArgs.getString("title")
                price = mArgs.getInt("price")
                id = mArgs.getString("id")!!
            }
            else{
                id = UUID.randomUUID().toString()
            }
        }
        else{
            id = UUID.randomUUID().toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.priceNumberPicker.minValue = 1
        binding.priceNumberPicker.maxValue = 999
        binding.priceNumberPicker.value = price
        binding.titleEditText.text = SpannableStringBuilder(title ?: "")
        binding.OKButton.setOnClickListener { _ ->
            title = binding.titleEditText.text.toString()
            price = binding.priceNumberPicker.value

            if(title != "") {
                Log.d("id: ", id)
                edit.updateGift(id, title!!, price)
                this.dismiss()
            }
        }
        binding.cancelButton.setOnClickListener { _ ->
            this.dismiss()
        }
    }

}