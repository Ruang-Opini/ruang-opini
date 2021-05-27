package id.ruangopini.data.repo.remote.retrofit

import id.ruangopini.data.model.ListPolicyCategoryResponse
import retrofit2.http.GET

interface ApiService {

    @GET("AllPolicyCategories")
    suspend fun getAllPolicyCategory(): ListPolicyCategoryResponse
}