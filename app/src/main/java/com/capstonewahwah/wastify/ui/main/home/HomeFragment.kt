package com.capstonewahwah.wastify.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonewahwah.wastify.adapters.ArticleAdapter
import com.capstonewahwah.wastify.data.remote.response.ArticlesItem
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.databinding.FragmentHomeBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getArticles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.articles.observe(viewLifecycleOwner) { article ->
            loadArticles(article)
        }
    }

    private fun loadArticles(article: ArticlesResponse) {
        homeViewModel.articlesLoading.observe(viewLifecycleOwner) { isLoading ->
            val adapter = ArticleAdapter(isLoading)
            adapter.submitList(article.articles)
            binding?.rvArticles?.layoutManager = LinearLayoutManager(requireContext())
            binding?.rvArticles?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}