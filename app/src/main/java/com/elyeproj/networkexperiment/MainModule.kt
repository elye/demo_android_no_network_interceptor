package com.elyeproj.networkexperiment

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class MainModule(private val context: Context) {

    @Provides
    @Singleton
    fun context(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideHttpClient(noConnectionInterceptor: NoConnectionInterceptor) =
        OkHttpClient.Builder().addInterceptor(noConnectionInterceptor).build()
}
