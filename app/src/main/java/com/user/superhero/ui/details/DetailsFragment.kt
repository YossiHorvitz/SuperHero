package com.user.superhero.ui.details
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.user.superhero.R
import com.user.superhero.adapters.ViewPagerAdapter
import com.user.superhero.data.Hero
import com.user.superhero.utils.AppUtils.shareHeroDetails
import com.user.superhero.utils.Constants.GIT_URL
import com.user.superhero.utils.extensions.loadImage
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var hero: Hero
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initHeroArguments()
        setupViewPager()
        setupTabLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(0, 0, 0, getString(R.string.action_git))
        menu.add(0, 1, 0, getString(R.string.action_share))?.setIcon(R.drawable.ic_share)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            0 -> { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GIT_URL)))
                true }
            1 -> { context?.let { shareHeroDetails(it, hero) }
                true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initHeroArguments() {
        hero = args.hero.apply {
            (activity as AppCompatActivity).supportActionBar?.title = name
            hero_image.loadImage(activity as AppCompatActivity, image.url)
        }
    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, hero, itemsCount = 3)
        view_pager.adapter = viewPagerAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, view_pager) { tab, position ->
            val heroNameArray = resources.getStringArray(R.array.hero_attr_names)
            tab.text = heroNameArray[position].substringBefore(' ')
        }.attach()
    }
}