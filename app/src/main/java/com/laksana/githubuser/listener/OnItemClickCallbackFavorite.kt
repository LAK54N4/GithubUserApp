package com.laksana.githubuser.listener

import android.view.View
import com.laksana.githubuser.model.UserFavorite

interface OnItemClickCallbackFavorite {
    fun onItemClicked(view: View, userList: UserFavorite)
}