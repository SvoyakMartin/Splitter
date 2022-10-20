package ru.svoyakmartin.splitter

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import ru.svoyakmartin.splitter.screens.main.MainActivity

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTests {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    fun testScroll() {
        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(7)
        )
    }
}