package com.example.bookshelf_app.feat_auth.domain.models

import android.os.Parcelable
import java.io.Serializable

data class UserModel(
    val userId:Int? = null,
    val userName:String? = null,
    val password:String? = null,
    val country:String? = null
):Serializable
