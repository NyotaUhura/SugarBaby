package com.example.sbaby.gift

import android.util.Log
import com.airbnb.mvrx.*
import com.example.sbaby.Child
import com.example.sbaby.GiftModel
import com.example.sbaby.Parent


data class GiftState(
    val child: Async<Child> = Uninitialized,
    val parent: Async<Parent> = Uninitialized,
    val giftList: Async<List<GiftModel>> = Uninitialized
) : MavericksState

class GiftViewModel(
    initialState: GiftState,
    private val giftRepository: GiftRepository,
): MavericksViewModel<GiftState>(initialState) {

    init{
        setState {
            copy(child = Loading(), parent = Loading(), giftList = Loading())
        }

        val child = giftRepository.getChild()
        val parent = giftRepository.getParent()
        val giftList = giftRepository.getGiftList()
        setState {
            copy(child = Success(child), parent = Success(parent), giftList = Success(giftList))
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
                gift.isAgree == isNeedToBeDone || gift.isAgree != isNeedAgreement
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