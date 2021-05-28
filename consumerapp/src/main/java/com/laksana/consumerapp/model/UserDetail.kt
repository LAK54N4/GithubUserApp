package com.laksana.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail (
   var id : Int,
   var username: String,
   var avatar: String,
   var location: String,
   var repository: String,
   var followers: String,
   var following: String,
): Parcelable



