package tech.nekonyan.githubapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import tech.nekonyan.githubapp.R
import tech.nekonyan.githubapp.ui.factory.SettingsViewModelFactory
import tech.nekonyan.githubapp.ui.viewmodel.SettingsViewModel
import tech.nekonyan.githubapp.util.Constants
import tech.nekonyan.githubapp.util.SettingsPreferences

class SettingsFragment : PreferenceFragmentCompat() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME)
    private val preferences: SettingsPreferences by lazy {
        SettingsPreferences.getInstance(requireContext().dataStore)
    }

    private lateinit var viewModel: SettingsViewModel

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = preferences
        setPreferencesFromResource(R.xml.settings, rootKey)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(preferences)
        )[SettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDarkModeSetting().observe(viewLifecycleOwner) { isDarkMode ->
            // Untuk mencegah lag ketika switch diklik berkali-kali.
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }, Constants.DARK_MODE_CHANGE_DELAY)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}