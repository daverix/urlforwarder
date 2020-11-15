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

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.UUID;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class Actions {
    public static void clickAddFilter() {
        onView(withId(R.id.btnAddFilter))
                .perform(click());
    }

    public static void setFilterData(String filterName,
                                     String filter,
                                     String replaceableText,
                                     String replaceableSubject) {
        onView(withId(R.id.editTitle))
                .perform(clearText())
                .perform(typeText(filterName), closeSoftKeyboard());
        onView(withId(R.id.editFilter))
                .perform(clearText())
                .perform(typeText(filter), closeSoftKeyboard());
        onView(withId(R.id.editReplaceableText))
                .perform(clearText())
                .perform(typeText(replaceableText), closeSoftKeyboard());
        onView(withId(R.id.editReplaceableSubject))
                .perform(clearText())
                .perform(typeText(replaceableSubject), closeSoftKeyboard());
    }

    public static void clickEncodeCheckbox() {
        onView(withId(R.id.checkEncode))
                .perform(click());
    }

    public static <T extends Activity> void saveUsingIdlingResource(ActivityScenario<T> scenario) {
        final ModifyFilterIdlingResource saveResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        scenario.onActivity(new ActivityScenario.ActivityAction<T>() {
            @Override
            public void perform(T activity) {
                ((UrlForwarderApplication) activity.getApplication()).setModifyFilterIdlingResource(saveResource);
            }
        });
        onView(withId(R.id.menuSave)).perform(click());

        IdlingRegistry.getInstance().register(saveResource);
    }

    public static <T extends Activity> void deleteUsingIdlingResource(ActivityScenario<T> scenario) {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        final ModifyFilterIdlingResource deleteResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        scenario.onActivity(new ActivityScenario.ActivityAction<T>() {
            @Override
            public void perform(T activity) {
                ((UrlForwarderApplication) activity.getApplication()).setModifyFilterIdlingResource(deleteResource);
            }
        });
        onView(withText(R.string.delete)).perform(click());

        IdlingRegistry.getInstance().register(deleteResource);
    }

    public static void clickOnFilterInList(String filterName) {
        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .perform(actionOnItem(hasDescendant(withText(filterName)), click()));
    }
}
