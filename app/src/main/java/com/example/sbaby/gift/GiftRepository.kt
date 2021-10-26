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
                "1", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "2", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
            GiftModel(
                "3", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "4", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
            GiftModel(
                "5", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "6", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
            GiftModel(
                "7", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "8", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
            GiftModel(
                "9", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "10", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
            GiftModel(
                "11", "Watch a film", "Free film", 20, 1, true),
            GiftModel(
                "12", "Play on PC for 10 minutes", "10 minutes for some game", 5, 5, false),
        )
    }
}