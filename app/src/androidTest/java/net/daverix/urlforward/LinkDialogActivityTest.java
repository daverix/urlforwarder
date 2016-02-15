package net.daverix.urlforward;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class LinkDialogActivityTest {

    @Rule
    public IntentsTestRule<LinkDialogActivity> testRule = new IntentsTestRule<>(LinkDialogActivity.class, false, false);

    @Test
    public void shouldStartIntentWithCorrectUri() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("http://example.com"));
        testRule.launchActivity(intent);

        ContentResolver contentResolver = testRule.getActivity().getContentResolver();
        String filterName = "MyTestFilter-" + UUID.randomUUID();
        insertFilter(contentResolver, filterName, "http://daverix.net/test.php?url=@url", "@url");

        onView(withText(filterName)).perform(click());
        intended(allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW),
                IntentMatchers.hasData("http://daverix.net/test.php?url=http://example.com")));
    }

    private static Uri insertFilter(ContentResolver contentResolver, String filterName, String filter, String replaceText) {
        ContentValues values = new ContentValues();
        values.put(UrlFilters.CREATED, System.currentTimeMillis());
        values.put(UrlFilters.UPDATED, System.currentTimeMillis());
        values.put(UrlFilters.TITLE, filterName);
        values.put(UrlFilters.FILTER, filter);
        values.put(UrlFilters.REPLACE_TEXT, replaceText);
        return contentResolver.insert(UrlFilters.CONTENT_URI, values);
    }
}
