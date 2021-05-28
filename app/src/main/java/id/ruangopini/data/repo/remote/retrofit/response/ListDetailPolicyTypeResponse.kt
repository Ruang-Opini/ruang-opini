package id.ruangopini.data.repo.remote.retrofit.response

import com.google.gson.annotations.SerializedName
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyDocument

data class ListDetailPolicyTypeResponse(

    @field:SerializedName("policy")
    val policy: List<PolicyItemTypeResponse>? = null
)

data class PolicyItemTypeResponse(

    @field:SerializedName("uu")
    val uu: String? = null,

    @field:SerializedName("no")
    val no: String? = null,

    @field:SerializedName("link")
    val link: List<String>? = null,

    @field:SerializedName("name")
    val name: String? = null
)

fun List<PolicyItemTypeResponse>.toListPolicy(): List<Policy> {
    val listPolicy = mutableListOf<Policy>()
    this.forEach {
        listPolicy.add(
            Policy(
                it.name ?: "",
                it.uu ?: "",
                "",
                PolicyDocument(it.link ?: listOf()),
                "", ""
            )
        )
    }
    return listPolicy
}
