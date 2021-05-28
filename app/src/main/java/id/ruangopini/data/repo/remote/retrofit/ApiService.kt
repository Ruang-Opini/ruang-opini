package id.ruangopini.data.repo.remote.retrofit

import id.ruangopini.data.model.ListPolicyCategoryResponse
import id.ruangopini.data.repo.remote.retrofit.response.ListDetailPolicyCategoryResponse
import id.ruangopini.data.repo.remote.retrofit.response.ListDetailPolicyTypeResponse
import id.ruangopini.data.repo.remote.retrofit.response.ListDocumentResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("AllPolicyCategories")
    suspend fun getAllPolicyCategory(): ListPolicyCategoryResponse

    @GET("PolicyByType")
    suspend fun getPolicyByType(
        @Query("url")
        url: String
    ): ListDetailPolicyTypeResponse

    @GET("PolicyByCategory")
    suspend fun getPolicyByCategory(
        @Query("url")
        url: String
    ): ListDetailPolicyCategoryResponse

    @GET("DocumentPolicy")
    suspend fun getDocumentPolicy(
        @Query("url")
        url: String
    ): ListDocumentResponse
}