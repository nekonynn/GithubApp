package tech.nekonyan.githubapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.data.network.service.GithubService
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.databinding.ActivityDetailBinding
import tech.nekonyan.githubapp.ui.factory.DetailViewModelFactory
import tech.nekonyan.githubapp.ui.fragment.ListFollowersFragment
import tech.nekonyan.githubapp.ui.fragment.ListFollowingFragment
import tech.nekonyan.githubapp.ui.viewmodel.DetailViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.Extensions.enableBackButton
import tech.nekonyan.githubapp.util.Extensions.setPlaceholderAndError
import tech.nekonyan.githubapp.util.ViewPagerAdapter
import tech.nekonyan.githubapp.util.database.GithubDatabase
import tech.nekonyan.githubapp.util.network.Resource
import tech.nekonyan.githubapp.util.network.RetrofitClient

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding: ActivityDetailBinding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private val isLoadingFavorite: Boolean by lazy {
        intent.getBooleanExtra(Constants.FAVORITE_FLAG_EXTRA, false)
    }
    private val username: String? by lazy {
        intent.getStringExtra(Constants.USERNAME_EXTRA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitClient.retrofit.create(GithubService::class.java)
        val database = GithubDatabase.getDatabase(this)

        val remoteRepository = GithubRemoteRepository(service)
        val localRepository = GithubLocalRepository(database)

        viewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(remoteRepository, localRepository)
        )[DetailViewModel::class.java]

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.state = Constants.LOADING
        binding.shimmerLayout.container.startShimmer()

        binding.swipeLayout.setOnRefreshListener {
            loadData()
        }

        binding.errorLayout.retryButton.setOnClickListener {
            loadData()
        }

        binding.favButton.setOnClickListener {
            if (viewModel.itemData != null) {
                viewModel.toggleFavorite()
            } else {
                Toast.makeText(this, getString(R.string.toast_error_fav), Toast.LENGTH_SHORT).show()
            }
        }

        enableBackButton()
        setupViewPager()
        setupObserver()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.removeAllFragment()
        adapter.notifyDataSetChanged()

        adapter.addFragment(ListFollowingFragment(), getString(R.string.title_fragment_following))
        adapter.addFragment(ListFollowersFragment(), getString(R.string.title_fragment_followers))
        adapter.notifyDataSetChanged()

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupObserver() {
        viewModel.data.observe(this) { resource ->
            binding.state = resource.state
            if (resource !is Resource.Loading<*>) {
                binding.swipeLayout.isRefreshing = false
                binding.shimmerLayout.container.hideShimmer()
            } else {
                binding.shimmerLayout.container.showShimmer(true)
            }

            if (resource is Resource.Success<*>) {
                resource.data?.let { data ->
                    binding.item = viewModel.itemData

                    Glide.with(this).load(data.avatar)
                        .setPlaceholderAndError(this).into(binding.ivAvatar)
                    binding.ivAvatar.setOnClickListener {
                        Intent(this, PreviewImageActivity::class.java).run {
                            putExtra(Constants.AVATAR_URL_EXTRA, data.avatar)
                            startActivity(this)
                        }
                    }
                }
            }
        }

        viewModel.stateFavorite.observe(this) { state ->
            val drawable: Int = if (state) {
                R.drawable.ic_favorited
            } else {
                R.drawable.ic_favorite
            }

            binding.favButton.setImageDrawable(ContextCompat.getDrawable(this, drawable))
        }
    }

    private fun loadData() {
        viewModel.getDetailUser(username, isLoadingFavorite)
        viewModel.getFavorite(username)
        viewModel.getListFollowing(username, isLoadingFavorite)
        viewModel.getListFollowers(username, isLoadingFavorite)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}