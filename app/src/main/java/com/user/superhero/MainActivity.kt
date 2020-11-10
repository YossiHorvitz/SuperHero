package com.user.superhero

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.user.superhero.fragment.DetailsFragment.Companion.imageName
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        var isTablet = false
        var isLandscape = false
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        isTablet = resources.getBoolean(R.bool.is_tablet)
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        deleteTempFile()
        super.onBackPressed()
    }

    /**
     * delete temp file if left behind
     * @see com.user.superhero.fragment.DetailsFragment.imageName
     * */
    private fun deleteTempFile() {
        File(getExternalFilesDir(null), imageName).delete()
    }
}