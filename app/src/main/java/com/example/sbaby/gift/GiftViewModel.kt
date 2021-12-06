package com.example.sbaby.gift

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
            val giftList = state.giftList.invoke() ?: return@withState
            val newList = giftList.filter { gift ->
                if (isNeedToBeDone || isNeedAgreement) {
                    gift.agree == isNeedToBeDone || gift.agree != isNeedAgreement
                } else {
                    false
                }
            }
            setState {
                copy(giftList = Success(newList))
            }
        }
    }

    fun getTitleGift(id: String): String {
        var title = "";
        withState { state ->
            val giftList = state.giftList.invoke() ?: return@withState
            val gift = giftList.find {
                it.id == id
            }

            title = gift!!.title;
        }
        return title;
    }


    fun changeCheckGiftStatus(id: String) {
        withState { state: GiftState ->
            val giftList = state.giftList.invoke() ?: return@withState
            val newGiftList = giftList.filter { gift ->
                gift.id != id
            }
            // TODO: DELETE FROM DB
            // TODO: MINUS COINS FROM CHILD

            setState { copy(giftList = Success(newGiftList)) }
        }
    }

    fun changeIsAgreeGiftStatus(id: String) {
        withState { state: GiftState ->
            val giftList = state.giftList.invoke() ?: return@withState
            val newGiftList = giftList.filter { gift ->
                gift.id != id
            }
            // TODO: DELETE FROM DB

            setState { copy(giftList = Success(newGiftList)) }
        }
    }

    fun changeDataGiftStatus(id: String) {
        TODO("Not yet implemented")
    }


    fun deleteGift(id: String) {
        withState { state: GiftState ->
            val giftList = state.giftList.invoke() ?: return@withState
            val newGiftList = giftList.filter { gift ->
                gift.id != id
            }
            // TODO: DELETE FROM DB

            setState { copy(giftList = Success(newGiftList)) }
        }
    }

    fun updateGift(id: String, title: String, price: Int){
        withState { state ->
            val giftList = state.giftList.invoke()
            val giftt = giftList?.firstOrNull {
                it.id == id
            }
            val gift: GiftModel = if(giftt != null) {
                giftt.copy(title = title, price = price)
            } else{
                GiftModel(UUID.randomUUID().toString(), title, title, price, 1, false)
            }

            viewModelScope.launch {
                val newGiftList = firebaseDataSource.updateGift(gift)
                setState { copy(giftList = Success(newGiftList)) }
            }
        }
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

    companion object : MavericksViewModelFactory<GiftViewModel, GiftState> {
        override fun create(viewModelContext: ViewModelContext, state: GiftState): GiftViewModel {
            val rep: FirebaseDataSource by viewModelContext.activity.inject()
            return GiftViewModel(state, rep)
        }
    }
}