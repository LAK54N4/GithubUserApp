package com.laksana.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laksana.githubuser.R
import com.laksana.githubuser.listener.OnItemClickCallbackFavorite
import com.laksana.githubuser.model.UserFavorite
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter(favoriteList: ArrayList<UserFavorite>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var userFav = favoriteList

    set(userFav) {
        if (userFav.size > 0) {
            this.userFav.clear()
        }
        this.userFav.addAll(userFav)
    }

    private var listener: OnItemClickCallbackFavorite? = null
    fun setOnItemClickCallbackFavorite(onItemClickCallback: OnItemClickCallbackFavorite){
        this.listener = onItemClickCallback
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(favorite: UserFavorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .apply(RequestOptions().override(110,110))
                    .into(img_avatar)

                tv_username.text = favorite.username
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(userFav[position])

        holder.itemView.setOnClickListener {
            listener?.onItemClicked(it, userFav[position])
        }
    }

    override fun getItemCount(): Int {
        return userFav.size
    }
}