package com.laksana.consumerapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.laksana.consumerapp.fragment.FollowersFragment
import com.laksana.consumerapp.fragment.FollowingFragment

class SectionsPagerAdapter(fa: FragmentActivity, var username: String): FragmentStateAdapter(fa) {
    companion object {
        const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FollowersFragment.newInstance(username)
            else -> FollowingFragment.newInstance(username)

        }
    }
}