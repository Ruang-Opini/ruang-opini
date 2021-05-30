package id.ruangopini.utils

import id.ruangopini.R
import id.ruangopini.data.model.Intro

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

    val listIntro = listOf(
        Intro(
            R.drawable.ic_intro_1,
            "Sentimen Analisis",
            "Dapatkan data jumlah pro, kontra, dan buzzer yang akurat tentang kebijakan yang sedang populer dibicarakan"
        ),
        Intro(
            R.drawable.ic_intro_2,
            "Ruang Diskusi",
            "Buat atau bergabung  di Ruang Diskusi dengan memberikan komentar, upvote, dan downvote"
        ),
        Intro(
            R.drawable.ic_intro_3,
            "Referensi",
            "Kesulitan mencari referensi yang terpercaya? Lihat referensi yang kami sediakan di halaman referensi"
        ),
        Intro(
            R.drawable.ic_intro_4,
            "Saatnya mencoba",
            "Dapatkan kesempatan meninjau ulang kebijakan yang dibuat pemerintah bersama jaringan sosial dalam media Ruang Opini. Tak perlu takut untuk mengemukakan pendapat karena kami menyediakan referensi yang membuat pendapat anda kuat"
        )
    )
}