package tech.nekonyan.githubapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.databinding.FragmentListBinding
import tech.nekonyan.githubapp.ui.adapter.GithubUserAdapter
import tech.nekonyan.githubapp.ui.viewmodel.MainViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.Extensions.hideKeyboard
import tech.nekonyan.githubapp.util.network.Resource

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: GithubUserAdapter

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        binding.state = Constants.LOADING

        userAdapter = GithubUserAdapter(requireContext())

        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.itemAnimator = DefaultItemAnimator()
        binding.rvUser.adapter = userAdapter

        binding.swipeLayout.setOnRefreshListener {
            viewModel.searchUser()
        }

        viewModel.dataList.observe(viewLifecycleOwner) { resource ->
            binding.state = resource.state
            binding.query = viewModel.query

            if (resource !is Resource.Loading<*>) {
                binding.swipeLayout.isRefreshing = false
                binding.shimmerLayout.container.hideShimmer()
            } else {
                binding.shimmerLayout.container.showShimmer(true)
            }

            if (resource is Resource.Success<*>) {
                resource.data?.let { data ->
                    userAdapter.submitList(data)
                }
            }
        }

        binding.inputSearch.doOnTextChanged { query, _, _, _ ->
            handler.removeCallbacksAndMessages(null)
            viewModel.query = query.toString().trim()
            handler.postDelayed({
                viewModel.searchUser()
                binding.inputSearch.hideKeyboard()
            }, Constants.SEARCH_DELAY)
        }

        binding.errorLayout.retryButton.setOnClickListener {
            viewModel.searchUser()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}