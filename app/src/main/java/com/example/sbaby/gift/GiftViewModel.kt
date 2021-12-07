package com.example.sbaby.gift

import android.os.Bundle
import android.util.Log
import com.airbnb.mvrx.*
import com.example.sbaby.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

data class GiftState(
    val user: Async<User> = Uninitialized,
    val giftList: Async<List<GiftModel>> = Uninitialized
) : MavericksState

class GiftViewModel(
    initialState: GiftState,
    private val firebaseDataSource: FirebaseDataSource,
) : MavericksViewModel<GiftState>(initialState) {

    init {
        setState {
            copy(user = Loading(), giftList = Loading())
        }
        viewModelScope.launch {
            val user = firebaseDataSource.getUser()
            val giftList = firebaseDataSource.getGiftList()
            if (user != null) {
                setState {
                    copy(user = Success(user), giftList = Success(giftList))
                }
            } else {
                setState { copy(user = Fail(NullPointerException())) }
            }
        }
    }

    fun filterGifts(isNeedToBeDone: Boolean, isNeedAgreement: Boolean) {
        withState { state ->
            viewModelScope.launch {
                val giftList = firebaseDataSource.getGiftList()
                val newList = giftList.filter { gift ->
                    if (isNeedToBeDone || isNeedAgreement) {
                        gift.isAgree == isNeedToBeDone || gift.isAgree != isNeedAgreement
                    } else {
                        false
                    }
                }
                setState {
                    copy(giftList = Success(newList))
                }
            }

        }
    }

    fun getTitleGift(id: String): String {
        var title = "";
        viewModelScope.launch {
            val giftList = firebaseDataSource.getGiftList()
            val gift = giftList.find {
                it.id == id
            }

            title = gift!!.title;
        }
        Log.d("", title)
        return title
    }

    fun getPriceGift(id: String): Int {
        var price = -1;
        withState { state ->
            val giftList = state.giftList.invoke() ?: return@withState
            val gift = giftList.find {
                it.id == id
            }


            price = gift!!.price;
        }
        return price;
    }


    fun changeCheckGiftStatus(id: String, money: Int) {
        withState { state ->
            viewModelScope.launch {
                val parent = (state.user.invoke()!! as Parent)
                val children = parent.childList.toMutableList()
                val child = children[0]
                val gifts_inx = child.gifts - id
                val newChild = child.copy(gifts = gifts_inx, money = child.money - money)
                firebaseDataSource.saveUser(newChild, update = false)

                val newGiftList = firebaseDataSource.getGiftList()

                setState { copy(giftList = Success(newGiftList)) }
            }
        }
    }


    fun changeDataGiftStatus(id: String) {
        TODO("Not yet implemented")
    }


    fun deleteGift(id: String) {
        withState { state ->
            viewModelScope.launch {
                val parent = (state.user.invoke()!! as Parent)
                val children = parent.childList.toMutableList()
                val child = children[0]
                val gifts_inx = child.gifts - id
                val newChild = child.copy(gifts = gifts_inx)
                firebaseDataSource.saveUser(newChild, update = false)

                val newGiftList = firebaseDataSource.getGiftList()

                setState { copy(giftList = Success(newGiftList)) }
            }
        }
    }

    fun changeIsAgreeGiftStatus(id: String) {
        withState { state ->
            val giftList = state.giftList.invoke()
            val giftt = giftList?.firstOrNull {
                it.id == id
            }
            val gift: GiftModel = giftt!!.copy(isAgree = true)


            viewModelScope.launch {
                val newGiftList = firebaseDataSource.updateGift(gift)
                setState { copy(giftList = Success(newGiftList)) }
            }
        }
    }

    fun updateGift(id: String, title: String, price: Int){
        withState { state ->
            val giftList = state.giftList.invoke()
            val giftt = giftList?.firstOrNull {
                it.id == id
            }
            val gift: GiftModel =
                giftt?.copy(title = title, price = price) ?: GiftModel(id, title, title, price, 1, false)

            viewModelScope.launch {
                val newGiftList = firebaseDataSource.updateGift(gift)
                when(state.user.invoke()){
                    is Child ->{
                        val gifts_inx = (state.user.invoke()!! as Child).gifts + id
                        val user = (state.user.invoke()!! as Child).copy(gifts = gifts_inx)
                        firebaseDataSource.saveUser(user)
                    }
                    is Parent ->{
                        val gifts_inx = (state.user.invoke()!! as Parent).childList[0].gifts + id
                        val user = (state.user.invoke()!! as Parent).childList[0].copy(gifts = gifts_inx)
                        firebaseDataSource.saveUser(user)
                    }
                }
                setState { copy(giftList = Success(newGiftList)) }
            }
        }
    }


    companion object : MavericksViewModelFactory<GiftViewModel, GiftState> {
        override fun create(viewModelContext: ViewModelContext, state: GiftState): GiftViewModel {
            val rep: FirebaseDataSource by viewModelContext.activity.inject()
            return GiftViewModel(state, rep)
        }
    }
}