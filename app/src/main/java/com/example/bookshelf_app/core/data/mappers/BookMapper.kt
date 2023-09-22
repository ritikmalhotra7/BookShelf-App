package com.example.bookshelf_app.core.data.mappers

import com.example.bookshelf_app.core.data.remote.dto.BookDtoItem
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel

fun BookDtoItem.toBookModel(): BookModel {
    return BookModel(alias, hits, id, image, lastChapterDate, title, null)
}

fun BookModel.toBookDtoItem(): BookDtoItem {
    return BookDtoItem(alias, hits, id, image, lastChapterDate, title)
}