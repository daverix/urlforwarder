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

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

@RunWith(AndroidJUnit4::class)
class LinkDialogActivityTest {
    @get:Rule
    var createFilterTestRule = ActivityTestRule(FiltersActivity::class.java, false, false)

    @get:Rule
    var testRule = IntentsTestRule(LinkDialogActivity::class.java, false, false)

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun shouldStartIntentWithCorrectUri() {
        createFilterTestRule.launchActivity(Intent(Intent.ACTION_MAIN))

        val filterName = "MyTestFilter-" + UUID.randomUUID()

        clickAddFilter()
        setFilterData(
                filterName = filterName,
                filter = "http://daverix.net/test.php?url=@uri&subject=@subject",
                replaceableText = "@uri",
                replaceableSubject = "@subject"
        )
        save()

        testRule.launchActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "http://example.com")
            putExtra(Intent.EXTRA_SUBJECT, "Example")
        })

        clickLink(filterName)

        val encodedUrl = URLEncoder.encode("http://example.com", "UTF-8")
        val expectedData = "http://daverix.net/test.php?url=$encodedUrl&subject=Example"

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(expectedData)))
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun shouldStartIntentWithCorrectUriWhenUriNotEncoded() {
        createFilterTestRule.launchActivity(Intent(Intent.ACTION_MAIN))

        val filterName = "MyTestFilter-" + UUID.randomUUID()

        clickAddFilter()
        setFilterData(filterName, "http://daverix.net/test/@uri", "@uri", "")
        onView(withId(R.id.checkEncode))
                .check(matches(ViewMatchers.isChecked()))
                .perform(click())
        save()

        testRule.launchActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "http://example2.com")
        })

        clickLink(filterName)
        intended(allOf(hasAction(Intent.ACTION_VIEW),
                hasData("http://daverix.net/test/http://example2.com")))
    }
}
