package com.example.sbaby.gift

import com.example.sbaby.*

class GiftRepository {

    // TODO
    fun getUser() : UserModel {
        return UserModel("124", "Artem")
    }

    fun getGiftList(): List<GiftModel> {
        return listOf(
            GiftModel(
                "1", "Watch film", "Free film", 20, 1),
            GiftModel(
                "2", "Play on PC", "10 minutes", 5, 5)
        )
    }
}