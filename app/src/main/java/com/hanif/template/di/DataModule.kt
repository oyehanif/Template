package com.hanif.template.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATA_STORE_PREFS_FILE_NAME = "DATA_STORE"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStore<Preferences> {
        val prefsDataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(DATA_STORE_PREFS_FILE_NAME)
        }
        return prefsDataStore
    }
}

