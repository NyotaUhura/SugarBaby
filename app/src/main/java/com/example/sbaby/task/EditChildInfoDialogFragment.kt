package com.example.sbaby.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sbaby.R

class EditChildInfoDialogFragment: DialogFragment(){
    companion object {
        const val TAG = "CreateGiftFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_edit_profile, container)
    }
}