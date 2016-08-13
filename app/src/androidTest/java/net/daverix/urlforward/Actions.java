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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class Actions {
    public static void addFilter(String filterName, String name, String replaceableText) {
        onView(withId(R.id.btnAddFilter))
                .perform(click());
        onView(withId(R.id.editTitle))
                .perform(clearText())
                .perform(typeText(filterName));
        onView(withId(R.id.editFilter))
                .perform(clearText())
                .perform(typeText(name));
        onView(withId(R.id.editReplacableText))
                .perform(clearText())
                .perform(typeText(replaceableText));
        onView(withId(R.id.menuSave))
                .perform(click());
    }
}
