package id.ruangopini.data.repo.remote.retrofit

import id.ruangopini.data.repo.remote.retrofit.response.TrendingResponse
import retrofit2.http.GET

interface TrendingService {
    @GET("Trending")
    suspend fun getTrending(): TrendingResponse
}