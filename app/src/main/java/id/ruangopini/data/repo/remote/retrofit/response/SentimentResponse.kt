package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class SentimentResponse(

	@field:SerializedName("tanggapan")
	val respond: RespondResponse? = null
)

data class RespondResponse(

	@field:SerializedName("negatif")
	val negative: Int? = null,

	@field:SerializedName("positif")
	val positive: Int? = null
)
