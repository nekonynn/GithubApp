package tech.nekonyan.githubapp.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.ui.viewmodel.MainViewModel
import tech.nekonyan.githubapp.util.SettingsPreferences

class MainViewModelFactory(
    private val remoteRepository: GithubRemoteRepository,
    private val localRepository: GithubLocalRepository,
    private val preferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(remoteRepository, localRepository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}