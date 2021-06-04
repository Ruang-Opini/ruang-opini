package id.ruangopini.data.repo.remote.retrofit

import id.ruangopini.data.repo.remote.retrofit.response.SentimentResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SentimentService {

    @GET("Responses")
    suspend fun getOffense(
        @Query("trending")
        trending: String
    ): SentimentResponse
}