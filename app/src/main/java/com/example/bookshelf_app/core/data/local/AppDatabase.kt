package com.example.bookshelf_app.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookshelf_app.core.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val getUserDao: UserDao
}