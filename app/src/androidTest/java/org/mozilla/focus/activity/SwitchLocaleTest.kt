/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.focus.activity

import android.os.Build.VERSION
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mozilla.focus.activity.robots.homeScreen
import org.mozilla.focus.activity.robots.searchScreen
import org.mozilla.focus.helpers.MainActivityFirstrunTestRule
import org.mozilla.focus.helpers.TestHelper.exitToTop
import org.mozilla.focus.helpers.TestHelper.restartApp
import org.mozilla.focus.helpers.TestHelper.verifyLocalizedText
import org.mozilla.focus.locale.LocaleManager
import java.util.*

// This test checks default EN and FR system locales and changing the language
@RunWith(AndroidJUnit4ClassRunner::class)
class SwitchLocaleTest {
    private val frenchGeneralHeading = "Général"
    private val englishSystemDefault = "System default"
    private val frenchSystemDefault = "Valeur par défaut du système"
    private val englishLanguageHeading = "Language"
    private val frenchLanguageHeading = "Langue"
    private val frenchLanguage = "Français"
    private val englishLanguage = "English (United States)"
    private val frenchSearchBarText = "Recherche ou adresse"
    private val englishSearchBarText = "Search or enter address"

    @get: Rule
    var mActivityTestRule = MainActivityFirstrunTestRule(showFirstRun = false)

    @After
    fun tearDown() {
        changeLocale("en")
        LocaleManager().resetToSystemLocale(mActivityTestRule.activity.applicationContext)
        mActivityTestRule.activity.finishAndRemoveTask()
    }

    @Test
    fun englishSystemLocaleTest() {
        homeScreen {
        }.openMainMenu {
        }.openSettings {
        }.openGeneralSettingsMenu {
            openLanguageMenu(englishLanguageHeading)
            /* system locale is in English, check it is now set to system locale */
            verifySystemDefaultLocaleIsSelected(englishSystemDefault)
            /* change locale to French, verify the locale is changed */
            selectLanguage(frenchLanguage)
            verifyLocalizedText(frenchLanguageHeading)
            verifyLocalizedText(frenchGeneralHeading)
            exitToTop()
        }
        /* Exit to main and see the UI is in French as well */
        searchScreen {
            verifySearchEditBarContainsText(frenchSearchBarText)
        }
        homeScreen {
        }.openMainMenu {
        verifyLocalizedText("Aide")
        verifyLocalizedText("Nouveautés")
        verifyLocalizedText("Paramètres")

        /* re-enter language settings, change it back to system locale, verify the locale is changed */
        }.openSettings {
        }.openLocalizedGeneralSettingsMenu(frenchGeneralHeading) {
            openLanguageMenu(frenchLanguageHeading)
            selectLanguage(frenchSystemDefault)
            verifyLocalizedText(englishLanguageHeading)
            exitToTop()
        }
        searchScreen {
            verifySearchEditBarContainsText(englishSearchBarText)
        }
    }

    @Test
    fun frenchSystemLocaleTest() {
        changeLocale("fr")
        restartApp(mActivityTestRule)

        /* Go to General Settings */
        homeScreen {
        }.openMainMenu {
        }.openSettings {
        }.openLocalizedGeneralSettingsMenu(frenchGeneralHeading) {
            /* system locale is in French, check it is now set to system locale */
            openLanguageMenu(frenchLanguageHeading)
            verifySystemDefaultLocaleIsSelected(frenchSystemDefault)
            /* change locale to English in the setting, verify the locale is changed */
            selectLanguage(englishLanguage)
            verifyLocalizedText(englishLanguageHeading)
            exitToTop()
        }
        searchScreen {
            verifySearchEditBarContainsText(englishSearchBarText)
        }
    }

    companion object {
        @Suppress("Deprecation")
        fun changeLocale(locale: String?) {
            val context = InstrumentationRegistry.getInstrumentation()
                .targetContext
            val res = context.applicationContext.resources
            val config = res.configuration
            config.setLocale(Locale(locale!!))
            if (VERSION.SDK_INT >= 25) {
                context.createConfigurationContext(config)
            } else {
                res.updateConfiguration(config, res.displayMetrics)
            }
        }
    }
}
