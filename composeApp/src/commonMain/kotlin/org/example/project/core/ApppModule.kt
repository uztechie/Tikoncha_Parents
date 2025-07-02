package org.example.project.core

import org.example.project.data.remote.TikonchaApi
import org.example.project.data.remote.TikonchaApiImpl
import org.example.project.data.remote.TikonchaClient
import org.example.project.domain.TikonchaRepository
import org.example.project.domain.TikonchaRepositoryImpl
import org.example.project.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClientEngineFactory().getHttpEngine()
    }

    single {
        TikonchaClient(get()).client
    }
    single<TikonchaApi> { TikonchaApiImpl(get()) }
    single<TikonchaRepository> { TikonchaRepositoryImpl(get()) }


    viewModel { HomeViewModel(get()) }
}