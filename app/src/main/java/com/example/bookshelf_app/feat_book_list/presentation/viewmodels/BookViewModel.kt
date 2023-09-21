package com.example.bookshelf_app.feat_book_list.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.domain.usecases.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooks:GetBooksUseCase
):ViewModel() {
    private val _mBookState = MutableStateFlow(BookState())
    val mBookState get() = _mBookState

    init {
        getBookList()
    }

    data class BookState(
        val books:List<BookModel> = listOf(),
        val containsError:String? = null,
        val isLoading:Boolean? = null
    )

    private fun getBookList() = viewModelScope.launch(Dispatchers.IO) {
        getBooks().collectLatest {result ->
            when(result){
                is ResponseState.Success->{
                    result.data?.let{
                        _mBookState.emit(_mBookState.value.copy(books = it, containsError = null, isLoading = false))
                    }
                }
                is ResponseState.Error->{
                    _mBookState.emit(_mBookState.value.copy(books = listOf(), containsError = result.message, isLoading = false))
                }
                is ResponseState.Loading->{
                    _mBookState.emit(_mBookState.value.copy(books = listOf(), containsError = null, isLoading = true))
                }
            }
        }
    }
}