package com.bobrovskii.forexam

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bobrovskii.forexam.navigation.Navigator
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	@Inject
	lateinit var navigator: Navigator

	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		viewModel
	}

	override fun onResume() {
		super.onResume()
		navigator.bind(findNavController(R.id.host_global))
	}
}