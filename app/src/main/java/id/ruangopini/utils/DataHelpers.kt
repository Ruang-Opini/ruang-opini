package id.ruangopini.utils

object DataHelpers {

    /** this
     * @param errorLoginMessage using for placeholder message from firebase auth
     */
    val errorLoginMessage =
        mapOf(
            "There is no user record corresponding to this identifier. The user may have been deleted." to "User tidak ditemukan",
            "The email address is badly formatted." to "Email tidak valid",
            "The email address is already in use by another account." to "Email sudah terdaftar",
            "The password is invalid or the user does not have a password." to "Password salah",
            "The given password is invalid. [ Password should be at least 6 characters ]" to "Password paling sedikit 6 karakter"
        )

    val titleMainTab = listOf(
        "Beranda", "Ruang Diskusi", "Referensi", "Profile"
    )

    val introData = listOf(
    )
}