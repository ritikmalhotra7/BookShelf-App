package com.example.bookshelf_app.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.bookshelf_app.core.data.local.AppDatabase
import com.example.bookshelf_app.core.data.local.UserDao
import com.example.bookshelf_app.core.data.remote.BookApi
import com.example.bookshelf_app.core.utils.CoreUtils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideDatabase(ctx: Application): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app_db")
            .build()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.getUserDao

    @Provides
    @Singleton
    fun provideApi(): BookApi {
        var httpClient = OkHttpClient.Builder()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(
                BookApi::
                class.java
            )
    }
}