package com.example.sbaby.gift

import com.airbnb.mvrx.*
import com.example.sbaby.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

data class GiftState(
    val user: Async<User> = Uninitialized,
    val giftList: Async<List<GiftModel>> = Uninitialized
) : MavericksState

class GiftViewModel(
    initialState: GiftState,
    private val repository: FirebaseDataSource,
) : MavericksViewModel<GiftState>(initialState) {

    init {
        setState {
            copy(user = Loading(), giftList = Loading())
        }
        viewModelScope.launch {
            val user = repository.getUser()
            val giftList = repository.getGiftList()
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

    companion object : MavericksViewModelFactory<GiftViewModel, GiftState> {
        override fun create(viewModelContext: ViewModelContext, state: GiftState): GiftViewModel {
            val rep: FirebaseDataSource by viewModelContext.activity.inject()
            return GiftViewModel(state, rep)
        }
    }
}