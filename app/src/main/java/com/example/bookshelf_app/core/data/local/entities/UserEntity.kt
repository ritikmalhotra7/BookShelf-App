package com.example.bookshelf_app.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = null,
    val userName: String? = null,
    val password: String? = null,
    val country: String? = null
)
