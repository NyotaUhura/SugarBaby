package com.example.sbaby.viewholders.task

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.sbaby.R
import com.example.sbaby.UserModel
import com.example.sbaby.ViewBindingEpoxyModelWithHolder
import com.example.sbaby.databinding.ItemUserNameBinding

@EpoxyModelClass(layout = R.layout.item_user_name)
abstract class UserNameViewHolder: ViewBindingEpoxyModelWithHolder<ItemUserNameBinding>() {

    @EpoxyAttribute
    lateinit var user: UserModel

    override fun ItemUserNameBinding.bind() {
        userName.text = user.name
    }
}
