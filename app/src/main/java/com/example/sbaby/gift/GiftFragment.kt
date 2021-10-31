package com.example.sbaby.gift

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.Child
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentGiftBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.gift.GiftCardUnagreeViewHolder
import com.example.sbaby.epoxy.viewholders.gift.GiftCardViewHolder
import com.example.sbaby.epoxy.viewholders.gift.giftCardUnagreeViewHolder
import com.example.sbaby.epoxy.viewholders.gift.giftCardViewHolder

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
        val user = state.user.invoke()
        val giftList = state.giftList.invoke()

        // TODO: Add "plus" item
        giftList?.forEach { giftModel ->
            when (giftModel.isAgree) {
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

        viewModel.onAsync(GiftState::user, onSuccess = { user ->
            if (user is Child) {

                binding.userNameTextView.text = user.name
                binding.moneyTextView.text = user.money.toString()
            }
        })

//        recyclerView.layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)

        binding.needToBeDoneCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(
                binding.needToBeDoneCheckbox.isChecked,
                binding.needAgreementCheckbox.isChecked
            )
        }

        binding.needAgreementCheckbox.setOnCheckedChangeListener { _, _ ->
            viewModel.filterGifts(
                binding.needToBeDoneCheckbox.isChecked,
                binding.needAgreementCheckbox.isChecked
            )
        }

    }
}