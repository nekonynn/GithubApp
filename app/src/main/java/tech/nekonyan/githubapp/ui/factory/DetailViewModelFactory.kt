package tech.nekonyan.githubapp.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.ui.viewmodel.DetailViewModel

class DetailViewModelFactory(
    private val remoteRepository: GithubRemoteRepository,
    private val localRepository: GithubLocalRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(remoteRepository, localRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}