package com.laksana.consumerapp.listener

import android.view.View
import com.laksana.consumerapp.model.UserFavorite

interface OnItemClickCallbackFavorite {
    fun onItemClicked(view: View, userList: UserFavorite)
}