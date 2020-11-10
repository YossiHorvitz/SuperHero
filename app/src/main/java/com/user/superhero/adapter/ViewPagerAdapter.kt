package com.user.superhero.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.user.superhero.api.APIResponse
import com.user.superhero.fragment.FragmentPager

class ViewPagerAdapter(
    activity: AppCompatActivity,
    private val hero: APIResponse.Results,
    private val itemsCount: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentPager.getInstance(position, hero)
    }
}