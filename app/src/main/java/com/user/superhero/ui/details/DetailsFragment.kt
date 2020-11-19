package com.user.superhero.ui.details

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import com.user.superhero.R
import com.user.superhero.adapters.ViewPagerAdapter
import com.user.superhero.data.Hero
import com.user.superhero.utils.Constants.GIT_URL
import com.user.superhero.utils.Constants.IMAGE_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var hero: Hero
    private val args = navArgs<DetailsFragmentArgs>()

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
            0 -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(GIT_URL)))
                true
            }

            1 -> {
                shareHeroDetails(hero)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initHeroArguments() {
        hero = args.value.hero!!.apply {
            (activity as AppCompatActivity).supportActionBar?.title = name
            loadImage(image.url)
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

    private fun loadImage(url: String) {
        hero_image.apply {
            Glide.with(this@DetailsFragment)
                .load(url)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.toBitmap().let {
                            saveImage(it)
                            applyPalette(it)
                        }
                        return false
                    }
                    /** change toolbar and status bar color */
                    private fun applyPalette(bitmap: Bitmap?) {
                        bitmap?.let {
                            Palette.from(it).generate { palette ->
                                val window = (activity as AppCompatActivity).window

                                val color = when (palette?.getDarkVibrantColor(R.attr.colorAccent)) {
                                    // this color not affect the color result well
                                    2130968748 -> ContextCompat.getColor(requireContext(), R.color.purple_500)
                                    else -> palette?.getDarkVibrantColor(R.attr.colorAccent)
                                }

                                ValueAnimator.ofObject(ArgbEvaluator(), window.statusBarColor, color)
                                    .apply {
                                        addUpdateListener { animator ->
                                            (activity as AppCompatActivity).toolbar.setBackgroundColor(animator.animatedValue as Int)
                                            window.statusBarColor = animator.animatedValue as Int
                                        }
                                        start()
                                    }
                            }
                        }
                    }
                }).into(this)
        }
    }

    /** save the image to internal storage in order to share it later */
    private fun saveImage(bitmap: Bitmap?) {
        val file = File(requireContext().getExternalFilesDir(null), IMAGE_NAME)
        file.createNewFile()

        try {
            FileOutputStream(file).use { out ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** share a hero image and some other information */
    private fun shareHeroDetails(hero: Hero) {
        val file = File(requireContext().getExternalFilesDir(null), IMAGE_NAME)

        if (file.exists()) {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${context?.applicationContext?.packageName}.provider",
                file
            )

            val details = StringBuilder()
                .append(String.format(getString(R.string.hero_name), hero.name))
                .append(String.format(getString(R.string.gender), hero.appearance.gender))
                .append(String.format(getString(R.string.strength), hero.powerstats.strength))

            val shareIntent = Intent().apply {
                type = "*/*"
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, details.toString())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                startActivity(Intent.createChooser(shareIntent, null))
            } catch (ignore: Exception) {
            }
        }
    }
}