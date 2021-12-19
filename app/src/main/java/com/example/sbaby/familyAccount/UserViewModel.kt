package com.example.sbaby.familyAccount

import com.airbnb.mvrx.*
import com.example.sbaby.FirebaseDataSource
import com.example.sbaby.GiftModel
import com.example.sbaby.User
import com.example.sbaby.gift.GiftState
import kotlinx.coroutines.launch

data class UserState(
    val user: Async<User> = Uninitialized
) : MavericksState

class UserViewModel(
    initialState: UserState,
    private val repository: FirebaseDataSource,
) : MavericksViewModel<UserState>(initialState) {

    init {
        setState {
            copy(user = Loading())
        }
        viewModelScope.launch {
            val user = repository.getUser()
            if (user != null) {
                setState {
                    copy(user = Success(user))
                }
            } else {
                setState { copy(user = Fail(NullPointerException())) }
            }
        }
    }


//    fun changeName(name: String) {
//        withState { state: UserState ->
//            val user = state.user.invoke() ?: return@withState
//            val newUser = { id: String ->
//                user.name == name
//            }
//
//            setState { copy(user = Success(newUser)) }
//        }
//    }
}