package net.daverix.urlforward

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class UrlCreationTest(
    private val filterUrl: String,
    private val replaceText: String,
    private val replaceSubject: String,
    private val encoded: Boolean,
    private val url: String,
    private val subject: String,
    private val regexPattern: String,
    private val expected: String
) {
    @Test
    fun createExpectedUrl() {
        val filter = LinkFilter(
            filterUrl = filterUrl,
            replaceText = replaceText,
            replaceSubject = replaceSubject,
            encoded = encoded,
            name = "some filter",
            created = 0,
            updated = 1,
            regexPattern = regexPattern
        )

        val actual = createUrl(filter, url, subject)

        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "filterUrl={0}, replaceText={1}, replaceSubject={2}, encoded={3}, url={4}, subject={5}, regex_pattern={6}, expected={6}")
        fun provideData(): List<Array<Any>> = listOf(
            arrayOf(
                "https://wikipedia.com/\\1",
                "",
                "",
                false,
                "https://example.com/companyname/wow",
                "doesnotmatter",
                "https://example.com/([^/]*).*",
                "https://wikipedia.com/companyname",
            ),
            arrayOf(
                "https://example.com/submit?url=@url",
                "@url",
                "",
                true,
                "https://someurl.com",
                "something",
                "",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com"
            ),
            arrayOf(
                "https://example.com/submit?url=@url",
                "@url",
                "@subject",
                true,
                "https://someurl.com",
                "something",
                "",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com"
            ),
            arrayOf(
                "https://example.com/submit?url=@url&subject=@subject",
                "@url",
                "@subject",
                true,
                "https://someurl.com",
                "",
                "",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com&subject="
            ),
            arrayOf(
                "https://example.com/submit?url=@url&subject=@subject",
                "@url",
                "@subject",
                true,
                "https://someurl.com",
                "something",
                "",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com&subject=something"
            ),
            arrayOf(
                "https://example.com/stuff/@url",
                "@url",
                "",
                true,
                "bananas",
                "something",
                "",
                "https://example.com/stuff/bananas"
            ),
        )
    }
}
