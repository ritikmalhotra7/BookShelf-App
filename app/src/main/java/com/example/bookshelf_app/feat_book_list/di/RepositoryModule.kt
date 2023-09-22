package com.example.bookshelf_app.feat_book_list.di

import com.example.bookshelf_app.feat_book_list.data.repositories.BookRepositoryImpl
import com.example.bookshelf_app.feat_book_list.domain.repositories.BookRepository
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
    abstract fun bindsBookRepository(repositoryImpl: BookRepositoryImpl): BookRepository
}