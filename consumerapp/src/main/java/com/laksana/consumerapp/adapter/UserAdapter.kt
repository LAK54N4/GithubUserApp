package com.laksana.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laksana.consumerapp.R
import com.laksana.consumerapp.listener.OnItemClickCallback
import com.laksana.consumerapp.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private var userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val listUser = userList
    private var listener: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.listener = onItemClickCallback
    }

    fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_user, parent, false)

        return this.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClicked(it, userList[position])
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName = itemView.tv_username as TextView

        fun bind(items: User) {
            tvUserName.text = items.username
            Glide.with(itemView.context)
                    .load(items.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(itemView.img_avatar)
        }
    }
}
