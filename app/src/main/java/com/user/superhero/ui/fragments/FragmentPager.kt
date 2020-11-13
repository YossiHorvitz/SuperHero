package com.user.superhero.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.user.superhero.R
import com.user.superhero.data.APIResponse
import kotlinx.android.synthetic.main.fragment_pager.*

class FragmentPager : Fragment(R.layout.fragment_pager) {

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int, hero: APIResponse.Results): Fragment {
            val fragment = FragmentPager()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putParcelable("hero", hero)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val position = requireArguments().getInt(ARG_POSITION)
        val hero = requireArguments().getParcelable<APIResponse.Results>("hero")!!

        details_text_view.apply {
            when (position) {
                0 -> { text = getString(
                        R.string.bio,
                        if (hero.biography.full_name.isNullOrBlank()) "-" else hero.biography.full_name,
                        hero.biography.alter_egos,
                        if (hero.biography.place_of_birth == "null") "-" else hero.biography.place_of_birth) }

                1 -> { text = getString(
                        R.string.appearance,
                        hero.appearance.gender,
                        hero.appearance.height.joinToString(),
                        hero.appearance.weight.joinToString()) }

                2 -> { text = getString(R.string.power,
                        if (hero.powerstats.power == "null") "-" else hero.powerstats.power,
                        if (hero.powerstats.speed == "null") "-" else hero.powerstats.speed,
                        if (hero.powerstats.strength == "null") "-" else hero.powerstats.strength) }
            }
        }
    }
}