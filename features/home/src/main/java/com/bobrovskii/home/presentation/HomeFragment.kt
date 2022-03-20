package com.bobrovskii.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

	@Inject
	lateinit var navigation: HomeNavigation

	private lateinit var binding: FragmentHomeBinding

	private val viewModel: HomeViewModel by viewModels()

	private lateinit var listOfExams: RecyclerView

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentHomeBinding.bind(view)
		listOfExams = binding.listOfExams
		listOfExams.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		listOfExams.adapter = Adapter(listOf(Name("Proga", "fda"), Name("OS", "nad")))
		initListeners()
	}

	private fun initListeners() {

	}
}