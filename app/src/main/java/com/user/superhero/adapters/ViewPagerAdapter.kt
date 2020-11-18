package com.user.superhero.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.user.superhero.data.Hero
import com.user.superhero.ui.fragments.FragmentPager

class ViewPagerAdapter(
    activity: AppCompatActivity,
    private val hero: Hero,
    private val itemsCount: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentPager.getInstance(position, hero)
    }
}