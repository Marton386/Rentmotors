package com.anless.rentmotors.api.entities

data class SuggestionDTO (
    val id: Int,
    val value: String,
    val unrestricted_value: String,
    val data: SuggestionDataDTO
) {
    data class SuggestionDataDTO(
        val kpp: String?,
        val management: ManagementDTO?,
        val name: NameDTO,
        val inn: String,
        val ogrn: String,
        val address: AddressDTO
    ) {
        data class ManagementDTO(
            val name: String,
            val post: String,
            val disqualified: String?
        )

        data class NameDTO(
            val full_with_opf: String,
            val short_with_opf: String?,
            val latin: String?,
            val full: String?,
            val short: String?
        )

        data class AddressDTO(
            val value: String
        )
    }
}