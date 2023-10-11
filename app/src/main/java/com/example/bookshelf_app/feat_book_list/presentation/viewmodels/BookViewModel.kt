package com.example.bookshelf_app.feat_book_list.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf_app.core.utils.CoreUtils.isConnected
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.domain.usecases.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooks: GetBooksUseCase,
    @ApplicationContext context: Context
) : ViewModel() {
    private val _mBookState = MutableStateFlow(BookState())
    val mBookState get() = _mBookState

    init {
        if (isConnected(
                context,
                onAvailableNetwork = { getBookList() },
                onLost = {
                    viewModelScope.launch {
                        _mBookState.emit(_mBookState.value.copy(containsError = "There is no internet connection"))
                    }
                })
        ) {
            getBookList()
        } else {
            viewModelScope.launch {
                _mBookState.emit(_mBookState.value.copy(containsError = "There is no internet connection"))
            }
        }
    }

    // State class to store and pass data to UI
    data class BookState(
        val books: List<BookModel> = listOf(),
        val containsError: String? = null,
        val isLoading: Boolean? = null
    )

    private fun getBookList() {
        viewModelScope.launch(Dispatchers.IO) {
            getBooks().collectLatest { result ->
                when (result) {
                    is ResponseState.Success -> {
                        result.data?.let {
                            _mBookState.emit(
                                _mBookState.value.copy(
                                    books = it,
                                    containsError = null,
                                    isLoading = false
                                )
                            )
                        }
                    }

                    is ResponseState.Error -> {
                        _mBookState.emit(
                            _mBookState.value.copy(
                                books = listOf(),
                                containsError = result.message,
                                isLoading = false
                            )
                        )
                    }

                    is ResponseState.Loading -> {
                        _mBookState.emit(
                            _mBookState.value.copy(
                                books = listOf(),
                                containsError = null,
                                isLoading = true
                            )
                        )
                    }
                }
            }
        }
    }
}