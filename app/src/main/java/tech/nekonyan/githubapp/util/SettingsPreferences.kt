package tech.nekonyan.githubapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) :
    PreferenceDataStore() {
    private val darkMode = booleanPreferencesKey(Constants.PREFERENCE_DARK_MODE)

    // Fungsi yang diperlukan PreferenceFragmentCompat untuk dapat mengimplementasikan PreferenceDataStore
    override fun putBoolean(key: String?, value: Boolean) {
        key?.let {
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.edit { preferences ->
                    preferences[booleanPreferencesKey(key)] = value
                }
            }
        }
    }

    // Fungsi yang diperlukan PreferenceFragmentCompat untuk dapat mengimplementasikan PreferenceDataStore
    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        key?.let {
            return runBlocking {
                dataStore.data.map { it[booleanPreferencesKey(key)] ?: defValue }.first()
            }
        }

        return false
    }

    fun getDarkModeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[darkMode] ?: false
        }
    }

    companion object {
        @Volatile
        private var instance: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferences {
            return instance ?: synchronized(this) {
                val instance = SettingsPreferences(dataStore)
                this.instance = instance
                instance
            }
        }
    }
}