package com.mipt.android.di

import android.content.SharedPreferences
import com.mipt.android.preferences.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    @Provides
    @Singleton
    fun getRepository(sharedPreferences: SharedPreferences): SessionManager = SessionManager(sharedPreferences)
}