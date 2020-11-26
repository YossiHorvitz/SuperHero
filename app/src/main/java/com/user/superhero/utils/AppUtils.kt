package com.user.superhero.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.palette.graphics.Palette
import com.user.superhero.R
import com.user.superhero.data.Hero
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object AppUtils {

    /** save the image to internal storage in order to share it later */
    fun saveImage(context: Context, bitmap: Bitmap) {
        val file = File(context.getExternalFilesDir(null), Constants.IMAGE_NAME)
        file.createNewFile()

        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** change toolbar and status bar color */
    fun applyPalette(activity: AppCompatActivity, bitmap: Bitmap) {
        bitmap.let {
            Palette.from(it).generate { palette ->
                val window = activity.window

                val color = when (palette?.getDarkVibrantColor(R.attr.colorAccent)) {
                    // this color not affect the color result well
                    2130968748 -> ContextCompat.getColor(activity, R.color.purple_500)
                    else -> palette?.getDarkVibrantColor(R.attr.colorAccent)
                }

                ValueAnimator.ofObject(ArgbEvaluator(), window.statusBarColor, color)
                    .apply {
                        addUpdateListener { animator ->
                            activity.toolbar.setBackgroundColor(animator.animatedValue as Int)
                            window.statusBarColor = animator.animatedValue as Int
                        }
                        start()
                    }
            }
        }
    }

    /** share a hero image and some other information */
    fun shareHeroDetails(context: Context, hero: Hero) {
        val file = File(context.getExternalFilesDir(null), Constants.IMAGE_NAME)

        if (file.exists()) {
            val uri = FileProvider.getUriForFile(context, "${context.applicationContext?.packageName}.provider", file)

            val details = StringBuilder()
                .append(String.format(context.getString(R.string.hero_name), hero.name))
                .append(String.format(context.getString(R.string.gender), hero.appearance.gender))
                .append(String.format(context.getString(R.string.strength), hero.powerstats.strength))

            val shareIntent = Intent().apply {
                type = "*/*"
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, details.toString())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(Intent.createChooser(shareIntent, null))
            } catch (ignore: Exception) {
            }
        }
    }
}