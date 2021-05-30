package id.ruangopini.utils

/**
 * this object
 * @param COLLECTION using for collection reference in firebase firestore
 */
object COLLECTION {
    const val USER = "user"
    const val DISCUSSION = "discussion"
}

/**
 * this object
 * @param STORAGE using for location path where data will store in firebase storage
 */
object STORAGE {
    const val ROOT_AVA = "photos/ava/"
}

object DateFormat {
    const val PROFILE = "MMMM yyyy"
}

object PATH {
    const val AVA = "AVA_"
}

enum class LoginState {
    SUCCESS, WRONG_USERNAME, WRONG_PASSWORD
}