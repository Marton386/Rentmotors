package com.anless.rentmotors.repositories

import retrofit2.HttpException
import com.anless.rentmotors.api.OrgAPI
import com.anless.rentmotors.utils.Result
import com.anless.rentmotors.models.Suggestion
import com.anless.rentmotors.api.requests.OrgRequest
import com.anless.rentmotors.models.converters.OrgConverter
import com.anless.rentmotors.utils.RetrofitParseException

class OrgRepository(
    private val orgAPI: OrgAPI,
    private val orgConverter: OrgConverter
) {
    suspend fun getOrgByNameOrInn(body: String): Result<List<Suggestion>> {
        return try {
            val response = orgAPI.getOrg(OrgRequest(body),
                "4e522d48a714165da8c5666a925f2269b6512e71")
            if (response.isSuccessful) {
                val suggestions = response.body()?.suggestions?.map { suggestItem ->
                    orgConverter.toModel(suggestItem)
                } ?: throw RetrofitParseException()
                Result.Success(suggestions)
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            Result.Error(e.code())
        }
    }
}