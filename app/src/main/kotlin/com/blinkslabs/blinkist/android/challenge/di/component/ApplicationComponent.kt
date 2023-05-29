package com.blinkslabs.blinkist.android.challenge.di.component

import android.content.Context
import com.blinkslabs.blinkist.android.challenge.di.modules.BooksApiModule
import com.blinkslabs.blinkist.android.challenge.di.modules.BooksDBModule
import com.blinkslabs.blinkist.android.challenge.di.modules.NetworkModule
import com.blinkslabs.blinkist.android.challenge.ui.BooksActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BooksApiModule::class, BooksDBModule::class, NetworkModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(activity: BooksActivity)
}
