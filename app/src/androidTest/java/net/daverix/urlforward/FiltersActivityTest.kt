/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2017 David Laurell

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

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import net.daverix.urlforward.databinding.FilterRowBinding
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class FiltersActivityTest {
    @get:Rule
    var testRule = ActivityTestRule(FiltersActivity::class.java)

    @Before
    fun registerIdlingCounters() {
        testRule.resetIdling { loadIdleCounter }
        testRule.resetIdling { modifyIdleCounter }
    }

    @Test
    fun shouldAddAndRemoveFilter() {
        val filterName = "MyFilter-" + UUID.randomUUID()

        clickAddFilter()

        setFilterData(filterName, "http://daverix.net/test.php?url=@uri&subject=@subject", "@uri", "@subject")
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName)
        delete()

        checkFilterNameNotInList(filterName)
    }

    @Test
    fun shouldAddDefaultAndVerifyDataIsCorrect() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "http://daverix.net/test.php?url=@uri1&subject=@subject1"
        val replaceableText = "@uri1"
        val replaceableSubject = "@subject1"

        clickAddFilter()

        setFilterData(filterName, filter, replaceableText, replaceableSubject)
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName)
        verifyEditTextFieldsMatch(filterName, filter, replaceableText, replaceableSubject)
        onView(withId(R.id.checkEncode)).check(matches(isChecked()))
    }

    @Test
    fun shouldAddAndUpdateDefaultAndVerifyDataIsCorrect() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "http://daverix.net/test.php?url=@uri12&subject=@subject12"
        val replaceableText = "@uri12"
        val replaceableSubject = "@subject12"

        val filterName2 = "MyFilter-" + UUID.randomUUID()
        val filter2 = "http://daverix.net/test.php?url=@uri13&subject=@subject13"
        val replaceableText2 = "@uri13"
        val replaceableSubject2 = "@subject13"

        clickAddFilter()
        setFilterData(filterName, filter, replaceableText, replaceableSubject)
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName)
        setFilterData(filterName2, filter2, replaceableText2, replaceableSubject2)
        closeSoftKeyboard()
        clickEncodeCheckbox()
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName2)
        verifyEditTextFieldsMatch(filterName2, filter2, replaceableText2, replaceableSubject2)
        onView(withId(R.id.checkEncode)).check(matches(isNotChecked()))
    }

    @Test
    fun uncheckedEncodeIsPersisted() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "http://daverix.net/test/@uri2&subject=@subject2"
        val replaceableText = "@uri2"
        val replaceableSubject = "@subject2"

        clickAddFilter()
        setFilterData(filterName, filter, replaceableText, replaceableSubject)
        closeSoftKeyboard()
        clickEncodeCheckbox()
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName)
        onView(withId(R.id.checkEncode)).check(matches(isNotChecked()))
    }

    @Test
    fun editEncodeIsPersisted() {
        val filterName = "MyFilter-" + UUID.randomUUID()
        val filter = "http://daverix.net/test.php?url=@uri12&subject=@subject12"
        val replaceableText = "@uri12"
        val replaceableSubject = "@subject12"

        val filterName2 = "MyFilter-" + UUID.randomUUID()
        val filter2 = "http://daverix.net/test.php?url=@uri13&subject=@subject13"
        val replaceableText2 = "@uri13"
        val replaceableSubject2 = "@subject13"

        clickAddFilter()

        setFilterData(filterName, filter, replaceableText, replaceableSubject)
        closeSoftKeyboard()
        clickEncodeCheckbox()
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName)

        setFilterData(filterName2, filter2, replaceableText2, replaceableSubject2)
        closeSoftKeyboard()
        clickEncodeCheckbox()
        save()

        clickOnRecyclerViewWithName<FilterRowBinding>(R.id.filters, filterName2)
        onView(withId(R.id.checkEncode)).check(matches(isChecked()))
    }

    private fun verifyEditTextFieldsMatch(filterName: String,
                                          filter: String,
                                          replaceableText: String,
                                          replaceableSubject: String) {
        onView(withId(R.id.editTitle)).check(matches(withText(filterName)))
        onView(withId(R.id.editFilter)).check(matches(withText(filter)))
        onView(withId(R.id.editReplaceableText)).check(matches(withText(replaceableText)))
        onView(withId(R.id.editReplaceableSubject)).check(matches(withText(replaceableSubject)))
    }

    private fun checkFilterNameNotInList(filterName: String) {
        onView(withId(R.id.filters))
                .check(matches(isDisplayed()))
                .check(matches(not(hasDescendant(withText(filterName)))))
    }
}
