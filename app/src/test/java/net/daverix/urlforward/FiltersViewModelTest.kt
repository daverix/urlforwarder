package net.daverix.urlforward

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Correspondence.from
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


@ExperimentalCoroutinesApi
class FiltersViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var linkFilterDao: LinkFilterDao

    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var sut: FiltersViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun showLoading() = runBlockingTest(testDispatcher) {
        val closeChannel = Channel<Unit>()
        every { linkFilterDao.getFilters() } returns flow {
            closeChannel.receive()
        }

        sut = FiltersViewModel(linkFilterDao)
        sut.loading.observeForever { }

        assertThat(sut.loading.value).isTrue()

        closeChannel.cancel()
    }

    @Test
    fun showItem() = runBlockingTest {
        every { linkFilterDao.getFilters() } returns flow {
            listOf(LinkFilter(
                    id = 1,
                    title = "Test filter",
                    filterUrl = "http://example.com?something=\$abc",
                    replaceText = "\$abc",
                    replaceSubject = "",
                    created = Date(0L),
                    updated = Date(0L),
                    skipEncode = false
            ))
        }

        sut = FiltersViewModel(linkFilterDao)
        sut.loading.observeForever { }
        sut.filters.observeForever { }

        testDispatcher.advanceUntilIdle()

        assertThat(sut.loading.value).isFalse()
        assertThat(sut.filters.value)
                .comparingElementsUsing(theTitle())
                .containsExactly("Test filter")
    }

    private fun theTitle() = from<FilterRowViewModel, String>(
            { actual, expected ->
                actual?.title == expected
            }
            , "title")
}