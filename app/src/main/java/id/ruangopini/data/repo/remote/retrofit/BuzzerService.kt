package id.ruangopini.data.repo.remote.retrofit

import id.ruangopini.data.repo.remote.retrofit.response.BuzzerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BuzzerService {
    @GET("Buzzer")
    suspend fun getBuzzer(
        @Query("trending")
        trending: String
    ): BuzzerResponse
}