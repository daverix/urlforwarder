/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import net.daverix.urlforward.ui.LinkDialogScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LinkDialogScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickOnFilter() {
        val filter1 = LinkFilter(
            name = "filter 1",
            replaceText = "@url1",
            replaceSubject = "@subject1",
            filterUrl = "http://test.test/?url=@url1&subject=@subject1",
            created = 0,
            updated = 1,
            encoded = true
        )

        val filter2 = LinkFilter(
            name = "filter 2",
            replaceText = "@url2",
            replaceSubject = "@subject2",
            filterUrl = "http://test2.test/?url=@url2&subject=@subject2",
            created = 0,
            updated = 1,
            encoded = true
        )
        val mutableState = MutableStateFlow<DialogState>(DialogState.Loading)
        var clickedFilter: LinkFilter? = null

        composeTestRule.setContent {
            val state by mutableState.collectAsState()
            LinkDialogScreen(
                state = state,
                onItemClick = { filter ->
                    clickedFilter = filter
                }
            )
        }

        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()

        mutableState.value = DialogState.Filters(
            filters = listOf(filter1, filter2)
        )

        composeTestRule.onNodeWithText("filter 2").performClick()

        assertEquals(filter2, clickedFilter)
    }
}
