package com.example.bookshelf_app.core.data.mappers

import com.example.bookshelf_app.core.data.local.entities.UserEntity
import com.example.bookshelf_app.feat_auth.domain.models.UserModel

fun UserEntity.toUserModel(): UserModel {
    return UserModel(userId, userName, password, country)
}
fun UserModel.toUserEntity():UserEntity{
    return UserEntity(userName = userName, password =  password, country =  country)
}