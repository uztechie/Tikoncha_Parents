package org.example.project.core

import org.example.project.data.remote.TikonchaApi
import org.example.project.data.remote.TikonchaApiImpl
import org.example.project.data.remote.TikonchaClient
import org.example.project.domain.TikonchaRepository
import org.example.project.domain.TikonchaRepositoryImpl
import org.example.project.presentation.add_child.ChildViewmodel
import org.example.project.presentation.home.HomeViewModel
import org.example.project.presentation.login.LoginViewmodel
import org.example.project.presentation.otp.OtpViewmodel
import org.example.project.presentation.register.RegisterViewmodel
import org.example.project.presentation.task.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordViewmodel
import uz.saidburxon.newedu.presentation.feature.login_password.LoginPasswordViewmodel

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
    viewModel { LoginViewmodel() }
    viewModel { OtpViewmodel() }
    viewModel { RegisterViewmodel() }
    viewModel { CreatePasswordViewmodel() }
    viewModel { ChildViewmodel() }
    viewModel { LoginPasswordViewmodel() }
    viewModel { TaskViewModel() }
    viewModel { HomeViewModel(get ()) }
}