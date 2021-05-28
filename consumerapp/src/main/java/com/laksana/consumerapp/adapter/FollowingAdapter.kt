package com.laksana.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laksana.consumerapp.R
import com.laksana.consumerapp.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowingAdapter(private val followingItem: ArrayList<User>): RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    fun setData(following: ArrayList<User>) {
        followingItem.clear()
        followingItem.addAll(following)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followingItem[position])
    }

    override fun getItemCount(): Int {
        return followingItem.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar = itemView.img_avatar
        private val username = itemView.tv_username

        internal fun bind(follow: User) {
            Glide.with(itemView.context).load(follow.avatar)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(avatar)

            username.text = follow.username
        }
    }
}