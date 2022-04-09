package tech.nekonyan.githubapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.databinding.FragmentFollowersBinding
import tech.nekonyan.githubapp.ui.adapter.GithubUserAdapter
import tech.nekonyan.githubapp.ui.viewmodel.DetailViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.network.Resource

class ListFollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private lateinit var userAdapter: GithubUserAdapter

    private val intent: Intent? by lazy {
        requireActivity().intent
    }
    private val username: String? by lazy {
        intent?.getStringExtra(Constants.USERNAME_EXTRA)
    }
    private val isRemote: Boolean by lazy {
        intent?.getBooleanExtra(Constants.FAVORITE_FLAG_EXTRA, true) ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers, container, false)
        binding.state = Constants.LOADING

        userAdapter = GithubUserAdapter(requireContext())

        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.itemAnimator = DefaultItemAnimator()
        binding.rvUser.adapter = userAdapter

        binding.errorLayout.retryButton.setOnClickListener {
            viewModel.getListFollowers(username, isRemote)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.followers.observe(viewLifecycleOwner) { resource ->
            binding.state = resource.state
            if (resource !is Resource.Loading<*>) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}