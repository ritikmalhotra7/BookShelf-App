package com.example.bookshelf_app.feat_book_list.domain.models

import java.io.Serializable

data class BookModel(
    val alias: String? = null,
    val hits: Int? = null,
    val id: String? = null,
    val image: String? = null,
    val lastChapterDate: Int? = null,
    val title: String? = null,
    var isFav: Boolean? = null
) : Serializable