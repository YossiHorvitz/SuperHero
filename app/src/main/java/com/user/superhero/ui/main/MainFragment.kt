package com.user.superhero.ui.main

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
import com.user.superhero.R
import com.user.superhero.adapters.SuperHeroAdapter
import com.user.superhero.databinding.FragmentMainBinding
import com.user.superhero.ui.MainActivity.Companion.isLandscape
import com.user.superhero.ui.MainActivity.Companion.isTablet
import com.user.superhero.utils.GridSpacingItemDecoration
import com.user.superhero.utils.extensions.dpToPx
import com.user.superhero.utils.extensions.hide
import com.user.superhero.viewmodel.SearchViewModel
import com.user.superhero.viewmodel.SuggestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main){

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private val suggestionViewModel: SuggestionViewModel by viewModels()

    private val adapter = createAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = FragmentMainBinding.bind(view)

        setupRecyclerView()
        setupListeners()
        observeLiveData()
    }

    private fun createAdapter(): SuperHeroAdapter {
        return SuperHeroAdapter { _, hero ->
            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(hero)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.content.recyclerView.apply {
            layoutManager = GridLayoutManager(context, getSpanCount())
            adapter = this@MainFragment.adapter
            setHasFixedSize(true)
            addItemDecoration(GridSpacingItemDecoration(getSpanCount(), spacing = 10.dpToPx(), includeEdge = true))
        }
    }

    private fun setupListeners() {
        binding.suggestionButton.setOnClickListener {
            suggestionViewModel.viewModelScope.launch {
                suggestionViewModel.getHeroById(Random.nextInt(731).toString())
            }
        }
    }

    private fun observeLiveData() {
        searchViewModel.list.observe(viewLifecycleOwner, {
            if (it.results.isNullOrEmpty())
                Snackbar.make(binding.root, getString(R.string.no_results), Snackbar.LENGTH_LONG).show()
            else
                adapter.submitList(it.results)
        })

        searchViewModel.showProgress.observe(viewLifecycleOwner, {
            binding.apply {
                content.recyclerView.animate().alpha(if (it) 0.15f else 1f).setDuration(500).start()
                loadingLayout.animate().alpha(if (it) 1f else 0f).setDuration(500).start()
                loadingTextView.text = getString(R.string.loading)
                suggestionButton.hide()
            }
        })

        suggestionViewModel.list.observe(viewLifecycleOwner, {
            it.isSuggestion = true
            // do nothing yet
        })
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
                    searchView.clearFocus()
                    searchViewModel.viewModelScope.launch {
                        searchViewModel.searchHero(query)
                    }
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