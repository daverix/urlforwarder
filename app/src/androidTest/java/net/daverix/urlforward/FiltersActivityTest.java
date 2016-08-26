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

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.daverix.urlforward.Actions.clickAddFilter;
import static net.daverix.urlforward.Actions.clickEncodeCheckbox;
import static net.daverix.urlforward.Actions.clickOnFilterInList;
import static net.daverix.urlforward.Actions.deleteUsingIdlingResource;
import static net.daverix.urlforward.Actions.saveUsingIdlingResource;
import static net.daverix.urlforward.Actions.setFilterData;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class FiltersActivityTest {
    @Rule
    public ActivityTestRule<FiltersActivity> testRule = new ActivityTestRule<>(FiltersActivity.class);

    @Test
    public void shouldAddAndRemoveFilter() {
        String filterName = "MyFilter-" + UUID.randomUUID();

        clickAddFilter();

        setFilterData(filterName, "http://daverix.net/test.php?url=@uri", "@uri");
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName);

        deleteUsingIdlingResource(getApplication());

        checkFilterNameNotInList(filterName);
    }

    @Test
    public void shouldAddDefaultAndVerifyDataIsCorrect() {
        String filterName = "MyFilter-" + UUID.randomUUID();
        String filter = "http://daverix.net/test.php?url=@uri1";
        String replaceableText = "@uri1";

        clickAddFilter();

        setFilterData(filterName, filter, replaceableText);
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName);

        verifyEditTextFieldsMatch(filterName, filter, replaceableText);
        onView(withId(R.id.checkEncode)).check(matches(isChecked()));
    }

    @Test
    public void shouldAddAndUpdateDefaultAndVerifyDataIsCorrect() {
        String filterName = "MyFilter-" + UUID.randomUUID();
        String filter = "http://daverix.net/test.php?url=@uri12";
        String replaceableText = "@uri12";

        String filterName2 = "MyFilter-" + UUID.randomUUID();
        String filter2 = "http://daverix.net/test.php?url=@uri13";
        String replaceableText2 = "@uri13";

        clickAddFilter();

        setFilterData(filterName, filter, replaceableText);
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName);

        setFilterData(filterName2, filter2, replaceableText2);
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName2);

        verifyEditTextFieldsMatch(filterName2, filter2, replaceableText2);
        onView(withId(R.id.checkEncode)).check(matches(isNotChecked()));
    }

    @Test
    public void shouldAddWithoutEncodeAndVerifyDataIsCorrect() {
        String filterName = "MyFilter-" + UUID.randomUUID();
        String filter = "http://daverix.net/test/@uri2";
        String replaceableText = "@uri2";

        clickAddFilter();

        setFilterData(filterName, filter, replaceableText);
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName);

        verifyEditTextFieldsMatch(filterName, filter, replaceableText);
        onView(withId(R.id.checkEncode)).check(matches(isNotChecked()));
    }

    @Test
    public void shouldAddAndUpdateWithEncodeAndVerifyDataIsCorrect() {
        String filterName = "MyFilter-" + UUID.randomUUID();
        String filter = "http://daverix.net/test.php?url=@uri12";
        String replaceableText = "@uri12";

        String filterName2 = "MyFilter-" + UUID.randomUUID();
        String filter2 = "http://daverix.net/test.php?url=@uri13";
        String replaceableText2 = "@uri13";

        clickAddFilter();

        setFilterData(filterName, filter, replaceableText);
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName);

        setFilterData(filterName2, filter2, replaceableText2);
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(getApplication());

        clickOnFilterInList(filterName2);

        verifyEditTextFieldsMatch(filterName2, filter2, replaceableText2);
        onView(withId(R.id.checkEncode)).check(matches(isChecked()));
    }

    private void verifyEditTextFieldsMatch(String filterName, String filter, String replaceableText) {
        onView(withId(R.id.editTitle)).check(matches(withText(filterName)));
        onView(withId(R.id.editFilter)).check(matches(withText(filter)));
        onView(withId(R.id.editReplacableText)).check(matches(withText(replaceableText)));
    }

    private void checkFilterNameNotInList(String filterName) {
        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .check(matches(not(hasDescendant(withText(filterName)))));
    }

    private UrlForwarderApplication getApplication() {
        return (UrlForwarderApplication) testRule.getActivity().getApplication();
    }
}
