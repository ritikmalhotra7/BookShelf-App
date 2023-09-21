package com.example.bookshelf_app.feat_book_list.data.repositories

import com.example.bookshelf_app.core.data.mappers.toBookModel
import com.example.bookshelf_app.core.data.remote.BookApi
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BookApi) : BookRepository {
    override suspend fun getBooks(): Flow<ResponseState<List<BookModel>>> = flow {
        emit(ResponseState.Loading())
        val data = api.getBooks()
        if (data.isSuccessful) {
            data.body()?.let { list ->
                emit(ResponseState.Success(list.map { it.toBookModel() }))
            }
        } else {
            emit(ResponseState.Error(message = data.message()))
        }
    }
}