package com.bobrovskii.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.FragmentHomeBinding
import com.bobrovskii.home.presentation.periodsAdapter.PeriodsAdapter
import com.bobrovskii.home.presentation.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

	private lateinit var binding: FragmentHomeBinding

	private val viewModel: HomeViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentHomeBinding.bind(view)
		initRecyclerView()
		initListeners()
	}

	private fun initRecyclerView() {
		binding.periodsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		val adapterPeriods = PeriodsAdapter()
		viewModel.periodsLiveData.observe(viewLifecycleOwner) {
			adapterPeriods.submitList(it)
		}

		binding.periodsRV.adapter = adapterPeriods
	}

	private fun initListeners() {
		binding.imageButtonExit.setOnClickListener {
			viewModel.goBack()
		}
		binding.imageButtonAdd.setOnClickListener {
			viewModel.openAddExam()
		}
	}
}