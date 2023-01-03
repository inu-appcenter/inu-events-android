package org.inu.events

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.inu.events.ui.home.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 에뮬레이터 언어 설정 한국어로 설정해 주세요!
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class PostEventTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun test() {
//        login()
        onView(withId(R.id.floating_action_button)).perform(click())
        onView(withId(R.id.checkBox_image)).perform(click())
        onView(withId(R.id.button_complete)).perform(click())
        onView(withId(R.id.editText_title)).perform(typeText("wpahrdldi~~~!!"))
        onView(withId(R.id.editText_host)).perform(typeText("doqtpsxj"))
        onView(withId(R.id.editText_target)).perform(typeText("sjaksdhaus eho!!"))
        onView(withId(R.id.checkBox)).perform(click())
        onView(withId(R.id.checkBox_location)).perform(click())
        onView(withId(R.id.spinner_classification)).perform(click())
//        onView(ViewMatchers.withSpinnerText("동아리/소모임")).perform(click())
    }

    fun login() {
        onView(withId(R.id.imageView)).perform(click())

    }
}