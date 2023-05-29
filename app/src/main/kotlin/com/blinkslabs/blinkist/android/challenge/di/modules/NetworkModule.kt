package com.blinkslabs.blinkist.android.challenge.di.modules

import android.content.Context
import com.blinkslabs.blinkist.android.challenge.common.NetworkStatus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun bindsNetworkStatus(context: Context) = NetworkStatus(context)

}
