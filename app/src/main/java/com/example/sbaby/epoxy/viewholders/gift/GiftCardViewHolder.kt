package com.example.sbaby.epoxy.viewholders.gift

import android.graphics.Color
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.GiftModel
import com.example.sbaby.R
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ChildGiftAddItemBinding
import com.example.sbaby.databinding.ChildGiftItemBinding
import com.example.sbaby.databinding.ParentGiftItemBinding
import com.example.sbaby.databinding.ParentGiftUnagreeItemBinding

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

@EpoxyModelClass(layout = R.layout.parent_gift_unagree_item)
abstract class GiftCardUnagreeViewHolder : ViewBindingEpoxyModelWithHolder<ParentGiftUnagreeItemBinding>() {

    @EpoxyAttribute
    lateinit var gift: GiftModel

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ParentGiftUnagreeItemBinding.bind() {
        giftTitleText.text = gift.title
        moneyText.text = "${gift.price}$"

        agreeButton.setOnClickListener {
            onClickListeners.agreeButtonOnclick(gift.id)
        }
        editButton.setOnClickListener {
            onClickListeners.editButtonOnclick(gift.id)
        }
        disagreeButton.setOnClickListener {
            onClickListeners.disagreeButtonOnclick(gift.id)
        }
    }

    interface buttonsOnclick {
        fun agreeButtonOnclick(id: String)
        fun editButtonOnclick(id: String)
        fun disagreeButtonOnclick(id: String)
    }
}

@EpoxyModelClass(layout = R.layout.child_gift_item)
abstract class GiftCardChildViewHolder : ViewBindingEpoxyModelWithHolder<ChildGiftItemBinding>() {

    @EpoxyAttribute
    lateinit var gift: GiftModel

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ChildGiftItemBinding.bind() {
        val colors = arrayOf(Color.GREEN, Color.GRAY, Color.BLUE,Color.MAGENTA)
        giftInfoCard.setCardBackgroundColor(colors[getHashOfString(gift.title) % colors.size])


        giftTitleText.text = gift.title
        moneyText.text = "${gift.price}$"
        giftInfoCard.setOnClickListener{
            onClickListeners.openButtonOnclick(gift.id)
        }
    }

    fun getHashOfString(string: String): Int {
        var res = 0

        string.forEach { char ->
            res += char.code
        }

        return res
    }

    interface buttonsOnclick {
        fun openButtonOnclick(id: String)
    }
}

@EpoxyModelClass(layout = R.layout.child_gift_add_item)
abstract class GiftCardChildAddViewHolder : ViewBindingEpoxyModelWithHolder<ChildGiftAddItemBinding>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    open lateinit var onClickListeners: buttonsOnclick

    override fun ChildGiftAddItemBinding.bind() {
        plusTextView.text = "+"
        moneyText.text = "New idea"
        giftInfoCard.setOnClickListener{
            onClickListeners.createButtonOnclick("-1")
        }
    }

    interface buttonsOnclick {
        fun createButtonOnclick(id: String)
    }
}