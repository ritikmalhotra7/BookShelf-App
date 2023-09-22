package com.example.bookshelf_app.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookshelf_app.core.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
@TypeConverters(com.example.bookshelf_app.core.data.local.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val getUserDao: UserDao
}