package com.user.superhero.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.user.superhero.MainActivity.Companion.isTablet
import com.user.superhero.MainActivity.Companion.isLandscape
import com.user.superhero.R
import com.user.superhero.adapter.SuperHeroAdapter
import com.user.superhero.api.APIResponse
import com.user.superhero.databinding.FragmentFirstBinding
import com.user.superhero.ui.GridSpacingItemDecoration
import com.user.superhero.view_model.HeroViewModel
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment(R.layout.fragment_first), SuperHeroAdapter.OnItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HeroViewModel>()

    private var list: List<APIResponse.Results> = emptyList()
    private val adapter = SuperHeroAdapter(list, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = FragmentFirstBinding.bind(view)

        binding.apply {

            val spanCount =
                if (isTablet) {
                    if (isLandscape) 4 else 3
                } else if (isLandscape) 3 else 2

            recyclerView.layoutManager = GridLayoutManager(activity, spanCount)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing = 25, includeEdge = true))

            viewModel.list.observe(viewLifecycleOwner, {
                val result = if (it.response == "success") it.results else emptyList()
                list = result
                adapter.list = result

                adapter.notifyDataSetChanged()

                if (list.isEmpty()) {
                    Snackbar.make(root, getString(R.string.no_results), Snackbar.LENGTH_LONG).show()
                }
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
    }

    override fun onItemClick(position: Int) {
        val hero = list[position]
        val bundle = bundleOf("hero" to hero)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    viewModel.searchHero(query)
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