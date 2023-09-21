package com.example.bookshelf_app.feat_auth.domain.repositories

import com.example.bookshelf_app.core.data.local.entities.UserEntity
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.core.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getAllUsers(): Flow<ResponseState<List<UserModel>>>
    suspend fun insertUser(user:UserEntity): Flow<ResponseState<Boolean>>
}