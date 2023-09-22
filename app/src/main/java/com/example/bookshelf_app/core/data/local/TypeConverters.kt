package com.example.bookshelf_app.core.data.local

import androidx.room.TypeConverter

class TypeConverters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        var result = ""
        list.forEach {
            result += "$it,"
        }
        return result
    }

    @TypeConverter
    fun fromString(string: String): List<String> {
        return string.split(",")
    }
}