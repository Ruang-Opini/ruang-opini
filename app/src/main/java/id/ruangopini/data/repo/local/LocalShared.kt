package id.ruangopini.data.repo.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object LocalShared {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val INTRO_KEY = booleanPreferencesKey("intro_key")

    suspend fun setAlreadyWatchIntro(context: Context) {
        context.dataStore.edit { settings -> settings[INTRO_KEY] = true }
    }

    suspend fun isAlreadyWatchIntro(context: Context) = flow {
        val data = context.dataStore.data.first()[INTRO_KEY] ?: false
        emit(data)
    }
}