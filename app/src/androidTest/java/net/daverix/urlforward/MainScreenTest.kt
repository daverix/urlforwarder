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

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.daverix.urlforward.ui.TAG_ENCODE_URL
import net.daverix.urlforward.ui.TAG_FILTER_NAME
import net.daverix.urlforward.ui.TAG_FILTER_URL
import net.daverix.urlforward.ui.TAG_REPLACEABLE_SUBJECT
import net.daverix.urlforward.ui.TAG_REPLACEABLE_TEXT
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@HiltAndroidTest
class MainScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<FiltersActivity>()

    @Test
    fun shouldAddAndRemoveFilter() {
        val filterName = "MyFilter-" + UUID.randomUUID()

        composeTestRule.run {
            // Main screen
            onCreateFilterButton().performClick()

            // Create filter screen
            onNodeWithTag(TAG_FILTER_NAME).performTextInput(filterName)
            onSaveButton().performClick()

            // Main screen
            onNodeWithText(filterName).performClick()

            // Edit filter screen
            onDeleteButton()
                .performScrollTo()
                .performClick()

            onDialogButtonWithText("Delete").performClick()

            // Main screen
            onNodeWithText(filterName).assertDoesNotExist()
        }
    }

    private fun ComposeContentTestRule.onDialogButtonWithText(text: String) =
        onNode(
            hasAnyAncestor(isDialog())
                .and(hasClickAction())
                .and(hasText(text))
        )

    @Test
    fun shouldAddDefaultAndVerifyDataIsCorrect() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "https://daverix.net/test.php?url=@uri1&subject=@subject1"
        val replaceableText = "@uri1"
        val replaceableSubject = "@subject1"

        composeTestRule.run {
            onCreateFilterButton().performClick()

            performFilterInputs(filterName, filter, replaceableText, replaceableSubject)
            onSaveButton().performClick()

            onNodeWithText(filterName).performClick()

            onNodeWithTag(TAG_FILTER_NAME).assert(hasText(filterName))
            onNodeWithTag(TAG_FILTER_URL).assert(hasText(filter))
            onNodeWithTag(TAG_REPLACEABLE_TEXT).assert(hasText(replaceableText))
            onNodeWithTag(TAG_REPLACEABLE_SUBJECT).assert(hasText(replaceableSubject))
            onNodeWithTag(TAG_ENCODE_URL).assertIsChecked()
        }
    }

    @Test
    fun shouldAddAndUpdateDefaultAndVerifyDataIsCorrect() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "https://daverix.net/test.php?url=@uri12&subject=@subject12"
        val replaceableText = "@uri12"
        val replaceableSubject = "@subject12"

        val filterName2 = "MyFilter-" + UUID.randomUUID()
        val filter2 = "https://daverix.net/test.php?url=@uri13&subject=@subject13"
        val replaceableText2 = "@uri13"
        val replaceableSubject2 = "@subject13"

        composeTestRule.run {
            onCreateFilterButton().performClick()

            performFilterInputs(filterName, filter, replaceableText, replaceableSubject)
            onSaveButton().performClick()

            onNodeWithText(filterName).performClick()

            performFilterInputs(filterName2, filter2, replaceableText2, replaceableSubject2)
            onSaveButton().performClick()

            onNodeWithText(filterName2).performClick()

            onNodeWithTag(TAG_FILTER_NAME).assert(hasText(filterName2))
            onNodeWithTag(TAG_FILTER_URL).assert(hasText(filter2))
            onNodeWithTag(TAG_REPLACEABLE_TEXT).assert(hasText(replaceableText2))
            onNodeWithTag(TAG_REPLACEABLE_SUBJECT).assert(hasText(replaceableSubject2))
            onNodeWithTag(TAG_ENCODE_URL).assertIsChecked()
        }
    }

    @Test
    fun uncheckedEncodeIsPersisted() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "https://daverix.net/test/@uri2&subject=@subject2"
        val replaceableText = "@uri2"
        val replaceableSubject = "@subject2"

        composeTestRule.run {
            onCreateFilterButton().performClick()

            performFilterInputs(
                filterName = filterName,
                filter = filter,
                replaceableText = replaceableText,
                replaceableSubject = replaceableSubject
            )
            onNodeWithTag(TAG_ENCODE_URL).performClick()
            onSaveButton().performClick()

            onNodeWithText(filterName).performClick()

            onNodeWithTag(TAG_ENCODE_URL).assertIsNotChecked()
        }
    }

    @Test
    fun editEncodeIsPersisted() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filterUrl = "https://daverix.net/test.php?url=@uri12&subject=@subject12"
        val replaceableText = "@uri12"
        val replaceableSubject = "@subject12"
        val filterName2 = "MyFilter-" + UUID.randomUUID()
        val filterUrl2 = "https://daverix.net/test.php?url=@uri13&subject=@subject13"
        val replaceableText2 = "@uri13"
        val replaceableSubject2 = "@subject13"

        composeTestRule.run {
            onCreateFilterButton().performClick()
            performFilterInputs(
                filterName = filterName,
                filter = filterUrl,
                replaceableText = replaceableText,
                replaceableSubject = replaceableSubject
            )
            onNodeWithTag(TAG_ENCODE_URL).performClick()
            onSaveButton().performClick()

            onNodeWithText(filterName).performClick()
            performFilterInputs(
                filterName = filterName2,
                filter = filterUrl2,
                replaceableText = replaceableText2,
                replaceableSubject = replaceableSubject2
            )
            onNodeWithTag(TAG_ENCODE_URL).performClick()
            onSaveButton().performClick()

            onNodeWithText(filterName2).performClick()
            onNodeWithTag(TAG_ENCODE_URL).assertIsChecked()
        }
    }

    private fun ComposeContentTestRule.performFilterInputs(
        filterName: String,
        filter: String,
        replaceableText: String,
        replaceableSubject: String
    ) {
        onNodeWithTag(TAG_FILTER_NAME).performTextClearanceAndInput(filterName)
        onNodeWithTag(TAG_FILTER_URL).performTextClearanceAndInput(filter)
        onNodeWithTag(TAG_REPLACEABLE_TEXT).performTextClearanceAndInput(replaceableText)
        onNodeWithTag(TAG_REPLACEABLE_SUBJECT).performTextClearanceAndInput(replaceableSubject)
    }

    private fun SemanticsNodeInteraction.performTextClearanceAndInput(filterName: String) {
        performTextClearance()
        performTextInput(filterName)
    }

    private fun SemanticsNodeInteractionsProvider.onCreateFilterButton() =
        onNodeWithContentDescription("Create filter")

    private fun SemanticsNodeInteractionsProvider.onSaveButton() = onNodeWithText("Save")

    private fun SemanticsNodeInteractionsProvider.onDeleteButton() = onNodeWithText("Delete")

    private fun SemanticsNodeInteraction.assertIsChecked() = assert(isToggleable().and(isOn()))

    private fun SemanticsNodeInteraction.assertIsNotChecked() = assert(isToggleable().and(isOff()))
}