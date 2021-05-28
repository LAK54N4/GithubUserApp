package com.laksana.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laksana.githubuser.R
import com.laksana.githubuser.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowersAdapter(private val followerItem: ArrayList<User>): RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    fun setData (followers : ArrayList<User>) {
        followerItem.clear()
        followerItem.addAll(followers)
        followerItem.count()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar = itemView.img_avatar
        private val username = itemView.tv_username

        internal fun bind(follower: User) {
            Glide.with(itemView.context).load(follower.avatar)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(avatar)

            username.text = follower.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followerItem[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followerItem.size
    }

}