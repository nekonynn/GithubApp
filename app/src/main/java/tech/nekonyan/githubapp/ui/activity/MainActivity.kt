package tech.nekonyan.githubapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.data.network.service.GithubService
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.databinding.ActivityMainBinding
import tech.nekonyan.githubapp.ui.factory.MainViewModelFactory
import tech.nekonyan.githubapp.ui.fragment.FavoriteFragment
import tech.nekonyan.githubapp.ui.fragment.ListFragment
import tech.nekonyan.githubapp.ui.viewmodel.MainViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.SettingsPreferences
import tech.nekonyan.githubapp.util.ViewPagerAdapter
import tech.nekonyan.githubapp.util.database.GithubDatabase
import tech.nekonyan.githubapp.util.network.RetrofitClient

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME)
    private val preferences: SettingsPreferences by lazy {
        SettingsPreferences.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitClient.retrofit.create(GithubService::class.java)
        val database = GithubDatabase.getDatabase(this)

        val remoteRepository = GithubRemoteRepository(service)
        val localRepository = GithubLocalRepository(database)

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(remoteRepository, localRepository, preferences)
        )[MainViewModel::class.java]

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupDarkMode()
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.removeAllFragment()
        adapter.notifyDataSetChanged()

        adapter.addFragment(ListFragment(), getString(R.string.title_fragment_list))
        adapter.addFragment(FavoriteFragment(), getString(R.string.title_fragment_favorite))
        adapter.notifyDataSetChanged()

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupDarkMode() {
        viewModel.getDarkModeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingsButton) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}