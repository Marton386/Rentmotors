package com.anless.rentmotors.models

class Suggestion(
    val id: Int,
    val value: String,
    val unrestrictedValue: String,
    val data: SuggestionData
) {
    data class SuggestionData(
        val kpp: String?,
        val management: Management?,
        val name: Name,
        val inn: String,
        val ogrn: String,
        val address: Address
    ) {
        data class Management(
            val name: String,
            val post: String,
            val disqualified: String?
        )

        data class Name(
            val fullWithOpf: String,
            val shortWithOpf: String?,
            val latin: String?,
            val full: String?,
            val short: String?
        )

        data class Address(
            val value: String
        )
    }
}