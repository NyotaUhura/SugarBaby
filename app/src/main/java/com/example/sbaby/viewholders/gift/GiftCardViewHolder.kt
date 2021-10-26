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

    override fun ParentGiftItemBinding.bind() {
        giftTitle.text = gift.title
        giftBodyText.text = gift.description
        moneyText.text = "${gift.price}$"
        giftCount.text = "${gift.availableCount}"
    }
}