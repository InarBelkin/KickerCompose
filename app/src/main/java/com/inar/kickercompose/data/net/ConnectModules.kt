package com.inar.kickercompose.data.net.repositories

import com.inar.kickercompose.data.net.network.NetworkService
import com.inar.kickercompose.data.viemodels.AccountHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class StatsReposModule {
    @Binds
    abstract fun bindStatsRepository(statsRepository: StatsRepository): IStatsRepository


//    @Binds
//    abstract fun bindAccountRepository(accountRepository: AccountRepository): IAccountRepository
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class SingleModule{
//    @Binds
//    @Singleton
//    abstract fun bindLobbyRepository(lobbyRepository: LobbyRepository): ILobbyRepository
//}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun createNetwork() = NetworkService()

    @Singleton
    @Provides
    fun createAccountRepos(networkService: NetworkService): IAccountRepository =
        AccountRepository(networkService)

    @Singleton
    @Provides
    fun createLobbyRepos(
        networkService: NetworkService,
        accountHandler: AccountHandler,
    ): ILobbyRepository = LobbyRepository(networkService, accountHandler);

    @Singleton
    @Provides
    fun createLobbyMessagesRepos(networkService: NetworkService):ILobbyMessagesRepository =
        LobbyMessagesRepository(networkService)
}