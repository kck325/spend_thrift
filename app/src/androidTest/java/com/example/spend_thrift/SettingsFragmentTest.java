package com.example.spend_thrift;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    private FragmentScenario<SettingsFragment> scenario;

    @Before
    public void setUp() {
        scenario = FragmentScenario.launchInContainer(SettingsFragment.class);
    }

    @Test
    public void testSettingsFragmentUI() {
        // Check if EditText fields are displayed
        Espresso.onView(withId(R.id.small_value_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.small_times_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.medium_value_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.medium_times_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.large_value_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.large_times_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.extra_large_value_input)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.extra_large_times_input)).check(matches(isDisplayed()));

        // Check if Save button is displayed
        Espresso.onView(withId(R.id.save_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSettingsFragmentSaveButton() {
        // Enter values in EditText fields
        Espresso.onView(withId(R.id.small_value_input)).perform(clearText(), typeText("50"));
        Espresso.onView(withId(R.id.small_times_input)).perform(clearText(), typeText("5"));
        Espresso.onView(withId(R.id.medium_value_input)).perform(clearText(), typeText("200"));
        Espresso.onView(withId(R.id.medium_times_input)).perform(clearText(), typeText("4"));
        Espresso.onView(withId(R.id.large_value_input)).perform(clearText(), typeText("1000"));
        Espresso.onView(withId(R.id.large_times_input)).perform(clearText(), typeText("2"));
        Espresso.onView(withId(R.id.extra_large_value_input)).perform(clearText(), typeText("5000"));
        Espresso.onView(withId(R.id.extra_large_times_input)).perform(clearText(), typeText("1"));

        // Click Save button
        Espresso.onView(withId(R.id.save_button)).perform(click());

    }
}
