package tech.nekonyan.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.nekonyan.githubapp.data.model.GithubUser
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.util.SettingsPreferences
import tech.nekonyan.githubapp.util.network.Resource

class MainViewModel(
    private val remoteRepository: GithubRemoteRepository,
    private val localRepository: GithubLocalRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var _dataList: MutableLiveData<Resource<List<GithubUser>>> = MutableLiveData()
    val dataList: LiveData<Resource<List<GithubUser>>> get() = _dataList

    private var _favoriteDataList: MutableLiveData<Resource<List<GithubUser>>> = MutableLiveData()
    val favoriteDataList: LiveData<Resource<List<GithubUser>>> get() = _favoriteDataList

    // NOTE: Sengaja pakai "" agar list tidak kosong saat input kosong.
    var query: String = "\"\""
        set(value) {
            field = value.ifBlank { "\"\"" }
        }

    var favoriteQuery: String = ""

    fun searchUser() {
        val disposable = remoteRepository.searchUser(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _dataList.postValue(Resource.Loading())
            }
            .subscribe({ resource ->
                _dataList.postValue(resource)
            }, { ex ->
                _dataList.postValue(Resource.Error(ex))
            })

        compositeDisposable.add(disposable)
    }

    fun searchFavoriteUser() {
        val disposable = localRepository.searchUser(favoriteQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _favoriteDataList.postValue(Resource.Loading())
            }
            .subscribe({ resource ->
                _favoriteDataList.postValue(resource)
            }, {
                _favoriteDataList.postValue(Resource.Empty())
            })

        compositeDisposable.add(disposable)
    }

    fun getDarkModeSetting(): LiveData<Boolean> = preferences.getDarkModeSetting().asLiveData()
}