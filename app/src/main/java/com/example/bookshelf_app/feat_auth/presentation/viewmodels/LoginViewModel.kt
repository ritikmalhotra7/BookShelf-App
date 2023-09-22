package com.example.bookshelf_app.feat_auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.feat_auth.domain.usecases.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAllUsers: GetAllUsersUseCase
) : ViewModel() {
    private val _mLoginState = MutableStateFlow(LoginState())
    val mLoginState get() = _mLoginState

    // State class to store and pass data to UI
    data class LoginState(
        val users: List<UserModel> = listOf(),
        val containsError: String? = null,
        val isLoading: Boolean? = null
    )

    fun getAllUserList() = viewModelScope.launch(Dispatchers.IO) {
        getAllUsers().collectLatest { result ->
            when (result) {
                is ResponseState.Success -> {
                    result.data?.let { list ->
                        _mLoginState.emit(
                            _mLoginState.value.copy(
                                users = list,
                                containsError = null,
                                isLoading = false
                            )
                        )
                    }
                }

                is ResponseState.Error -> {
                    _mLoginState.emit(
                        _mLoginState.value.copy(
                            users = listOf(),
                            containsError = result.message,
                            isLoading = false
                        )
                    )
                }

                is ResponseState.Loading -> {
                    _mLoginState.emit(
                        _mLoginState.value.copy(
                            users = listOf(),
                            containsError = null,
                            isLoading = true
                        )
                    )
                }
            }
        }
    }
}