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

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static net.daverix.urlforward.Actions.clickAddFilter;
import static net.daverix.urlforward.Actions.clickEncodeCheckbox;
import static net.daverix.urlforward.Actions.saveUsingIdlingResource;
import static net.daverix.urlforward.Actions.setFilterData;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class LinkDialogActivityTest {
    @Test
    public void shouldStartIntentWithCorrectUri() throws UnsupportedEncodingException {
        final ActivityScenario<FiltersActivity> createFilterScenario = ActivityScenario.launch(FiltersActivity.class);

        String filterName = "MyTestFilter-" + UUID.randomUUID();

        clickAddFilter();
        setFilterData(filterName, "http://daverix.net/test.php?url=@uri&subject=@subject", "@uri", "@subject");
        saveUsingIdlingResource(createFilterScenario);

        Intents.init();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://example.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Example");
        intent.setClassName("net.daverix.urlforward", "net.daverix.urlforward.LinkDialogActivity");
        ActivityScenario.launch(intent);

        onData(withLinkFilter(filterName)).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("http://daverix.net/test.php?url=" + URLEncoder.encode("http://example.com", "UTF-8") + "&subject=Example")));
        Intents.release();
    }

    @Test
    public void shouldStartIntentWithCorrectUriWhenUriNotEncoded() {
        final ActivityScenario<FiltersActivity> createFilterScenario = ActivityScenario.launch(FiltersActivity.class);

        String filterName = "MyTestFilter-" + UUID.randomUUID();

        clickAddFilter();
        setFilterData(filterName, "http://daverix.net/test/@uri", "@uri", "");
        closeSoftKeyboard();
        clickEncodeCheckbox();
        saveUsingIdlingResource(createFilterScenario);

        Intents.init();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://example2.com");
        intent.setClassName("net.daverix.urlforward", "net.daverix.urlforward.LinkDialogActivity");
        ActivityScenario.launch(intent);

        onData(withLinkFilter(filterName)).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("http://daverix.net/test/http://example2.com")));
        Intents.release();
    }

    private static BoundedMatcher<Object, LinkFilter> withLinkFilter(final String filterName) {
        return new BoundedMatcher<Object, LinkFilter>(LinkFilter.class) {
            @Override
            protected boolean matchesSafely(LinkFilter item) {
                return filterName.equals(item.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with title: ").appendValue(filterName);
            }
        };
    }
}
