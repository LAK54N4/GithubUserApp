package com.laksana.githubuser.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.laksana.githubuser.MainActivity
import com.laksana.githubuser.R
import com.laksana.githubuser.adapter.SectionsPagerAdapter
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.laksana.githubuser.database.DatabaseContract.UserColumns.Companion._ID
import com.laksana.githubuser.fragment.FollowersFragment
import com.laksana.githubuser.fragment.FollowingFragment
import com.laksana.githubuser.fragment.FollowingFragment.Companion.ARG_PARAM1
import com.laksana.githubuser.model.User
import com.laksana.githubuser.model.UserFavorite
import com.laksana.githubuser.viewModel.DetailViewModel
import kotlinx.android.synthetic.main.details_user.*

class ViewDetailsActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "detail"
        const val EXTRA_FAVORITE = "favorite"
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    private var statusFavorite = false
    private lateinit var uriID: Uri
    private lateinit var detailViewModel : DetailViewModel
    private lateinit var viewPager2: ViewPager2
    private lateinit var  pagerAdapter: FragmentStateAdapter

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_user)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = "Detail User"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<User>(EXTRA_DETAIL)
        if (data != null) {
            val dataUser = data.username
            showDetailUser(data)
            showTabs(dataUser)
            showFollowers(dataUser)
            showFollowing(dataUser)
        } else {
            val dataFav = intent.getParcelableExtra<UserFavorite>(EXTRA_FAVORITE)
            if (dataFav != null) {
                val dataUser = dataFav.username
                showDetailUserFav(dataFav)
                showTabs(dataUser)
                showFollowers(dataUser)
                showFollowing(dataUser)
            }
        }

        detailViewModel.getDetailUser().observe(this, { it ->
            Log.d("cek", it.toString())

            if (it != null) {
                uriID = Uri.parse("$CONTENT_URI/" + it.id)

                val cursor: Cursor? = contentResolver.query(uriID, null, null, null,null)

                if (cursor?.count == 1) {
                    Toast.makeText(applicationContext, "Already Exist!", Toast.LENGTH_SHORT).show()
                    statusFavorite = true
                    setStatusFavorite(statusFavorite)

                    favoriteButton.setOnClickListener {
                        detailViewModel.getDetailUser().observe(this, {
                            contentResolver.delete(uriID, null, null)

                            statusFavorite = !statusFavorite
                            setStatusFavorite(statusFavorite)
                            Toast.makeText(this, "Success remove favorite", Toast.LENGTH_SHORT).show()

                            val intentDelete = Intent(this, FavoriteActivity::class.java)
                            startActivity(intentDelete)
                        })
                    }

                } else {
                    Toast.makeText(applicationContext, "Not Exist!", Toast.LENGTH_SHORT).show()
                    statusFavorite = false
                    setStatusFavorite(statusFavorite)
                    favoriteButton.setOnClickListener {
                        if (!statusFavorite) {
                            detailViewModel.getDetailUser().observe(this, {
                                if (it != null) {

                                    val contentValues = ContentValues().apply {
                                        put(_ID, it.id)
                                        put(COLUMN_NAME_USERNAME, it.username)
                                        put(COLUMN_NAME_AVATAR_URL, it.avatar)
                                    }
                                    contentResolver.insert(CONTENT_URI, contentValues)
                                    Toast.makeText(this, "Successfully added to favorite", Toast.LENGTH_SHORT).show()
                                    val intentSave = Intent(this, FavoriteActivity::class.java)
                                    startActivity(intentSave)
                                    finish()
                                }
                            })
                            statusFavorite = !statusFavorite
                            setStatusFavorite(statusFavorite)
                        }
                    }
                }
            }
        })
    }

    private fun showDetailUser(data: User) {
        val detailsUsername: TextView = findViewById(R.id.details_username)
        val location: TextView = findViewById(R.id.tvLocation)
        val repos: TextView = findViewById(R.id.tvTotalRepos)
        val totalFollowers: TextView = findViewById(R.id.tvTotalFollowers)
        val totalFollowing: TextView = findViewById(R.id.tvTotalFollowing)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        data.username.let { detailViewModel.showDetailsUser(it) }
        detailViewModel.getDetailUser().observe(this, {
            if (it != null) {
                Glide.with(this).load(data.avatar)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(details_img_avatar)

                detailsUsername.text = data.username
                location.text = it.location
                repos.text = it.repository
                totalFollowers.text = it.followers
                totalFollowing.text = it.following
            }
        })
    }

    private fun showDetailUserFav(dataFav: UserFavorite) {
        val detailsUsername: TextView = findViewById(R.id.details_username)
        val location: TextView = findViewById(R.id.tvLocation)
        val repos: TextView = findViewById(R.id.tvTotalRepos)
        val totalFollowers: TextView = findViewById(R.id.tvTotalFollowers)
        val totalFollowing: TextView = findViewById(R.id.tvTotalFollowing)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)
        dataFav.username.let { detailViewModel.showDetailsUser(it) }
        detailViewModel.getDetailUser().observe(this, {
            if (it != null) {
                Glide.with(this).load(dataFav.avatar)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(details_img_avatar)

                detailsUsername.text = dataFav.username
                location.text = it.location
                repos.text = it.repository
                totalFollowers.text = it.followers
                totalFollowing.text = it.following
            }
        })
    }

    private fun showTabs(dataUser: String) {
        viewPager2 = findViewById(R.id.viewpager)
        pagerAdapter = SectionsPagerAdapter(this, dataUser)
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = when(position) {
                0-> resources.getString(R.string.tab_followers)
                else -> resources.getString(R.string.tab_following)
            }
        }.attach()
    }

    private fun showFollowers(dataUser: String){
        val fragmentFollowers: FollowersFragment = FollowersFragment.newInstance(dataUser)
        val bundle = Bundle()
        bundle.putString(FollowersFragment.NAME_FOLLOWER, dataUser)
        fragmentFollowers.arguments = bundle
    }

    private fun showFollowing(dataUser: String){
        val bundle = Bundle()
        val fragmentFollowing: FollowingFragment = FollowingFragment.newInstance(dataUser)
        bundle.putString(ARG_PARAM1, dataUser)
        fragmentFollowing.arguments = bundle
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite_user -> {
                val favoriteIntent = Intent(this@ViewDetailsActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.settings -> {
                val settingsIntent = Intent(this@ViewDetailsActivity, SettingsActivity::class.java)
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
        val i = Intent(this@ViewDetailsActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        finish()
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if(statusFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
            favoriteButton.solidColor
        }
        else {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}