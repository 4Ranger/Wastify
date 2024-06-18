package com.capstonewahwah.wastify.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.adapters.ArticleAdapter
import com.capstonewahwah.wastify.data.local.pref.UserData
import com.capstonewahwah.wastify.data.remote.response.ArticlesItem
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.Source
import com.capstonewahwah.wastify.databinding.FragmentHomeBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel
import com.google.android.material.button.MaterialButton
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
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
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            mainViewModel.getUserDetails(user.token)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            val userData = UserData(
                username = userDetails.login.username,
                email = userDetails.login.email,
                trashScanned = userDetails.login.historyCount,
                points = userDetails.login.historyPoints,
                photoUrl = userDetails.login.photoURL,
                firstBoot = false
            )
            mainViewModel.saveUserData(userData)
        }

        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLoggedIn) {
                Log.d("Token", user.token)

                mainViewModel.getUserData().observe(viewLifecycleOwner) { userData ->
                    Glide.with(requireContext())
                        .load(userData.photoUrl)
                        .into(binding?.ivProfile!!)

                    if (!userData.firstBoot) {
                        binding?.tvUsername?.text = getString(R.string.username_home, userData.username)
                    } else {
                        binding?.tvUsername?.text = getString(R.string.username_home, user.username)
                    }

                    binding?.tvPointDetails?.text = getString(R.string.user_point, userData.points)
                    binding?.tvTrashDetails?.text = getString(R.string.waste_amount, userData.trashScanned)

                    binding?.cvProfile?.setOnClickListener {
                        val extras = FragmentNavigatorExtras(
                            binding?.cvProfile!! to "userProfileDetail",
                            binding?.tvUsername!! to "userNameDetail"
                        )
                        val toProfileFragment = HomeFragmentDirections.actionNavigationHomeToProfileFragment()
                        toProfileFragment.username = userData.username
                        toProfileFragment.token = user.token
                        toProfileFragment.email = userData.email
                        toProfileFragment.photoUrl = userData.photoUrl
                        findNavController().navigate(toProfileFragment, extras)
                    }
                }

                binding?.swipeRefrshLayout?.setOnRefreshListener {
                    binding?.swipeRefrshLayout?.isRefreshing = true
                    mainViewModel.getArticles()
                    mainViewModel.getUserDetails(user.token)
                }
            }
        }

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
                binding?.swipeRefrshLayout?.isRefreshing = false
                homeViewModel.articles.observe(viewLifecycleOwner) { article ->
                    loadArticles(article)
                }
            }

            binding?.cvHelp?.setOnClickListener {

                binding?.scrollView?.isSmoothScrollingEnabled = true
                binding?.scrollView?.scrollToDescendant(binding?.rvArticles!!)

                val targets = ArrayList<Target>()

                val firstRoot = FrameLayout(requireContext())
                val first = layoutInflater.inflate(R.layout.target_layout, firstRoot)
                val tvFirstDetails: TextView = first.findViewById(R.id.tv_spotlight_details)
                val firstTarget = Target.Builder()
                    .setAnchor(binding?.rvArticles!!)
                    .setShape(RoundedRectangle(
                        binding?.rvArticles?.height!!.toFloat(),
                        resources.displayMetrics.widthPixels.toFloat(),
                        20f
                    ))
                    .setOverlay(first)
                    .setOnTargetListener(object : OnTargetListener{
                        override fun onEnded() {

                        }

                        override fun onStarted() {
                            tvFirstDetails.text = getString(R.string.spotlight_first_detail)
                            tvFirstDetails.setPadding(
                                0,
                                0,
                                0,
                                binding?.rvArticles?.height!! * 2
                            )
                        }
                    })
                    .build()

                targets.add(firstTarget)

                val secondRoot = FrameLayout(requireContext())
                val second = layoutInflater.inflate(R.layout.target_layout, secondRoot)
                val tvSecondDetails: TextView = second.findViewById(R.id.tv_spotlight_details)
                val secondTarget = Target.Builder()
                    .setAnchor(requireActivity().findViewById<View>(R.id.navigation_leaderboards))
                    .setShape(Circle(100f))
                    .setOverlay(second)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onEnded() {

                        }

                        override fun onStarted() {
                            tvSecondDetails.text = getString(R.string.second_spotlight_details)
                            tvSecondDetails.setPadding(
                                0,
                                0,
                                0,
                                92 * 3
                            )
                        }
                    })
                    .build()

                targets.add(secondTarget)

                val thirdRoot = FrameLayout(requireContext())
                val third = layoutInflater.inflate(R.layout.target_layout, thirdRoot)
                val tvThirdDetails: TextView = third.findViewById(R.id.tv_spotlight_details)
                val thirdTarget = Target.Builder()
                    .setAnchor(requireActivity().findViewById<View>(R.id.fab))
                    .setShape(Circle(120f))
                    .setOverlay(third)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onEnded() {

                        }

                        override fun onStarted() {
                            tvThirdDetails.text = getString(R.string.third_spotlight_details)
                            tvThirdDetails.setPadding(
                                0,
                                0,
                                0,
                                92 * 3
                            )
                        }
                    })
                    .build()

                targets.add(thirdTarget)

                val fourthRoot = FrameLayout(requireContext())
                val fourth = layoutInflater.inflate(R.layout.target_layout, fourthRoot)
                val tvFourthDetails: TextView = fourth.findViewById(R.id.tv_spotlight_details)
                val fourthTarget = Target.Builder()
                    .setAnchor(requireActivity().findViewById<View>(R.id.navigation_classification))
                    .setShape(Circle(100f))
                    .setOverlay(fourth)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onEnded() {

                        }

                        override fun onStarted() {
                            tvFourthDetails.text = getString(R.string.fourth_spotlight_details)
                            tvFourthDetails.setPadding(
                                0,
                                0,
                                0,
                                92 * 3
                            )
                        }
                    })
                    .build()

                targets.add(fourthTarget)

                val fifthRoot = FrameLayout(requireContext())
                val fifth = layoutInflater.inflate(R.layout.target_layout, fifthRoot)
                val tvFifthDetails: TextView = fifth.findViewById(R.id.tv_spotlight_details)
                val fifthTarget = Target.Builder()
                    .setAnchor(requireActivity().findViewById<View>(R.id.navigation_location))
                    .setShape(Circle(100f))
                    .setOverlay(fifth)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onEnded() {

                        }

                        override fun onStarted() {
                            tvFifthDetails.text = getString(R.string.fifth_spotlight_details)
                            tvFifthDetails.setPadding(
                                0,
                                0,
                                0,
                                92 * 3
                            )
                        }
                    })
                    .build()

                targets.add(fifthTarget)

                val sixthRoot = FrameLayout(requireContext())
                val sixth = layoutInflater.inflate(R.layout.final_spotlight_layout, sixthRoot)
                val sixthTarget = Target.Builder()
                    .setShape(Circle(0f))
                    .setOverlay(sixth)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onEnded() {

                        }

                        override fun onStarted() {

                        }
                    })
                    .build()

                targets.add(sixthTarget)

                val spotlight = Spotlight.Builder(requireActivity())
                    .setTargets(targets)
                    .setBackgroundColorRes(R.color.spotlightBackground)
                    .setDuration(500L)
                    .setAnimation(DecelerateInterpolator(2f))
                    .setOnSpotlightListener(object : OnSpotlightListener {
                        override fun onEnded() {
                            it.isEnabled = true
                        }

                        override fun onStarted() {
                            it.isEnabled = false
                        }
                    })
                    .build()

                spotlight.start()

                val nextTarget = View.OnClickListener { spotlight.next() }
                val closeSpotlight = View.OnClickListener { spotlight.finish() }

                first.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
                second.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
                third.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
                fourth.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
                fifth.findViewById<View>(R.id.btn_next).setOnClickListener(nextTarget)
                sixth.findViewById<View>(R.id.btn_finish).setOnClickListener(closeSpotlight)
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
                mainViewModel.deleteUserData()
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