package com.elyeproj.networkexperiment

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {
    fun inject(repository: Repository)
}
