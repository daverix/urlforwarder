package net.daverix.urlforward

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class UrlCreationTest {
    @ParameterizedTest
    @MethodSource("provideData")
    fun createExpectedUrl(
        filterUrl: String,
        replaceText: String,
        replaceSubject: String,
        encoded: Boolean,
        url: String,
        subject: String,
        expected: String
    ) {
        val filter = LinkFilter(
            filterUrl = filterUrl,
            replaceText = replaceText,
            replaceSubject = replaceSubject,
            encoded = encoded,
            name = "some filter",
            created = 0,
            updated = 1,
            textPattern = ".*",
            subjectPattern = ".*"
        )

        val actual = createUrl(filter, url, subject)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun verifyForwardedUrlWithTextPattern() {
        val filter = LinkFilter(
            filterUrl = "https://myconsumer.site/@url1",
            replaceText = "@url",
            replaceSubject = "@subject",
            encoded = true,
            name = "some filter",
            created = 0,
            updated = 1,
            textPattern = "https://example.com/([a-z]+)",
            subjectPattern = ".*"
        )

        val actual = createUrl(filter, "https://example.com/abc", null)

        assertThat(actual).isEqualTo("https://myconsumer.site/abc")
    }

    @Test
    fun verifyForwardedUrlWithSubjectPattern() {
        val filter = LinkFilter(
            filterUrl = "https://myconsumer.site/share?url=@url&code=@subject1",
            replaceText = "@url",
            replaceSubject = "@subject",
            encoded = true,
            name = "some filter",
            created = 0,
            updated = 1,
            textPattern = "https://example.com/([a-z]+)",
            subjectPattern = ".*\\s([a-z0-9]+)!"
        )

        val actual = createUrl(filter, "https://example.com/abc", "Thank you, your code is def123!")

        assertThat(actual).isEqualTo("https://myconsumer.site/share?url=https%3A%2F%2Fexample.com%2Fabc&code=def123")
    }

    companion object {
        @JvmStatic
        fun provideData(): List<Array<Any>> = listOf(
            arrayOf(
                "https://example.com/submit?url=@url",
                "@url",
                "",
                true,
                "https://someurl.com",
                "something",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com"
            ),
            arrayOf(
                "https://example.com/submit?url=@url",
                "@url",
                "@subject",
                true,
                "https://someurl.com",
                "something",
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com"
            ),
            arrayOf(
                "https://example.com/submit?url=@url&subject=@subject",
                "@url",
                "@subject",
                true,
                "https://someurl.com",
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
                "https://example.com/submit?url=https%3A%2F%2Fsomeurl.com&subject=something"
            ),
            arrayOf(
                "https://example.com/stuff/@url",
                "@url",
                "",
                true,
                "bananas",
                "something",
                "https://example.com/stuff/bananas"
            ),
        )
    }
}
