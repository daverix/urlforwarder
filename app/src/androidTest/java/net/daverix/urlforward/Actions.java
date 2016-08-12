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
