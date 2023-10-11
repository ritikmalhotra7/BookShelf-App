package com.example.bookshelf_app.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


object CoreUtils {
    const val BASE_URL = "https://www.jsonkeeper.com"

    private const val PREFERENCES_NAME = "BookShelfPreferences"
    val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

    const val KEY_FOR_LOGGED_IN_USER = "Logged In User Details"

    suspend fun Context.saveToDataStore(key: String, data: String) {
        val key = stringPreferencesKey(key)
        this.dataStore.edit { preferences ->
            preferences[key] = data
        }
    }

    fun isConnected(context:Context, onAvailableNetwork:()->Unit, onLost:()->Unit):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
            .build()
        cm.registerNetworkCallback(networkRequest,object:ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                onAvailableNetwork()
            }

            override fun onLost(network: Network) {
                onLost()
            }
        })
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}