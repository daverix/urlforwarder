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
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static net.daverix.urlforward.Actions.addFilter;
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
        ModifyFilterIdlingResource saveResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        getApplication().setModifyFilterIdlingResource(saveResource);

        addFilter(filterName, "http://daverix.net/test.php?url=@uri", "@uri");

        registerIdlingResources(saveResource);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "http://example.com");
        testRule.launchActivity(intent);

        onView(withText(filterName)).perform(click());
        intended(allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData("http://daverix.net/test.php?url=" + URLEncoder.encode("http://example.com", "UTF-8"))));
    }

    private UrlForwarderApplication getApplication() {
        return (UrlForwarderApplication) createFilterTestRule.getActivity().getApplication();
    }
}
