package com.example.to_docompose.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.util.Constants.PREFERENCE_KEY
import com.example.to_docompose.util.Constants.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// Why do we need to use @ViewModelScoped?
    // Because we are going to inject this DataStoreRepository inside our SharedViewModel.

// Extension property on a context object
val Context.dataStore by preferencesDataStore(name = PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private object PreferenceKeys {
        val sortKey = stringPreferencesKey(name = PREFERENCE_KEY)
    }

    private val dataStore = context.dataStore

    suspend fun persistSortState(priority: Priority) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.sortKey] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortState = preferences[PreferenceKeys.sortKey] ?: Priority.NONE.name
            sortState
        }

    // This is the simplest way to implement preference data store. This example is based on the documentation.
    /*private val dataStore = context.dataStore
    val sortKey = stringPreferencesKey(PREFERENCE_KEY)

    val readSortState: Flow<String> = dataStore.data
        .map { preference ->
            val sortState = preference[sortKey] ?: Priority.NONE.name
            sortState
        }

    suspend fun persistSortState(priority: Priority) {
        dataStore.edit { preference ->
            preference[sortKey] = priority.name
        }
    }*/

}
