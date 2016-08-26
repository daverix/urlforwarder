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
package net.daverix.urlforward;

import android.support.test.InstrumentationRegistry;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class Actions {
    public static void clickAddFilter() {
        onView(withId(R.id.btnAddFilter))
                .perform(click());
    }

    public static void setFilterData(String filterName, String filter, String replaceableText) {
        onView(withId(R.id.editTitle))
                .perform(clearText())
                .perform(typeText(filterName));
        onView(withId(R.id.editFilter))
                .perform(clearText())
                .perform(typeText(filter));
        onView(withId(R.id.editReplacableText))
                .perform(clearText())
                .perform(typeText(replaceableText));
    }

    public static void clickEncodeCheckbox() {
        onView(withId(R.id.checkEncode))
                .perform(click());
    }

    public static void saveUsingIdlingResource(UrlForwarderApplication application) {
        ModifyFilterIdlingResource saveResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        application.setModifyFilterIdlingResource(saveResource);

        onView(withId(R.id.menuSave))
                .perform(click());

        registerIdlingResources(saveResource);
    }

    public static void deleteUsingIdlingResource(UrlForwarderApplication application) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        ModifyFilterIdlingResource deleteResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        application.setModifyFilterIdlingResource(deleteResource);

        onView(withText(R.string.delete)).perform(click());

        registerIdlingResources(deleteResource);
    }

    public static void clickOnFilterInList(String filterName) {
        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .perform(actionOnItem(hasDescendant(withText(filterName)), click()));
    }
}
