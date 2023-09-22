package com.example.bookshelf_app.core.data.remote

import com.example.bookshelf_app.core.data.remote.dto.BookDtoItem
import retrofit2.Response
import retrofit2.http.GET

interface BookApi {
    @GET("/b/ZEDF")
    suspend fun getBooks(): Response<List<BookDtoItem>>
}