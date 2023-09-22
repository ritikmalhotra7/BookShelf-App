package com.example.bookshelf_app.core.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bookshelf_app.core.utils.CoreUtils.KEY_FOR_LOGGED_IN_USER
import com.example.bookshelf_app.core.utils.CoreUtils.dataStore
import com.example.bookshelf_app.core.utils.CoreUtils.saveToDataStore
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

object UserProvider {

    suspend fun getLoggedInUser(context: Context): UserModel? {
        try {
            return Gson().fromJson(
                context.dataStore.data.first()[stringPreferencesKey(KEY_FOR_LOGGED_IN_USER)],
                UserModel::class.java
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun saveCurrentLoggedUser(context: Context, user: UserModel): Boolean {
        return try {
            context.saveToDataStore(KEY_FOR_LOGGED_IN_USER, Gson().toJson(user))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun removeCurrentLoggedInUser(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }
}