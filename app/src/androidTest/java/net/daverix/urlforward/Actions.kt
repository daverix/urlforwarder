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

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*

fun clickAddFilter() {
    onView(withId(R.id.btnAddFilter)).perform(click())
}

fun setFilterData(filterName: String,
                  filter: String,
                  replaceableText: String,
                  replaceableSubject: String) {
    onView(withId(R.id.editTitle))
            .perform(clearText(), typeText(filterName), closeSoftKeyboard())
    onView(withId(R.id.editFilter))
            .perform(clearText(), typeText(filter), closeSoftKeyboard())
    onView(withId(R.id.editReplaceableText))
            .perform(clearText(), typeText(replaceableText), closeSoftKeyboard())
    onView(withId(R.id.editReplaceableSubject))
            .perform(clearText(), typeText(replaceableSubject), closeSoftKeyboard())
}

fun clickFilter(filterName: String) {
    onView(withId(R.id.filters))
            .check(matches(isDisplayed()))
            .perform(scrollTo<FiltersAdapter.ViewHolder>(hasDescendant(withText(filterName))))
            .perform(actionOnItem<FiltersAdapter.ViewHolder>(hasDescendant(withText(filterName)), click()))
}

fun clickLink(filterName: String) {
    onView(withId(R.id.links))
            .check(matches(isDisplayed()))
            .perform(scrollTo<LinksAdapter.ViewHolder>(hasDescendant(withText(filterName))))
            .perform(actionOnItem<LinksAdapter.ViewHolder>(hasDescendant(withText(filterName)), click()))
}

fun save() {
    onView(withId(R.id.menuSave)).perform(click())
}

fun delete() {
    openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
    onView(withText(R.string.delete)).perform(click())
}
