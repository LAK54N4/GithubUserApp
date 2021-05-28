package com.laksana.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.laksana.consumerapp.activity.FavoriteActivity
import com.laksana.consumerapp.activity.SettingsActivity
import com.laksana.consumerapp.activity.ViewDetailsActivity
import com.laksana.consumerapp.adapter.UserAdapter
import com.laksana.consumerapp.listener.OnItemClickCallback
import com.laksana.consumerapp.model.User
import com.laksana.consumerapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    private val listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Github User Consumer"

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)

        userAdapter = UserAdapter(listUser)
        userAdapter.notifyDataSetChanged()

        rv_listName.layoutManager = LinearLayoutManager(this)
        rv_listName.setHasFixedSize(true)
        rv_listName.adapter = userAdapter

        showLoading(false)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MainViewModel::class.java)

        btnSearch.setOnClickListener {
            val username = editUsername.text.toString()
            if (username.isEmpty()) return@setOnClickListener
            showLoading(true)

            mainViewModel.setSearch(username)
        }

        mainViewModel.getListUsers().observe(this, { listUser ->
            if (listUser != null) {
                userAdapter.setData(listUser)
                showLoading(false)
            }
        })

        userAdapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(view: View, userList: User) {
                val userData = User(userList.username, userList.avatar)
                val moveViewDetail = Intent (this@MainActivity, ViewDetailsActivity::class.java)
                moveViewDetail.putExtra(ViewDetailsActivity.EXTRA_DETAIL, userData)
                startActivity(moveViewDetail)
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        }
        else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite_user -> {
                val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.settings -> {
                val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
