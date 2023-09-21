package com.example.bookshelf_app.feat_book_list.domain.repositories

import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(): Flow<ResponseState<List<BookModel>>>
}