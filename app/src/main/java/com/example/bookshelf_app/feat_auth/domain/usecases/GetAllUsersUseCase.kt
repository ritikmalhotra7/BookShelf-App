package com.example.bookshelf_app.feat_auth.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): Flow<ResponseState<List<UserModel>>>{
        return repository.getAllUsers()
    }
}