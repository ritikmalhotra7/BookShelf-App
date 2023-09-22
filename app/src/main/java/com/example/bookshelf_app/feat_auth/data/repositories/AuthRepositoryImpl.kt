package com.example.bookshelf_app.feat_auth.data.repositories

import com.example.bookshelf_app.core.data.local.UserDao
import com.example.bookshelf_app.core.data.local.entities.UserEntity
import com.example.bookshelf_app.core.data.mappers.toUserModel
import com.example.bookshelf_app.core.utils.ResponseState
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.feat_auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val userDao: UserDao) : AuthRepository {
    override suspend fun getAllUsers(): Flow<ResponseState<List<UserModel>>> = flow {
        emit(ResponseState.Loading())
        try {
            val data = userDao.getAllUsers()
            emit(ResponseState.Success(data.map { it.toUserModel() }))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResponseState.Error(e.localizedMessage))
        }
    }

    override suspend fun insertUser(user: UserEntity): Flow<ResponseState<Boolean>> = flow {
        emit(ResponseState.Loading())
        try {
            userDao.insertUser(user)
            emit(ResponseState.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResponseState.Error(e.toString()))
        }
    }
}