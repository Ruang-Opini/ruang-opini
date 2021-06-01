package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class TrendingResponse(
	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("trending")
	val trending: List<String>? = null
)
