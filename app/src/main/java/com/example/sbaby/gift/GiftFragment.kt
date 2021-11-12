package com.example.sbaby.gift

import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.example.sbaby.*
import com.example.sbaby.databinding.FragmentGiftBinding
import com.example.sbaby.epoxy.simpleController
import com.example.sbaby.epoxy.viewholders.gift.*

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

    private val buttonsOpen: GiftCardChildViewHolder.buttonsOnclick =
        object : GiftCardChildViewHolder.buttonsOnclick {
            override fun openButtonOnclick(id: String) {
                //TODO: Open dialog window to edit
            }
        }

    private val buttonsCreate: GiftCardChildAddViewHolder.buttonsOnclick =
        object : GiftCardChildAddViewHolder.buttonsOnclick {
            override fun createButtonOnclick(id: String) {
                //TODO: Open dialog window to add
            }
        }

    override fun epoxyController() = simpleController(viewModel) { state ->
        val user = state.user.invoke()
        val giftList = state.giftList
        // TODO: Add "plus" item

        if (giftList is Success) {
            val gifts = giftList.invoke()
            when (user) {
                is Parent -> {
                    renderParentTasks(gifts)
                }
                is Child -> {
                    giftCardChildAddViewHolder {
                        id("plus")
                        onClickListeners(buttonsCreate) }
                    renderChildTasks(gifts)
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAsync(GiftState::user, onSuccess = { user ->
            when (user) {
                is Parent -> {
                    binding.needToBeDoneCheckbox.isVisible = true;
                    binding.needToBeDoneCheckbox.setOnCheckedChangeListener { _, _ ->
                        viewModel.filterGifts(
                            binding.needToBeDoneCheckbox.isChecked,
                            binding.needAgreementCheckbox.isChecked
                        )
                    }

                    binding.needAgreementCheckbox.isVisible = true;
                    binding.needAgreementCheckbox.setOnCheckedChangeListener { _, _ ->
                        viewModel.filterGifts(
                            binding.needToBeDoneCheckbox.isChecked,
                            binding.needAgreementCheckbox.isChecked
                        )
                    }
                }
                is Child -> {

                    val params = binding.recyclerView.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = binding.topInfoMaterialCardView.id
                    binding.recyclerView.requestLayout()

                    recyclerView.layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)


                    binding.userNameTextView.text = user.name
                    binding.moneyTextView.text = user.money.toString()
                }
            }
        })
    }

    private fun EpoxyController.renderParentTasks(gifts: List<GiftModel>) {

        gifts.forEach { giftModel ->
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

    private fun EpoxyController.renderChildTasks(gifts: List<GiftModel>) {
        gifts.forEach { giftModel ->
            giftCardChildViewHolder {
                id(giftModel.id)
                gift(giftModel)
                onClickListeners(buttonsOpen)
            }
        }
    }
}

