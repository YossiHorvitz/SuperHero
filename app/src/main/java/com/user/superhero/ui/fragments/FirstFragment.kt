package com.user.superhero.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.user.superhero.ui.MainActivity.Companion.isTablet
import com.user.superhero.ui.MainActivity.Companion.isLandscape
import com.user.superhero.R
import com.user.superhero.adapters.SuperHeroAdapter
import com.user.superhero.data.Hero
import com.user.superhero.databinding.FragmentFirstBinding
import com.user.superhero.utils.GridSpacingItemDecoration
import com.user.superhero.viewmodel.HeroViewModel
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.launch

class FirstFragment : Fragment(R.layout.fragment_first){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HeroViewModel>()

    private var list: List<Hero> = emptyList()
    private val adapter = createAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = FragmentFirstBinding.bind(view)

        setupRecyclerView()

        viewModel.list.observe(viewLifecycleOwner, {
            val result = if (it.response == "success") it.results else emptyList()
            list = result
            adapter.list = result

            adapter.notifyDataSetChanged()

            if (list.isEmpty())
                Snackbar.make(binding.root, getString(R.string.no_results), Snackbar.LENGTH_LONG).show()
        })

        viewModel.showProgress.observe(viewLifecycleOwner, {
            if (it) {
                adapter.list = emptyList()
                adapter.notifyDataSetChanged()

                loading_text_view.text = getString(R.string.loading)
                loading_text_view.visibility = View.VISIBLE
                loading_image.visibility = View.VISIBLE
            } else {
                loading_text_view.visibility = View.GONE
                loading_image.visibility = View.GONE
            }
        })
    }

    private fun createAdapter(): SuperHeroAdapter {
        return SuperHeroAdapter(list) { _, hero ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(hero)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(activity, getSpanCount())
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(GridSpacingItemDecoration(getSpanCount(), spacing = 25, includeEdge = true))
        }
    }

    private fun getSpanCount(): Int {
        return if (isTablet)
            if (isLandscape) 4 else 3
        else if (isLandscape) 3 else 2
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.viewModelScope.launch {
                        viewModel.searchHero(query)
                    }
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}