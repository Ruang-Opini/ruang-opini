package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyDocument

data class ListDetailPolicyCategoryResponse(

    @field:SerializedName("policy")
    val policy: List<PolicyItemCategoryResponse>? = null
)

data class PolicyItemCategoryResponse(

    @field:SerializedName("no")
    val no: String? = null,

    @field:SerializedName("year")
    val year: String? = null,

    @field:SerializedName("about")
    val about: String? = null,

    @field:SerializedName("link")
    val link: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("policyNum")
    val policyNum: String? = null
)

fun List<PolicyItemCategoryResponse>.toListPolicy(): List<Policy> {
    val listPolicy = mutableListOf<Policy>()
    this.forEach {
        listPolicy.add(
            Policy(
                it.about ?: "",
                it.policyNum ?: "",
                it.link ?: "",
                PolicyDocument(listOf()),
                it.name ?: "",
                it.year ?: ""
            )
        )
    }
    return listPolicy
}
