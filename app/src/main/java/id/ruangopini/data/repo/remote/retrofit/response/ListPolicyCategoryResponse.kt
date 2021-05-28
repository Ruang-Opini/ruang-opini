package id.ruangopini.data.model

import com.google.gson.annotations.SerializedName

data class ListPolicyCategoryResponse(
	@field:SerializedName("category")
	val category: CategoryResponse? = null
)

data class PolicyCategoryResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class CategoryResponse(

	@field:SerializedName("jenis")
	val typeOfPolicy: List<PolicyCategoryResponse>? = null,

	@field:SerializedName("kategori")
	val categoryOfPolicy: List<PolicyCategoryResponse>? = null
)

fun List<PolicyCategoryResponse>.toPolicyCategory(): MutableList<PolicyCategory> {
	val data = mutableListOf<PolicyCategory>()
	this.forEach {
		data.add(PolicyCategory(it.name ?: "", it.url ?: ""))
	}
	return data
}
