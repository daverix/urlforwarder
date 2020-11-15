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

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import java.util.*

fun clickAddFilter() {
    onView(withId(R.id.btnAddFilter))
            .perform(click())
}

fun setFilterData(
        filterName: String,
        filter: String,
        replaceableText: String,
        replaceableSubject: String
) {
    onView(withId(R.id.editTitle))
            .perform(clearText())
            .perform(typeText(filterName), closeSoftKeyboard())
    onView(withId(R.id.editFilter))
            .perform(clearText())
            .perform(typeText(filter), closeSoftKeyboard())
    onView(withId(R.id.editReplaceableText))
            .perform(clearText())
            .perform(typeText(replaceableText), closeSoftKeyboard())
    onView(withId(R.id.editReplaceableSubject))
            .perform(clearText())
            .perform(typeText(replaceableSubject), closeSoftKeyboard())
}

fun clickEncodeCheckbox() {
    onView(withId(R.id.checkEncode))
            .perform(click())
}

fun <T : Activity> ActivityScenario<T>.saveUsingIdlingResource() {
    val saveResource = ModifyFilterIdlingResource(UUID.randomUUID().toString())
    onActivity {
        (it.application as UrlForwarderApplication).run {
            modifyFilterIdlingResource = saveResource
        }
    }
    onView(withId(R.id.menuSave)).perform(click())
    IdlingRegistry.getInstance().register(saveResource)
}

fun <T : Activity> ActivityScenario<T>.deleteUsingIdlingResource() {
    Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
    val deleteResource = ModifyFilterIdlingResource(UUID.randomUUID().toString())
    onActivity {
        (it.application as UrlForwarderApplication).run {
            modifyFilterIdlingResource = deleteResource
        }
    }
    onView(withText(R.string.delete)).perform(click())
    IdlingRegistry.getInstance().register(deleteResource)
}

fun clickOnFilterInList(filterName: String) {
    onView(withId(R.id.list))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(filterName)), click()))
}
