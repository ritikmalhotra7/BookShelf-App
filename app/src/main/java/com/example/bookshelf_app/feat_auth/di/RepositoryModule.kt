package com.example.bookshelf_app.feat_auth.di

import com.example.bookshelf_app.feat_auth.data.repositories.AuthRepositoryImpl
import com.example.bookshelf_app.feat_auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(implementation: AuthRepositoryImpl): AuthRepository
}