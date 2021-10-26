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
                "1", "Watch a film", "Free film", 20, 1),
            GiftModel(
                "2", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5),
            GiftModel(
                "1", "Watch a film", "Free film", 20, 1),
            GiftModel(
                "2", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5),
            GiftModel(
                "1", "Watch a film", "Free film", 20, 1),
            GiftModel(
                "2", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5),
            GiftModel(
                "1", "Watch a film", "Free film", 20, 1),
        )
    }
}