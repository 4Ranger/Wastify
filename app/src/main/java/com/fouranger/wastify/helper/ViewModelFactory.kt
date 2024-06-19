package com.fouranger.wastify.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fouranger.wastify.data.Repository
import com.fouranger.wastify.di.Injection
import com.fouranger.wastify.ui.login.LoginViewModel
import com.fouranger.wastify.ui.main.MainViewModel
import com.fouranger.wastify.ui.main.classifications.ClassificationsViewModel
import com.fouranger.wastify.ui.main.home.HomeViewModel
import com.fouranger.wastify.ui.main.home.profile.ProfileViewModel
import com.fouranger.wastify.ui.main.leaderboards.LeaderboardsViewModel
import com.fouranger.wastify.ui.register.RegisterViewModel
import com.fouranger.wastify.ui.main.scan.ScanViewModel
import com.fouranger.wastify.ui.welcome.WelcomeViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LeaderboardsViewModel::class.java) -> {
                LeaderboardsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ClassificationsViewModel::class.java) -> {
                ClassificationsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}