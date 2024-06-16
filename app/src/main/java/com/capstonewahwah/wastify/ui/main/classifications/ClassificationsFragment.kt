package com.capstonewahwah.wastify.ui.main.classifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Fade
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.adapters.HistoryAdapter
import com.capstonewahwah.wastify.data.remote.response.HistoryResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.data.remote.response.Prediction
import com.capstonewahwah.wastify.data.remote.response.Recommendations1
import com.capstonewahwah.wastify.data.remote.response.Timestamp
import com.capstonewahwah.wastify.databinding.FragmentClassificationsBinding
import com.capstonewahwah.wastify.helper.ViewModelFactory
import com.capstonewahwah.wastify.ui.main.MainViewModel

class ClassificationsFragment : Fragment() {
    private var _binding: FragmentClassificationsBinding? = null
    private val binding get() = _binding

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val classificationsViewModel by viewModels<ClassificationsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClassificationsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            classificationsViewModel.getHistories(user.token)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvWaste?.layoutManager = GridLayoutManager(requireContext(), 2)

        classificationsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                val dummyResponse = listOf(
                    HistoryResponseItem(
                        id = "abcd1234",
                        imageUrl = "https://example.com/image.jpg",
                        prediction = Prediction(
                            class0 = 0.1,
                            class2 = 0.2,
                            class1 = 0.15,
                            class4 = 0.25,
                            class3 = 0.1,
                            class5 = 0.2
                        ),
                        predictedClass = "plastik",
                        recommendations = Recommendations1(
                            reduce = "Reduce plastic usage by buying in bulk or larger packages.",
                            reuse = "Reuse plastic bottles, containers, and bags for storage or other purposes.",
                            refuse = "Refuse single-use plastics. Use reusable shopping bags and refillable water bottles.",
                            rot = "Plastic cannot be composted, but try to choose biodegradable products when possible.",
                            repurpose = "Get creative and repurpose plastic bottles into planters or crafts.",
                            recycle = "Separate plastics by type and recycle them through your local recycling program."
                        ),
                        timestamp = Timestamp(
                            nanoseconds = 123456789,
                            seconds = 1618928712
                        )
                    ),
                    HistoryResponseItem(
                        id = "abcd1234",
                        imageUrl = "https://example.com/image.jpg",
                        prediction = Prediction(
                            class0 = 0.1,
                            class2 = 0.2,
                            class1 = 0.15,
                            class4 = 0.25,
                            class3 = 0.1,
                            class5 = 0.2
                        ),
                        predictedClass = "plastik",
                        recommendations = Recommendations1(
                            reduce = "Reduce plastic usage by buying in bulk or larger packages.",
                            reuse = "Reuse plastic bottles, containers, and bags for storage or other purposes.",
                            refuse = "Refuse single-use plastics. Use reusable shopping bags and refillable water bottles.",
                            rot = "Plastic cannot be composted, but try to choose biodegradable products when possible.",
                            repurpose = "Get creative and repurpose plastic bottles into planters or crafts.",
                            recycle = "Separate plastics by type and recycle them through your local recycling program."
                        ),
                        timestamp = Timestamp(
                            nanoseconds = 123456789,
                            seconds = 1618928712
                        )
                    ),
                    HistoryResponseItem(
                        id = "abcd1234",
                        imageUrl = "https://example.com/image.jpg",
                        prediction = Prediction(
                            class0 = 0.1,
                            class2 = 0.2,
                            class1 = 0.15,
                            class4 = 0.25,
                            class3 = 0.1,
                            class5 = 0.2
                        ),
                        predictedClass = "plastik",
                        recommendations = Recommendations1(
                            reduce = "Reduce plastic usage by buying in bulk or larger packages.",
                            reuse = "Reuse plastic bottles, containers, and bags for storage or other purposes.",
                            refuse = "Refuse single-use plastics. Use reusable shopping bags and refillable water bottles.",
                            rot = "Plastic cannot be composted, but try to choose biodegradable products when possible.",
                            repurpose = "Get creative and repurpose plastic bottles into planters or crafts.",
                            recycle = "Separate plastics by type and recycle them through your local recycling program."
                        ),
                        timestamp = Timestamp(
                            nanoseconds = 123456789,
                            seconds = 1618928712
                        )
                    ),
                    HistoryResponseItem(
                        id = "abcd1234",
                        imageUrl = "https://example.com/image.jpg",
                        prediction = Prediction(
                            class0 = 0.1,
                            class2 = 0.2,
                            class1 = 0.15,
                            class4 = 0.25,
                            class3 = 0.1,
                            class5 = 0.2
                        ),
                        predictedClass = "plastik",
                        recommendations = Recommendations1(
                            reduce = "Reduce plastic usage by buying in bulk or larger packages.",
                            reuse = "Reuse plastic bottles, containers, and bags for storage or other purposes.",
                            refuse = "Refuse single-use plastics. Use reusable shopping bags and refillable water bottles.",
                            rot = "Plastic cannot be composted, but try to choose biodegradable products when possible.",
                            repurpose = "Get creative and repurpose plastic bottles into planters or crafts.",
                            recycle = "Separate plastics by type and recycle them through your local recycling program."
                        ),
                        timestamp = Timestamp(
                            nanoseconds = 123456789,
                            seconds = 1618928712
                        )
                    )
                )
                loadHistory(dummyResponse)
            } else {
                classificationsViewModel.histories.observe(viewLifecycleOwner) { histories ->
                    loadHistory(histories)
                }
            }
        }
    }

    private fun loadHistory(history: List<HistoryResponseItem>) {
        classificationsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            val adapter = HistoryAdapter(isLoading)
            adapter.submitList(history.reversed())
            binding?.rvWaste?.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}