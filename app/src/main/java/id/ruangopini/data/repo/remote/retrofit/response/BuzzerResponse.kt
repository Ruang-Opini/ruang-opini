package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class BuzzerResponse(

	@field:SerializedName("type")
	val type: BuzzerTypeResponse? = null
)

data class BuzzerTypeResponse(

	@field:SerializedName("buzzer")
	val buzzer: Int? = null,

	@field:SerializedName("non")
	val non: Int? = null
)
