package com.example.sbaby.gift

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentGiftBinding
import com.example.sbaby.simpleController
import com.example.sbaby.viewholders.gift.GiftCardUnagreeViewHolder
import com.example.sbaby.viewholders.gift.GiftCardViewHolder
import com.example.sbaby.viewholders.gift.giftCardUnagreeViewHolder
import com.example.sbaby.viewholders.gift.giftCardViewHolder

class GiftFragment : MvRxBaseFragment(R.layout.fragment_gift) {
    companion object {
        private const val NUMBER_OF_COLUMNS = 2
    }

    private val viewModel: GiftViewModel by fragmentViewModel()
    private val binding: FragmentGiftBinding by viewBinding()

    private val buttons: GiftCardViewHolder.buttonsOnclick =
        object : GiftCardViewHolder.buttonsOnclick {
            override fun checkButtonOnclick(id: String) {
                viewModel.changeCheckGiftStatus(id)
            }

        }

    private val buttonsUnagree: GiftCardUnagreeViewHolder.buttonsOnclick =
        object : GiftCardUnagreeViewHolder.buttonsOnclick {
            override fun agreeButtonOnclick(id: String) {
                viewModel.changeIsAgreeGiftStatus(id)
            }

            override fun editButtonOnclick(id: String) {
                viewModel.changeDataGiftStatus(id)
            }

            override fun disagreeButtonOnclick(id: String) {
                viewModel.deleteGift(id)
            }
        }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val child = state.child.invoke()
        val giftList = state.giftList.invoke()

        // TODO: Add "plus" item
        giftList?.forEach { giftModel ->
            when(giftModel.isAgree){
                true -> giftCardViewHolder {
                    id(giftModel.id)
                    gift(giftModel)
                    onClickListeners(buttons)
                }
                false -> giftCardUnagreeViewHolder {
                    id(giftModel.id)
                    gift(giftModel)
                    onClickListeners(buttonsUnagree)
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAsync(GiftState::child, onSuccess = { child ->
            binding.userNameTextView.text = child.name
            binding.moneyTextView.text = child.money.toString()
        })

//        recyclerView.layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)

        binding.needToBeDoneCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(binding.needToBeDoneCheckbox.isChecked, binding.needAgreementCheckbox.isChecked)
        }

        binding.needAgreementCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(binding.needToBeDoneCheckbox.isChecked, binding.needAgreementCheckbox.isChecked)
        }

    }
}