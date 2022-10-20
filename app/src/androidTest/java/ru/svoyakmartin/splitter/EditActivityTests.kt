package ru.svoyakmartin.splitter

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import ru.svoyakmartin.splitter.screens.add.WedgeEditActivity

@RunWith(AndroidJUnit4::class)
class EditActivityTests {
    @get:Rule
    val activity = ActivityScenarioRule(WedgeEditActivity::class.java)

    @Test
    fun calculateSum() {
        Espresso.onView(ViewMatchers.withId(R.id.add_edit_text))
            .perform(ViewActions.typeText("100.00"))

        Espresso.onView(ViewMatchers.withId(R.id.out_edit_text))
            .perform(ViewActions.typeText("200.00"))

        Espresso.onView(ViewMatchers.withId(R.id.add_extra_edit_text))
            .perform(ViewActions.typeText("300.00"))

        Espresso.onView(ViewMatchers.withId(R.id.sum_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("Клин: 330.00"))))
    }
}