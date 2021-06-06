package id.ruangopini.utils

import id.ruangopini.R
import id.ruangopini.data.model.AboutUs
import id.ruangopini.data.model.Intro
import id.ruangopini.data.model.ItemHelp

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
            "Dapatkan kesempatan meninjau ulang kebijakan yang dibuat pemerintah bersama jaringan sosial dalam media Ruang Opini. Tak perlu takut untuk mengemukakan pendapat karena kami menyediakan referensi yang membuat pendapat Anda kuat"
        )
    )

    fun getTextBuzzer(percentage: Int) = when (percentage) {
        in 0..33 -> "Trending ini aman dari Buzzer"
        in 34..66 -> "Trending ini rentan dari Buzzer"
        in 67..100 -> "Trending ini dipadati oleh Buzzer"
        else -> "Trending ini aman dari Buzzer"
    }

    fun getColorBuzzer(percentBuzzer: Int) = when (percentBuzzer) {
        in 0..33 -> R.color.info_safe
        in 34..66 -> R.color.primary
        in 67..100 -> R.color.info_warning
        else -> R.color.info_safe
    }

    fun getTextColorBuzzer(color: Int) =
        if (color == R.color.primary) R.color.black else R.color.white

    val dataAboutUs = listOf(
        AboutUs(
            "Ruang Opini",
            "Layanan yang dibuat untuk memberikan informasi sentimen analisis kebijakan yang sedang populer di Twitter. Informasi ini berupa data statitik pro dan kontra serta tingkat buzzer yang dikumpulkan dari Twitter menggunakan teknologi mechine learning. Selain itu, kami menyediakan ruang diskusi untuk berdiskusi berdasarkan informasi kebijakan yang telah didapatkan. Jadi, Anda bisa membuat ruang diskusi setelah melihat hasil data sentimen analisis."
        ),
        AboutUs(
            "Apa yang Anda dapatkan?",
            "Kami memberikan dataset kebijakan yang dapat Anda lihat di halaman utama dan sumber terpercaya mengenai kebijakan yang sedang Anda diskusikan. "
        ),
        AboutUs(
            "Apa yang kami kumpulkan?",
            "Kami mengumpulkan konten dan informasi lainnya yang Anda berikan ketika menggunakan produk kami. Termasuk informasi ketika registrasi, membuat ruang diskusi, dan aktivitas Anda selama diskusi."
        ),
        AboutUs(
            "Permintaan perangkat",
            "Kami menyarankan Anda menggunakan perangkat pintar dengan spesifikasi minimal Android 5.0 (Lollipop) dan jaringan koneksi internet yang stabil untuk mendapatkan pengalaman terbaik saat menggunakan aplikasi Ruang Opini."
        ),
        AboutUs(
            "Siapa saja yang menggunakan produk ini?",
            "Semua orang dapat menggunakan layanan kami. Akan tetapi, kami juga menginginkan agar pengguna merasa aman, nyaman, dan mematuhi hukum. Oleh karena itu, kami meminta Anda untuk mematuhi beberapa hal berikut:<br><br>" +
                    "<ol><li> Anda tidak boleh menggunakan nama samaran dan menyamar sebagai orang lain.</li>" +
                    "<li> Menjaga etika dalam berpendapat dan berkomunikasi sehingga tidak melanggar hukum yang berlaku dan merugikan pengguna lain.</li>" +
                    "<li> Tidak diperbolehkan menyerang sistem aplikasi dengan cara apapun.</li></ol>"
        ),
    )

    val dataHelpManageAccount = listOf(
        ItemHelp(
            "Bagaimana cara mengganti foto profil dan latar belakang?",
            "Masuk ke halaman profil dengan mengetuk [icon-profile] di bagian kanan bawah layar Anda. Kemudian ketuk [icon-edit] untuk mengunggah foto dari galeri Anda.",
            listOf("[icon-profile]", "[icon-edit]")
        ),
        ItemHelp(
            "Bagaimana cara mengganti nama, dan bio?",
            "Masuk ke halaman profil dengan mengetuk [icon-profile] di bagian kanan bawah layar Anda. Kemudian ketuk [icon-edit], lalu masukan Nama dan Bio Anda yang baru. Ketuk “Simpan” untuk menyimpan perubahan yang Anda buat.",
            listOf("[icon-profile]", "[icon-edit]")
        )
    )
    val dataHelpDiscussionRoom = listOf(
        ItemHelp(
            "Bagaimana cara melihat informasi sentimen analisis?",
            "Ketuk [icon-home] untuk melihat kebijakan yang sedang populer. Kemudian ketuk pada judul kebijakan. Halaman tersebut memuat informasi berikut: <br>" +
                    "<ol><li>Judul dan deskripsi kebijakan yang ingin Anda lihat</li>" +
                    "<li>Pro dan kontra menampilkan jumlah twit yang pro dan kontra dengan kebijakan yang ingin Anda lihat.</li>" +
                    "<li>Tingkat buzzer yang terlibat dalam postingan di twitter terkait kebijakan yang ingin Anda lihat.</li>" +
                    "<li>Banyaknya ruang diskusi yang dibuat dari kebijakan yang Anda lihat.</li>" +
                    "<li>Banyaknya pengguna yang terlibat di dalam ruang diskusi.</li></ol>",
            listOf("[icon-home]")
        ),
        ItemHelp(
            "Saya tertarik membuat Ruang Disukusi untuk topik yang baru saja saya lihat. Bagaimana cara membuatnya?",
            "Ketik [icon-add] untuk membuat Ruang Diskusi. Isi kolom “Nama Ruang Diskusi” dan “Deskripsi”. Kemudian ketuk “Tambahkan”. Ruang yang Anda buat akan muncul di halaman kebijakan yang Anda lihat.",
            listOf("[icon-add]")
        ),
        ItemHelp(
            "Melihat Ruang Diskusi",
            "Ketuk nama ruang yang ingin Anda lihat. Anda akan melihat beberapa topik diskusi di ruang tersebut yang dipost oleh pengguna.",
            listOf()
        ),
        ItemHelp(
            "Mengunggah topik diskusi di dalam Ruang Diskusi",
            "Ketuk nama ruang yang ingin Anda lihat. Ketuk [icon-join] untuk bergabung dengan ruang diskusi. Kemudia ketuk [icon-add] untuk mengunggah topik diskusi. untuk bergabung dengan ruang diskusi. Anda juga dapat menambahkan foto dan referensi di dalam postingan tersebut.",
            listOf("[icon-join]", "[icon-add]")
        ),
        ItemHelp(
            "Bergabung dengan diskusi yang sudah ada",
            "Ketuk nama ruang yang ingin Anda lihat. Kemudian pilih topik diskusi yang ingin Anda ikuti. Anda dapat mengelurakan opini Anda terkait topik diskusi dengan memberikan komentar. Selain itu, Anda juga dapat memberikan upvote dan downvote untuk diskusi yang Anda ikuti.",
            listOf()
        )
    )

    val dataHelpReference = listOf(
        ItemHelp(
            "Bagaimana saya melihat referensi tentang kebijakan yang ingin saya diskusikan?",
            "Ketuk [icon-reference] kemudian pilih referensi apa yang ingin Anda lihat.",
            listOf("[icon-reference]")
        )
    )
    val dataHelpReport = listOf(
        ItemHelp(
            "Bagaimana saya menghubungi developer aplikasi ini?",
            "Segala aduan pengguna terkait aplikasi Ruang Opini dapat dikirim melalui surel kami di :<br>" +
                    "ruangopini21@gmail.com",
            listOf()
        )
    )

    val listIcon = mapOf(
        "[icon-profile]" to R.drawable.icon_profile,
        "[icon-edit]" to R.drawable.icon_edit,
        "[icon-home]" to R.drawable.icon_home,
        "[icon-add]" to R.drawable.icon_add,
        "[icon-join]" to R.drawable.ic_icon_join,
        "[icon-reference]" to R.drawable.icon_reference
    )
}