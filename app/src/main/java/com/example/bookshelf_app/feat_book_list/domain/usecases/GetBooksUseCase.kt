package com.example.bookshelf_app.feat_book_list.domain.usecases

import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.domain.repositories.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke(): Flow<ResponseState<List<BookModel>>> = repository.getBooks()
}