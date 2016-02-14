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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

        addFilter(filterName);

        registerIdlingResources(saveResource);

        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .check(matches(hasDescendant(withText(filterName))))
                .perform(actionOnItem(hasDescendant(withText(filterName)), click()));

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        ModifyFilterIdlingResource deleteResource = new ModifyFilterIdlingResource(UUID.randomUUID().toString());
        getApplication().setModifyFilterIdlingResource(deleteResource);

        onView(withText(R.string.delete))
                .perform(click());
        registerIdlingResources(deleteResource);

        onView(withId(R.id.list))
                .check(matches(isDisplayed()))
                .check(matches(not(hasDescendant(withText(filterName)))));
    }

    private void addFilter(String filterName) {
        onView(withId(R.id.btnAddFilter))
                .perform(click());
        onView(withId(R.id.editTitle))
                .perform(clearText())
                .perform(typeText(filterName));
        onView(withId(R.id.editFilter))
                .perform(clearText())
                .perform(typeText("http://daverix.net/test.php?url=@uri"));
        onView(withId(R.id.editReplacableText))
                .perform(clearText())
                .perform(typeText("@uri"));
        onView(withId(R.id.menuSave))
                .perform(click());
    }

    private UrlForwarderApplication getApplication() {
        return (UrlForwarderApplication) testRule.getActivity().getApplication();
    }
}
