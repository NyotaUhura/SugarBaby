package com.example.sbaby.viewholders.gift

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.GiftModel
import com.example.sbaby.R
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ParentGiftItemBinding

@EpoxyModelClass(layout = R.layout.parent_gift_item)
abstract class GiftCardViewHolder : ViewBindingEpoxyModelWithHolder<ParentGiftItemBinding>() {

    @EpoxyAttribute
    lateinit var gift: GiftModel

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ParentGiftItemBinding.bind() {
        giftTitleText.text = gift.title
        moneyText.text = "${gift.price}$"
        completeCheckbox.setOnCheckedChangeListener { compoundButton, _ ->
                onClickListeners.checkButtonOnclick(gift.id)
        }
    }

    interface buttonsOnclick {
        fun checkButtonOnclick(id: String)
    }
}