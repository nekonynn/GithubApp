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
import tech.nekonyan.githubapp.databinding.FragmentFavoriteBinding
import tech.nekonyan.githubapp.ui.adapter.GithubUserAdapter
import tech.nekonyan.githubapp.ui.viewmodel.MainViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.Extensions.hideKeyboard
import tech.nekonyan.githubapp.util.network.Resource

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: GithubUserAdapter

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.state = Constants.LOADING

        userAdapter = GithubUserAdapter(requireContext(), true)

        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.itemAnimator = DefaultItemAnimator()
        binding.rvUser.adapter = userAdapter

        binding.swipeLayout.setOnRefreshListener {
            viewModel.searchFavoriteUser()
        }

        viewModel.favoriteDataList.observe(viewLifecycleOwner) { resource ->
            binding.state = resource.state
            binding.query = viewModel.favoriteQuery

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
            viewModel.favoriteQuery = query.toString().trim()
            handler.postDelayed({
                viewModel.searchFavoriteUser()
                binding.inputSearch.hideKeyboard()
            }, Constants.SEARCH_DELAY)
        }

        binding.errorLayout.retryButton.setOnClickListener {
            viewModel.searchFavoriteUser()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchFavoriteUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}