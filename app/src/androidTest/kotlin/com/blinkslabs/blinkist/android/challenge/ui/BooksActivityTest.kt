package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.blinkslabs.blinkist.android.challenge.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BooksActivityTest {
    private lateinit var scenario: ActivityScenario<BooksActivity>

    @Before
    fun setUp() {
        scenario = launch(BooksActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testChangeBooksArrangement() {
        onView(withId(R.id.alphabeticalOrder)).perform(click())
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.weekOrder)).perform(click())
            .check(ViewAssertions.matches(isDisplayed()))
    }
}