package com.example.sbaby.gift

import android.util.Log
import com.airbnb.mvrx.*
import com.example.sbaby.GiftModel
import com.example.sbaby.UserModel


data class GiftState(
    val user: Async<UserModel> = Uninitialized,
    val giftList: Async<List<GiftModel>> = Uninitialized
) : MavericksState

class GiftViewModel(
    initialState: GiftState,
    private val giftRepository: GiftRepository,
): MavericksViewModel<GiftState>(initialState) {

    init{
        setState {
            copy(user = Loading(), giftList = Loading())
        }

        val user = giftRepository.getUser()
        val giftList = giftRepository.getGiftList()
        setState {
            copy(user = Success(user), giftList = Success(giftList))
        }
    }


    companion object : MavericksViewModelFactory<GiftViewModel, GiftState> {
        override fun create(viewModelContext: ViewModelContext, state: GiftState): GiftViewModel {
            val rep = GiftRepository()
            return GiftViewModel(state, rep)
        }
    }

    fun filterGifts(isNeedToBeDone: Boolean, isNeedAgreement: Boolean){
        val giftList = giftRepository.getGiftList()
        val newList = giftList.filter { gift ->
            if (isNeedToBeDone || isNeedAgreement) {
                gift.IsAgree == isNeedToBeDone || gift.IsAgree != isNeedAgreement
            }
            else{
                false
            }
        }
        Log.d("R", newList.count().toString())
        setState {
            copy(giftList = Success(newList))
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
}