package com.laksana.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite (
    var id: Int = 0,
    var username: String,
    var avatar: String
): Parcelable


