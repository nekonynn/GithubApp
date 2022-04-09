package tech.nekonyan.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import tech.nekonyan.githubapp.util.SettingsPreferences

class SettingsViewModel(
    private val preferences: SettingsPreferences
) : ViewModel() {
    fun getDarkModeSetting(): LiveData<Boolean> = preferences.getDarkModeSetting().asLiveData()
}