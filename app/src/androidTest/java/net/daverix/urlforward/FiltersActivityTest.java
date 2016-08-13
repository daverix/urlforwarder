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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.daverix.urlforward.Actions.addFilter;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class FiltersActivityTest {
    @Rule
    public ActivityTestRule<FiltersActivity> testRule = new ActivityTestRule<>(FiltersActivity.class);

    @Test
    public void shouldAddAndRemoveFilter() {
        String filterName = "MyFilter-" + UUID.randomUUID();

        ModifyFilterIdlingResource saveResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        getApplication().setModifyFilterIdlingResource(saveResource);

        addFilter(filterName, "http://daverix.net/test.php?url=@uri", "@uri");

        registerIdlingResources(saveResource);

        clickOnFilterInList(filterName);

        ModifyFilterIdlingResource deleteResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        getApplication().setModifyFilterIdlingResource(deleteResource);

        deleteFromOptionsMenu();

        registerIdlingResources(deleteResource);

        checkFilterNameNotInList(filterName);
    }

    private void checkFilterNameNotInList(String filterName) {
        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .check(matches(not(hasDescendant(withText(filterName)))));
    }

    private void deleteFromOptionsMenu() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.delete)).perform(click());
    }

    private void clickOnFilterInList(String filterName) {
        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .check(matches(hasDescendant(withText(filterName))))
                .perform(actionOnItem(hasDescendant(withText(filterName)), click()));
    }


    private UrlForwarderApplication getApplication() {
        return (UrlForwarderApplication) testRule.getActivity().getApplication();
    }
}
