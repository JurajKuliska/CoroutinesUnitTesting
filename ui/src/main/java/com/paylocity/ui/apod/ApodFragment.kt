package com.paylocity.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.paylocity.ui.util.launchWhile
import com.paylocity.unittest.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApodFragment : Fragment() {

	companion object {
		fun newInstance() = ApodFragment()
	}

	private val adapter = ApodAdapter()
	private var _binding: MainFragmentBinding? = null
	private val binding: MainFragmentBinding get() = _binding!!

	private val viewModel: ApodViewModel by viewModel()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = MainFragmentBinding.inflate(inflater, container, false)

		binding.recyclerView.adapter = adapter
		binding.fab.setOnClickListener {
			viewModel.refreshData()
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewLifecycleOwner.launchWhile(Lifecycle.State.RESUMED) {
			viewModel.viewState.collect {
				binding.progressbar.visibility = View.GONE
				when (it) {
					is ApodViewStateLoading -> {
						adapter.submitList(it.list)
						if(it.list.isEmpty()) {
							binding.progressbar.visibility = View.VISIBLE
						}
					}
					is ApodViewStateEmpty -> adapter.submitList(emptyList())
					is ApodViewStateError -> {
						adapter.submitList(it.list)
						Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
					}
					is ApodViewStateSuccess -> adapter.submitList(it.list)
				}
			}
		}
	}

	override fun onDestroyView() {
		binding.recyclerView.adapter = null
		_binding = null
		super.onDestroyView()
	}
}