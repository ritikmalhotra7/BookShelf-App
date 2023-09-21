package com.example.bookshelf_app.feat_auth.domain.usecases

import com.example.bookshelf_app.core.data.mappers.toUserEntity
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(user: UserModel): Flow<ResponseState<Boolean>> {
        return repository.insertUser(user.toUserEntity())
    }
}