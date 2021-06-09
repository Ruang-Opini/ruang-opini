package id.ruangopini.utils

/**
 * this object
 * @param COLLECTION using for collection reference in firebase firestore
 */
object COLLECTION {
    const val USER = "user"
    const val DISCUSSION = "discussion"
    const val ISSUE = "issue"
    const val POST = "post"
    const val COMMENT = "comment"
    const val HELP = "help"
    const val ANALYTICS = "analitics"

    const val CATEGORY = "category"
}

/**
 * this object
 * @param STORAGE using for location path where data will store in firebase storage
 */
object STORAGE {
    const val ROOT_AVA = "photos/ava/"
    const val ROOT_POST = "photos/post/"
    const val ROOT_BANNER = "photos/banner/"
}

object DateFormat {
    const val SHORT = "dd-MM-yyyy"
    const val PROFILE = "MMMM yyyy"
    const val POST = "EEEE, dd MMMM yyyy, hh.mm"
}

object PATH {
    const val AVA = "AVA_"
    const val POST = "POST_"
    const val BANNER = "BANNER_"
}

enum class LoginState {
    SUCCESS, WRONG_USERNAME, WRONG_PASSWORD
}