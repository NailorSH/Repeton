package com.nailorsh.repeton

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

private const val SETTINGS_PREFERENCES_NAME = "settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)

@HiltAndroidApp
class RepetonApplication : Application() {

}