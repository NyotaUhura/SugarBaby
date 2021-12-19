package com.example.sbaby.familyAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.Child
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.User
import com.example.sbaby.databinding.CardEditProfileBinding
import com.example.sbaby.databinding.FragmentGiftBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.gift.GiftCardViewHolder
import com.example.sbaby.gift.GiftState

class EditProfileFragment : MvRxBaseFragment(R.layout.card_edit_profile) {
    private val viewModel : UserViewModel by fragmentViewModel()
    private val binding: CardEditProfileBinding by viewBinding()

    override fun epoxyController() = simpleController(viewModel) { state ->
        val user = state.user;
        if (user is Success){
            val invokedUser = user.invoke();
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.onAsync(UserState::user, onSuccess = { user ->
//            var userName = user.name;
//            binding.nameEditText.text = userName.toString()
////            binding.photoEditImage.setImageURI(url)
//        })
//    }

}