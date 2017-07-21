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
package net.daverix.urlforward;

import android.content.Intent;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static net.daverix.urlforward.Actions.clickAddFilter;
import static net.daverix.urlforward.Actions.clickEncodeCheckbox;
import static net.daverix.urlforward.Actions.saveUsingIdlingResource;
import static net.daverix.urlforward.Actions.setFilterData;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class LinkDialogActivityTest {
    @Rule
    public ActivityTestRule<FiltersActivity> createFilterTestRule = new ActivityTestRule<>(FiltersActivity.class, false, false);

    @Rule
    public IntentsTestRule<LinkDialogActivity> testRule = new IntentsTestRule<>(LinkDialogActivity.class, false, false);

    @Test
    public void shouldStartIntentWithCorrectUri() throws UnsupportedEncodingException {
        createFilterTestRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        String filterName = "MyTestFilter-" + UUID.randomUUID();

        clickAddFilter();
        setFilterData(filterName, "http://daverix.net/test.php?url=@uri&subject=@subject", "@uri", "@subject");
        saveUsingIdlingResource(getApplication());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://example.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Example");
        testRule.launchActivity(intent);

        onData(withLinkFilter(filterName)).perform(click());
        intended(allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData("http://daverix.net/test.php?url=" + URLEncoder.encode("http://example.com", "UTF-8") + "&subject=Example")));
    }

    @Test
    public void shouldStartIntentWithCorrectUriWhenUriNotEncoded() throws UnsupportedEncodingException {
        createFilterTestRule.launchActivity(new Intent(Intent.ACTION_MAIN));

        String filterName = "MyTestFilter-" + UUID.randomUUID();

        clickAddFilter();
        setFilterData(filterName, "http://daverix.net/test/@uri", "@uri", "");
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(getApplication());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://example2.com");
        testRule.launchActivity(intent);

        onData(withLinkFilter(filterName)).perform(click());
        intended(allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData("http://daverix.net/test/http://example2.com")));
    }

    private UrlForwarderApplication getApplication() {
        return (UrlForwarderApplication) createFilterTestRule.getActivity().getApplication();
    }

    private static BoundedMatcher<Object, LinkFilterViewModel> withLinkFilter(final String filterName) {
        return new BoundedMatcher<Object, LinkFilterViewModel>(LinkFilterViewModel.class) {
            @Override
            protected boolean matchesSafely(LinkFilterViewModel item) {
                return filterName.equals(item.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with title: ").appendValue(filterName);
            }
        };
    }
}
