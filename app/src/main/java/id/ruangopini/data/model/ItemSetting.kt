package id.ruangopini.data.model

data class ItemSetting(
    val name: String,
    val target: Class<*>? = null
)

data class AboutUs(
    val title: String,
    val content: String
)

data class ItemHelp(
    val question: String,
    val answer: String,
    val icons: List<String>,
    var isExpand: Boolean? = false
)
