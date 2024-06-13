package com.capstonewahwah.wastify.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.adapters.ArticleAdapter
import com.capstonewahwah.wastify.data.remote.response.ArticlesItem
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.Source
import com.capstonewahwah.wastify.databinding.FragmentHomeBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by activityViewModels<MainViewModel> {
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

        homeViewModel.articlesLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                val dummyArticlesData = ArticlesResponse(
                    totalResults = 10,
                    articles = listOf(
                        ArticlesItem(
                            publishedAt = "2023-04-27T12:34:56Z",
                            author = "John Doe",
                            urlToImage = "https://example.com/image1.jpg",
                            description = "This is a dummy description for article 1.",
                            source = Source(
                                id = "example-source",
                                name = "Example Source"
                            ),
                            title = "Dummy Article Title 1",
                            url = "https://example.com/dummy-article-1",
                            content = "This is a dummy content for article 1."
                        ),
                        ArticlesItem(
                            publishedAt = "2023-04-26T09:15:30Z",
                            author = "Jane Smith",
                            urlToImage = "https://example.com/image2.jpg",
                            description = "This is a dummy description for article 2.",
                            source = Source(
                                id = "another-source",
                                name = "Another Source"
                            ),
                            title = "Dummy Article Title 2",
                            url = "https://example.com/dummy-article-2",
                            content = "This is a dummy content for article 2."
                        )
                    ),
                    status = "ok"
                )
                loadArticles(dummyArticlesData)
            } else {
                homeViewModel.articles.observe(viewLifecycleOwner) { article ->
                    loadArticles(article)
                }
            }
        }

        binding?.cvBtnLogout?.setOnClickListener {
            val customLayout = layoutInflater.inflate(R.layout.exit_dialog, null)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(customLayout)

            val yesBtn = customLayout.findViewById<MaterialButton>(R.id.btn_logout_yes)
            val noBtn = customLayout.findViewById<MaterialButton>(R.id.btn_logout_no)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            yesBtn.setOnClickListener {
                mainViewModel.logout()
                alertDialog.dismiss()
            }

            noBtn.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun loadArticles(article: ArticlesResponse) {
        homeViewModel.articlesLoading.observe(viewLifecycleOwner) { isLoading ->
            val adapter = ArticleAdapter(isLoading)
            adapter.submitList(article.articles)
            binding?.rvArticles?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding?.rvArticles?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}