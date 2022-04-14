package com.inar.kickercompose.data.net.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class StatsReposModule {
    @Binds
    abstract fun bindStatsRepository(statsRepository: StatsRepository): IStatsRepository

}