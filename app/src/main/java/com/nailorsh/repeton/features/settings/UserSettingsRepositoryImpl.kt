package com.nailorsh.repeton.features.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val SETTINGS = "SETTINGS"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS)

class UserSettingsRepositoryImpl(
    private val context: Context
) : UserSettingsRepository {


    override suspend fun updateTheme(isDarkTheme: Boolean) {
        try {
            context.dataStore.edit { settings ->
                settings[IS_DARK_THEME] = isDarkTheme
            }
        } catch (e: Exception) {
            e.localizedMessage
        }
    }

    override suspend fun getTheme(): Flow<Boolean> {
        val isDarkTheme: Flow<Boolean> = context.dataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { settings ->
                settings[IS_DARK_THEME] ?: false
            }
        return isDarkTheme
    }

    companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }
}