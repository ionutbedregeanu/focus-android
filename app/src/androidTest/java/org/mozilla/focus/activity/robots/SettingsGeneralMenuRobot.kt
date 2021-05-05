/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.activity.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.mozilla.focus.helpers.TestHelper.mDevice
import org.mozilla.focus.helpers.TestHelper.packageName

class SettingsGeneralMenuRobot {

    fun verifyGeneralSettingsItems() {
        defaultBrowserSwitch.check(matches(isDisplayed()))
        switchToLinkToggleButton.check(matches(isDisplayed()))
        languagePicker.check(matches(isDisplayed()))
    }

    fun openLanguageMenu(localizedText: String) {
        localizedLanguagePicker(localizedText).perform(click())
    }

    fun verifySystemDefaultLocaleIsSelected(localizedText: String) {
        systemDefaultLocale(localizedText).check(matches(isChecked()))
    }

    fun selectLanguage(language: String) {
        languagesList.scrollIntoView(UiSelector().text(language))
        mDevice.findObject(UiSelector().text(language)).click()
    }

    class Transition
}

private val defaultBrowserSwitch = onView(withText("Make Firefox Focus default browser"))

private val switchToLinkToggleButton = onView(withText("Switch to link in new tab immediately"))

private val languagePicker = onView(withText("Language"))

private fun localizedLanguagePicker(localizedText: String) = onView(withText(localizedText))

private fun systemDefaultLocale(localizedText: String) = onView(withText(localizedText))

private val languagesList =
    UiScrollable(UiSelector().resourceId("$packageName:id/select_dialog_listview"))
