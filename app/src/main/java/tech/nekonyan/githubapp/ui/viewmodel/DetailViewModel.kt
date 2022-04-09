package tech.nekonyan.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.nekonyan.githubapp.data.local.entity.GithubDetailEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowersEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowingEntity
import tech.nekonyan.githubapp.data.model.GithubDetailUser
import tech.nekonyan.githubapp.data.model.GithubUser
import tech.nekonyan.githubapp.data.repository.GithubLocalRepository
import tech.nekonyan.githubapp.data.repository.GithubRemoteRepository
import tech.nekonyan.githubapp.util.network.Resource

class DetailViewModel(
    private val remoteRepository: GithubRemoteRepository,
    private val localRepository: GithubLocalRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var _data: MutableLiveData<Resource<GithubDetailUser>> = MutableLiveData()
    val data: LiveData<Resource<GithubDetailUser>> get() = _data

    private var _following: MutableLiveData<Resource<List<GithubUser>>> = MutableLiveData()
    val following: LiveData<Resource<List<GithubUser>>> get() = _following

    private var _followers: MutableLiveData<Resource<List<GithubUser>>> = MutableLiveData()
    val followers: LiveData<Resource<List<GithubUser>>> get() = _followers

    private var _stateFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val stateFavorite: LiveData<Boolean> get() = _stateFavorite

    var itemData: GithubDetailUser? = null
    private val listFollowing: ArrayList<GithubFollowingEntity> = ArrayList()
    private val listFollowers: ArrayList<GithubFollowersEntity> = ArrayList()

    fun getDetailUser(username: String?, isLoadingFavorite: Boolean) {
        if (username == null) {
            _data.postValue(Resource.Error(Exception("No username")))
            return
        }

        val disposable = if (!isLoadingFavorite) {
            remoteRepository.getDetailUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _data.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _data.postValue(resource)
                    itemData = resource.data
                }, { ex ->
                    _data.postValue(Resource.Error(ex))
                })
        } else {
            localRepository.getDetailUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _data.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _data.postValue(resource)
                    itemData = resource.data
                }, { throwable ->
                    _data.postValue(Resource.Error(throwable))
                })
        }

        compositeDisposable.add(disposable)
    }

    fun getListFollowing(username: String?, isLoadingFavorite: Boolean) {
        if (username == null) {
            _data.postValue(Resource.Error(Exception("No username")))
            return
        }

        val disposable = if (!isLoadingFavorite) {
            remoteRepository.getUserFollowing(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _following.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _following.postValue(resource)
                    listFollowing.clear()
                    resource.data?.forEach { data ->
                        listFollowing.add(GithubFollowingEntity(data, username))
                    }
                }, { ex ->
                    _following.postValue(Resource.Error(ex))
                })
        } else {
            localRepository.getUserFollowing(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _following.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _following.postValue(resource)
                    listFollowing.clear()
                    resource.data?.forEach { data ->
                        listFollowing.add(GithubFollowingEntity(data, username))
                    }
                }, {
                    _following.postValue(Resource.Empty())
                })
        }

        compositeDisposable.add(disposable)
    }

    fun getListFollowers(username: String?, isLoadingFavorite: Boolean) {
        if (username == null) {
            _data.postValue(Resource.Error(Exception("No username")))
            return
        }

        val disposable = if (!isLoadingFavorite) {
            remoteRepository.getUserFollowers(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _followers.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _followers.postValue(resource)
                    listFollowers.clear()
                    resource.data?.forEach { data ->
                        listFollowers.add(GithubFollowersEntity(data, username))
                    }
                }, { ex ->
                    _followers.postValue(Resource.Error(ex))
                })
        } else {
            localRepository.getUserFollowers(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _followers.postValue(Resource.Loading())
                }
                .subscribe({ resource ->
                    _followers.postValue(resource)
                    listFollowers.clear()
                    resource.data?.forEach { data ->
                        listFollowers.add(GithubFollowersEntity(data, username))
                    }
                }, {
                    _followers.postValue(Resource.Empty())
                })
        }

        compositeDisposable.add(disposable)
    }

    fun toggleFavorite() {
        itemData?.let { data ->
            val isFavorite = stateFavorite.value ?: false

            val disposable =
                localRepository.toggleFavorite(
                    GithubDetailEntity(data),
                    listFollowing, listFollowers,
                    isFavorite
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        _stateFavorite.postValue(!isFavorite)
                    }

            compositeDisposable.add(disposable)
        }
    }

    fun getFavorite(username: String?) {
        if (username == null) {
            return
        }

        val disposable = localRepository.getDetailUser(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _stateFavorite.postValue(true)
            }, {
                _stateFavorite.postValue(false)
            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}