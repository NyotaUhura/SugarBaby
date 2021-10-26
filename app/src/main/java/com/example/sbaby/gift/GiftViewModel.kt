package com.example.sbaby.gift

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
}