package com.fouranger.wastify.ui.main.leaderboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.fouranger.wastify.adapters.LeaderboardsAdapter
import com.fouranger.wastify.data.remote.response.CreatedAt1
import com.fouranger.wastify.data.remote.response.LeaderboardItem
import com.fouranger.wastify.data.remote.response.LeaderboardsResponse
import com.fouranger.wastify.databinding.FragmentLeaderboardsBinding
import com.fouranger.wastify.helper.ViewModelFactory

class LeaderboardsFragment : Fragment() {

    private var _binding: FragmentLeaderboardsBinding? = null
    private val binding get() = _binding

    private val leaderboardsViewModel by viewModels<LeaderboardsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    override fun onResume() {
        super.onResume()
        leaderboardsViewModel.getLeaderboards()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeaderboardsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvLeaderboards?.layoutManager = LinearLayoutManager(requireContext())

        leaderboardsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding?.tvNoData?.visibility = View.GONE
                val dummyResponse = LeaderboardsResponse(
                    leaderboard = listOf(
                        LeaderboardItem(
                            uid = "abc123def456",
                            createdAt = CreatedAt1(
                                nanoseconds = 500000000,
                                seconds = 1618214711
                            ),
                            historyCount = 3,
                            historyPoints = 30,
                            email = "john@example.com",
                            username = "JohnDoe",
                            profilePhotoUrl = "https://example.com/profile.jpg",
                            rank = 1,
                            history = listOf("game1", "game2", "game3")
                        ),
                        LeaderboardItem(
                            uid = "ghi789jkl012",
                            createdAt = CreatedAt1(
                                nanoseconds = 565000000,
                                seconds = 1618246483
                            ),
                            historyCount = 2,
                            historyPoints = 69,
                            email = "jane@example.com",
                            username = "JaneDoe",
                            profilePhotoUrl = "",
                            rank = 2,
                            history = listOf("game4", "game5")
                        ),
                        LeaderboardItem(
                            uid = "mno345pqr678",
                            createdAt = CreatedAt1(
                                nanoseconds = 680000000,
                                seconds = 1618348343
                            ),
                            historyCount = 1,
                            historyPoints = 64,
                            email = "bob@example.com",
                            username = "BobSmith",
                            profilePhotoUrl = "",
                            rank = 3,
                            history = listOf("game6")
                        )
                    ),
                    error = false,
                    message = "Leaderboard retrieved successfully"
                )
                loadLeaderboard(dummyResponse)
            } else {
                leaderboardsViewModel.leaderboards.observe(viewLifecycleOwner) { leaderboards ->
                    loadLeaderboard(leaderboards)
                    if (leaderboards.error) {
                        binding?.tvNoData?.visibility = View.VISIBLE
                    } else {
                        binding?.tvNoData?.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun loadLeaderboard(leaderboard: LeaderboardsResponse) {
        leaderboardsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            val adapter = LeaderboardsAdapter(isLoading)
            adapter.submitList(leaderboard.leaderboard)
            binding?.rvLeaderboards?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}