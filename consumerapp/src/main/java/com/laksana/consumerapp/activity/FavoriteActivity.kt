package com.laksana.consumerapp.activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laksana.consumerapp.MainActivity
import com.laksana.consumerapp.R
import com.laksana.consumerapp.adapter.FavoriteAdapter
import com.laksana.consumerapp.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.laksana.consumerapp.helper.MappingHelper
import com.laksana.consumerapp.listener.OnItemClickCallbackFavorite
import com.laksana.consumerapp.model.UserFavorite
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class FavoriteActivity: AppCompatActivity() {
    companion object{
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: FavoriteAdapter
    private val listUser = ArrayList<UserFavorite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Favorite User"
        actionBar.setDisplayHomeAsUpEnabled(true)

        adapter = FavoriteAdapter(listUser)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(ViewDetailsActivity.EXTRA_STATE)
            if (list != null) {

                adapter = FavoriteAdapter(list)
                adapter.userFav = list
                adapter.notifyDataSetChanged()

                val rv: RecyclerView = findViewById(R.id.rv_listNameFavorite)
                rv.layoutManager = LinearLayoutManager(this@FavoriteActivity, RecyclerView.VERTICAL, false)
                rv.setHasFixedSize(true)
                rv.adapter = adapter
            }
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavorite.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = deferredFavorite.await()
            progressBarFavorite.visibility =View.INVISIBLE
            if(favorite.size>0) {
                adapter.userFav = favorite
                Toast.makeText(this@FavoriteActivity, "${favorite.count()} Records Found", Toast.LENGTH_SHORT).show()

                val rv: RecyclerView = findViewById(R.id.rv_listNameFavorite)
                rv.layoutManager = LinearLayoutManager(this@FavoriteActivity, RecyclerView.VERTICAL, false)
                rv.setHasFixedSize(true)
                rv.adapter = adapter

                adapter.setOnItemClickCallbackFavorite(object : OnItemClickCallbackFavorite {

                    override fun onItemClicked(view: View, userList: UserFavorite) {
                        val userData = UserFavorite(userList.id, userList.username, userList.avatar)

                        val moveViewDetail = Intent (this@FavoriteActivity, ViewDetailsActivity::class.java)
                        moveViewDetail.putExtra(ViewDetailsActivity.EXTRA_FAVORITE, userData)
                        startActivity(moveViewDetail)
                    }
                })

            } else {
                adapter.userFav = ArrayList()
                Toast.makeText(this@FavoriteActivity, "No Records Found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menufavorite, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val favoriteIntent = Intent(this@FavoriteActivity, MainActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.settings -> {
                val settingsIntent = Intent(this@FavoriteActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@FavoriteActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.userFav)
    }

}

