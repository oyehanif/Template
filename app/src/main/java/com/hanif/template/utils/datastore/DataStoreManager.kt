package com.daylet.dayletapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    private val appDataStore: DataStore<Preferences>,
) {
    suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T,
    ) {

//        "setValue ---> key: $key -> value: $value".logd()
        appDataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T> {
        return appDataStore.getValue(key, defaultValue)
    }

    suspend fun <T> DataStore<Preferences>.getValue(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T> {
        return data.catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map { preferences ->
//            "key -> $key  value -> $preferences[key]".logd()
            preferences[key] ?: defaultValue
        }
    }

    suspend fun clearDataStorePreferences() {
        appDataStore.edit { preferences ->
            preferences.remove(PreferencesKey.ACCESS_TOKEN)
            preferences.remove(PreferencesKey.PROFILE_DATA)
            preferences.remove(PreferencesKey.IS_FINGERPRINT)
        }
    }

    suspend fun clearAllDataStorePreferences() {
        appDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

object PreferencesKey {
    val ACCESS_TOKEN: Preferences.Key<String> = stringPreferencesKey("access_token")
    val FIRST_LAUNCH: Preferences.Key<Boolean> = booleanPreferencesKey("first_launch")
    val SELECTED_LANGUAGE: Preferences.Key<String> = stringPreferencesKey("selected_language")
    val EMAIL: Preferences.Key<String> = stringPreferencesKey("email")
    val PASSWORD: Preferences.Key<String> = stringPreferencesKey("password")
    val REMEMBER_ME: Preferences.Key<Boolean> = booleanPreferencesKey("remember_me")
    val PROFILE_DATA: Preferences.Key<String> = stringPreferencesKey("profile")
    val IS_FINGERPRINT: Preferences.Key<Boolean> = booleanPreferencesKey("is_fingerprint")
}
