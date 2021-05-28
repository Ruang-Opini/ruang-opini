package id.ruangopini

import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private data class Comment(
        val comment: String? = null,
        val discussionId: String? = null,
        val postId: String? = null
    )

    @Test
    fun addition_isCorrect() {
        val comments = listOf(
            Comment("Waahhh Keren Banget", "AA", "aa"),
            Comment("Anajay jiwaa", "BB", "bb"),
            Comment("Loh koh bisa gitu", "AA", "aa"),
            Comment("Aneh aja masa", "CC", "cc"),
            Comment("Lagian ngapain coba bikin begituan", "AA", "aa"),
            Comment("Yaampun", "AA", "aa"),
            Comment("Wis angel angel", "CC", "cc"),
            Comment("Ga ngerti lagi dah", "AA", "aa"),
            Comment("Wahhh asik nih", "BB", "bb"),
            Comment("Siap boss", "BB", "bb"),
            Comment("Kawal terusss", "AA", "aa"),
            Comment("Gaskeun broo", "AA", "aa"),
        ).groupBy { it.discussionId }.forEach { (t, u) ->
            println("t: $t, size = ${u.size}")
            println("u: $u")
        }
    }

    @Test
    fun getUrl() {
        val base = "https://peraturan.go.id/"
        val source =
            "https://peraturan.go.id/peraturan/jenis.html?id=11e6c11afeb497d8bcdf313635333237"
        val result = source.subSequence(base.length, source.length)
        println(result)
    }

    @Test
    fun getPdfName() {
        val name = "https://peraturan.go.id/common/dokumen/ln/2020/uu13-2020bt.pdf"
        println(name.split("/").last())
    }

    @Test
    fun toTitleCase() {
        val name =
            "PENETAPAN PERATURAN PEMERINTAH PENGGANTI UNDANG-UNDANG NOMOR 1 TAHUN 2020 TENTANG KEBIJAKAN KEUANGAN NEGARA DAN STABILITAS SISTEM KEUANGAN UNTUK PENANGANAN PANDEMI CORONA VIRUS DISEASE 2019 (COVID-19) DAN/ATAU DALAM RANGKA MENGHADAPI ANCAMAN YANG MEMBAHAYAKAN PEREKONOMIAN NASIONAL DAN/ATAU STABILITAS SISTEM KEUANGAN MENJADI UNDANG-UNDANG"
        val result = buildString {
            name.split(" ").forEach {
                append(it.lowercase().replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                }).append(" ")
            }
        }.trim()
        println(result)
    }
}