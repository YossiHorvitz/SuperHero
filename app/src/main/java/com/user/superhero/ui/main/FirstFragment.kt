package com.user.superhero.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.user.superhero.R
import com.user.superhero.adapters.SuperHeroAdapter
import com.user.superhero.databinding.FragmentFirstBinding
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
class FirstFragment : Fragment(R.layout.fragment_first){

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private val suggestionViewModel by viewModels<SuggestionViewModel>()

    private val recyclerViewAdapter = createAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = FragmentFirstBinding.bind(view)

        setupRecyclerView()
        setupListeners()
        observeLiveData()
    }

    private fun createAdapter(): SuperHeroAdapter {
        return SuperHeroAdapter { _, hero ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(hero)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.content.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, getSpanCount())
            adapter = recyclerViewAdapter
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
                recyclerViewAdapter.submitList(it.results)
        })

        searchViewModel.showProgress.observe(viewLifecycleOwner, {
            binding.apply {
                searchInfoCardView?.isVisible = it
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
                    searchViewModel.viewModelScope.launch {
                        searchViewModel.searchHero(query)
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