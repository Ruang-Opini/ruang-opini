package id.ruangopini

import org.junit.Test

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
}