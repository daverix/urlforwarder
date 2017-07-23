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

import android.app.Activity
import android.databinding.ViewDataBinding
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import java.util.*

fun clickAddFilter() {
    onView(withId(R.id.btnAddFilter)).perform(click())
}

fun setFilterData(filterName: String,
                  filter: String,
                  replaceableText: String,
                  replaceableSubject: String) {
    onView(withId(R.id.editTitle))
            .perform(clearText())
            .perform(typeText(filterName))
    onView(withId(R.id.editFilter))
            .perform(clearText())
            .perform(typeText(filter))
    onView(withId(R.id.editReplaceableText))
            .perform(clearText())
            .perform(typeText(replaceableText))
    onView(withId(R.id.editReplaceableSubject))
            .perform(clearText())
            .perform(typeText(replaceableSubject))
}

fun clickEncodeCheckbox() {
    onView(withId(R.id.checkEncode)).perform(click())
}

fun <T : ViewDataBinding> clickOnRecyclerViewWithName(viewId: Int, filterName: String) {
    onView(withId(viewId))
            .check(matches(isDisplayed()))
            .perform(actionOnItem<SimpleBindingAdapter.BindingHolder<T>>(hasDescendant(withText(filterName)), click()))
}

fun save() {
    onView(withId(R.id.menuSave)).perform(click())
}

fun delete() {
    Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
    onView(withText(R.string.delete)).perform(click())
}

fun <T : Activity?> ActivityTestRule<T>.resetIdling(block: UrlForwarderApplication.() -> ProxyIdleCounter) {
    val idlingResource = CountingIdlingResource(UUID.randomUUID().toString())
    block(getApp()).idleResource = IdleCounterWrapper(idlingResource)
    Espresso.registerIdlingResources(idlingResource)
}

private fun <T : Activity?> ActivityTestRule<T>.getApp(): UrlForwarderApplication {
    val app = activity?.application as UrlForwarderApplication?
    if(app == null)
        throw IllegalStateException("Could not get application from test rule!")

    return app
}
