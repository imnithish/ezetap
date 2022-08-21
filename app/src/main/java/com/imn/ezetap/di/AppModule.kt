/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.di

import com.imn.network.network.Network
import com.imn.network.network.NetworkImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNetwork(): Network {
        return NetworkImplementation()
    }
}