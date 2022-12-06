package com.anless.rentmotors.models.converters

import com.anless.rentmotors.models.Suggestion
import com.anless.rentmotors.api.entities.SuggestionDTO

class OrgConverter {
    fun toModel(remote: SuggestionDTO) = Suggestion(
        remote.id,
        remote.value,
        remote.unrestricted_value,
        Suggestion.SuggestionData(
            remote.data.kpp,
            remote.data.management?.let {
                Suggestion.SuggestionData.Management(
                    it.name,
                    it.post,
                    it.disqualified
                )
            },
            Suggestion.SuggestionData.Name(
                remote.data.name.full_with_opf,
                remote.data.name.short_with_opf,
                remote.data.name.latin,
                remote.data.name.full,
                remote.data.name.short
            ),
            remote.data.inn,
            remote.data.ogrn,
            Suggestion.SuggestionData.Address(
                remote.data.address?.value
            )
        )
    )
}