package id.ruangopini.data.model

data class Policy(
    val name: String,
    val policyNum: String,
    var url: String, // for category
    var documents: PolicyDocument,
    val type: String, // for category
    val year: String // for category
)

data class PolicyDocument(
    val documents: List<String>
)
