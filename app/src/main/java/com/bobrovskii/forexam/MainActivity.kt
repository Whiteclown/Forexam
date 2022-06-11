package com.bobrovskii.forexam

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.bobrovskii.core.IOnBackPressed
import com.bobrovskii.forexam.navigation.Navigator
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

	override fun onBackPressed() {
		val fragment = supportFragmentManager.currentNavigationFragment
		(fragment as? IOnBackPressed)?.onBackPressed()?.let {
			if (it) {
				super.onBackPressed()
			}
		}
	}
}

val FragmentManager.currentNavigationFragment: Fragment?
	get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()