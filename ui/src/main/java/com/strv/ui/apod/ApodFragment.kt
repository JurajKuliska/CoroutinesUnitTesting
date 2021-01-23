package com.strv.ui.apod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strv.ui.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApodFragment : Fragment() {

	companion object {
		fun newInstance() = ApodFragment()
	}

	private val viewModel: ApodViewModel by viewModel()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.main_fragment, container, false)
	}
}