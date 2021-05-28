package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName

data class ListDocumentResponse(
	@field:SerializedName("policy")
	val documents: List<String>? = null
)
