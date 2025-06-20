package com.lesson.codewalledu.src.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class UserPreferences@Inject constructor(
    @ApplicationContext private val context: Context // Hilt ကနေ ApplicationContext ကို inject လုပ်ပါတယ်။
) {
    private val applicationContext = context.applicationContext

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val ONBOARDING_FINISHED_KEY = booleanPreferencesKey("onboarding_finished")
        private val USER_LOGGED_IN_KEY = booleanPreferencesKey("user_logged_in")
        private val IS_USER_PREMIUM_KEY = booleanPreferencesKey("is_premium_user")

    }



    // Flows to observe onboarding and login status (using readBoolean)
    val isOnboardingFinishedFlow: Flow<Boolean> = readBoolean(ONBOARDING_FINISHED_KEY)
    val isUserLoggedInFlow: Flow<Boolean> = readBoolean(USER_LOGGED_IN_KEY)
    val isUserPremiumFlow: Flow<Boolean> = readBoolean(IS_USER_PREMIUM_KEY)

    // Function to write values to DataStore
    // Function to write Boolean values to DataStore
    suspend fun writeBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        applicationContext.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    // Function to read values from DataStore as a Flow
    // Function to read Boolean values from DataStore as a Flow
    fun readBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return applicationContext.dataStore.data.map { preferences ->
            preferences[key] ?: false // Default value if not found
        }
    }

    suspend fun setOnboardingFinished(finished: Boolean) {
        writeBoolean(ONBOARDING_FINISHED_KEY, finished)
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        writeBoolean(USER_LOGGED_IN_KEY, loggedIn)
    }

    suspend fun setUserPremium(isPremium: Boolean) {
        writeBoolean(IS_USER_PREMIUM_KEY, isPremium)
    }


    // User data အားလုံးကို clear လုပ်ချင်ရင်
    suspend fun clearAllPreferences() {
        applicationContext.dataStore.edit { settings ->
           settings.clear()
        }
    }





}