package com.user.superhero.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import com.user.superhero.R
import com.user.superhero.adapter.ViewPagerAdapter
import com.user.superhero.api.APIResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DetailsFragment : Fragment(R.layout.fragment_details) {

    companion object {
        /**
         * default image name saved to a file
         * */
        const val imageName: String = "temp.jpg"
    }

    lateinit var hero: APIResponse.Results

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hero = arguments?.getParcelable("hero")!!
        (activity as AppCompatActivity).supportActionBar?.title = hero.name
        loadImage(hero.image.url)

        val heroNameArray = resources.getStringArray(R.array.hero_attr_names)
        val viewPagerAdapter = ViewPagerAdapter(activity as AppCompatActivity, hero, 3)
        view_pager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, view_pager) { tab, position ->
            tab.text = heroNameArray[position].substringBefore(' ')
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(0, 0, 0, getString(R.string.action_git))
        menu.add(0, 1, 0, getString(R.string.action_share))?.setIcon(R.drawable.ic_share)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            0 -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/YossiHorvitz/SuperHero")))
                true
            }

            1 -> {
                shareHeroDetails(hero)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val bitmap = resource?.toBitmap()
                    bitmap?.let { applyPalette(it) }
                    saveImage(resource)
                    return false
                }

                /**
                 * change toolbar and status bar color
                 * */
                private fun applyPalette(bitmap: Bitmap) {
                    Palette.from(bitmap).generate { palette ->
                        val toolbar = (activity as AppCompatActivity).toolbar
                        val window = (activity as AppCompatActivity).window

                        var color = palette?.getDarkVibrantColor(R.attr.colorAccent)

                        // this color not affect the color result well
                        if (color == 2130968748)
                            color = ContextCompat.getColor(requireContext(), R.color.purple_500)

                        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), window.statusBarColor, color)

                        colorAnimation.addUpdateListener { animator ->
                            toolbar.setBackgroundColor(animator.animatedValue as Int)
                            window.statusBarColor = animator.animatedValue as Int
                        }
                        colorAnimation.start()
                    }
                }

            }).into(hero_image)
    }

    /**
     * save the image to internal storage in order to share it later
     * */
    private fun saveImage(resource: Drawable?) {
        val bitmap = resource?.toBitmap()

        val file = File(requireContext().getExternalFilesDir(null), imageName)
        file.createNewFile()

        val os: OutputStream
        try {
            os = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, os)
            os.flush()
            os.close()
        } catch (ignore: Exception) {
        }
    }

    /**
     * share a hero image and some other information
     * */
    private fun shareHeroDetails(hero: APIResponse.Results) {
        val file = File(requireContext().getExternalFilesDir(null), imageName)

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