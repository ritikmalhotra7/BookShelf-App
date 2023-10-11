package com.example.bookshelf_app.feat_auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.feat_auth.domain.usecases.GetAllUsersUseCase
import com.example.bookshelf_app.feat_auth.domain.usecases.InsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getAllUsers: GetAllUsersUseCase,
    private val insertUsers: InsertUserUseCase
) : ViewModel() {
    private val _mSignUpState = MutableStateFlow(SignUpState())
    val mSignUpState get() = _mSignUpState

    // State class to store and pass data to UI
    data class SignUpState(
        val users: List<UserModel> = listOf(),
        val insertStatus: Boolean? = null,
        val containsError: String? = null,
        val isLoading: Boolean? = null
    )

    fun getAllUserList() = viewModelScope.launch(Dispatchers.IO) {
        getAllUsers().collectLatest { result ->
            when (result) {
                is ResponseState.Success -> {
                    result.data?.let { list ->
                        _mSignUpState.emit(
                            _mSignUpState.value.copy(
                                users = list,
                                containsError = null,
                                isLoading = false
                            )
                        )
                    }
                }

                is ResponseState.Error -> {
                    _mSignUpState.emit(
                        _mSignUpState.value.copy(
                            users = listOf(),
                            containsError = result.message,
                            isLoading = false
                        )
                    )
                }

                is ResponseState.Loading -> {
                    _mSignUpState.emit(
                        _mSignUpState.value.copy(
                            users = listOf(),
                            containsError = null,
                            isLoading = true
                        )
                    )
                }
            }
        }
    }

    fun insertUser(user: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        insertUsers(user).collectLatest { result ->
            when (result) {
                is ResponseState.Success -> {
                    _mSignUpState.emit(
                        _mSignUpState.value.copy(
                            insertStatus = true,
                            containsError = null,
                            isLoading = false
                        )
                    )
                }

                is ResponseState.Error -> {
                    _mSignUpState.emit(
                        _mSignUpState.value.copy(
                            insertStatus = null,
                            containsError = result.message,
                            isLoading = false
                        )
                    )
                }

                is ResponseState.Loading -> {
                    _mSignUpState.emit(
                        _mSignUpState.value.copy(
                            insertStatus = null,
                            containsError = null,
                            isLoading = true
                        )
                    )
                }
            }
        }
    }
}