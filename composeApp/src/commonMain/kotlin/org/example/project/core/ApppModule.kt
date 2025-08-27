package org.example.project.core

import org.example.project.data.remote.AvatarApiService
import org.example.project.data.remote.ChildApiService
import org.example.project.data.remote.LoginApiService
import org.example.project.data.remote.TikonchaClient
import org.example.project.data.repository.AvatarRepositoryImpl
import org.example.project.data.repository.ChildRepositoryImpl
import org.example.project.data.repository.LoginRepositoryImpl
import org.example.project.domain.repository.AvatarRepository
import org.example.project.domain.repository.ChildRepository
import org.example.project.domain.repository.LoginRepository
import org.example.project.domain.use_case.AddChildUseCase
import org.example.project.domain.use_case.ChildrenUseCase
import org.example.project.domain.use_case.LoadAvatarFromServerUseCase
import org.example.project.domain.use_case.RegisterUseCase
import org.example.project.domain.use_case.SendOtpUseCase
import org.example.project.domain.use_case.UploadAvatarToServerUseCase
import org.example.project.domain.use_case.UserInfoUseCase
import org.example.project.domain.use_case.VerifyOtpUseCase
import org.example.project.presentation.add_child.ChildViewmodel
import org.example.project.presentation.child_confirm_cod.ChildConfirmViewModel
import org.example.project.presentation.home.HomeViewModel
import org.example.project.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.example.project.presentation.login.LoginViewmodel
import org.example.project.presentation.otp.OtpViewmodel
import org.example.project.presentation.register.RegisterViewmodel
import org.example.project.presentation.task.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.saidburxon.newedu.presentation.feature.chat.ChatViewModel
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordViewmodel
import uz.saidburxon.newedu.presentation.feature.login_password.LoginPasswordViewmodel

val sharedModule = module {
    viewModelOf(::HomeViewModel)
    single {
        HttpClientEngineFactory().getHttpEngine()
    }

    single {
        TikonchaClient(get()).client
    }
    //api service
    single { LoginApiService(get()) }
    single { ChildApiService(get()) }
    single { AvatarApiService(get()) }

    //repository
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<ChildRepository> { ChildRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl(get()) }


    //use case module
    single { SendOtpUseCase(get()) }
    single { VerifyOtpUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { UserInfoUseCase(get()) }
    single { AddChildUseCase(get()) }
    single { ChildrenUseCase(get()) }
    single { UploadAvatarToServerUseCase(get()) }
    single { LoadAvatarFromServerUseCase(get()) }



    viewModel { LoginViewmodel(get()) }
    viewModel { OtpViewmodel(get()) }
    viewModel { RegisterViewmodel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { CreatePasswordViewmodel() }
    viewModel { ChildViewmodel(get()) }
    viewModel { LoginPasswordViewmodel() }
    viewModel { TaskViewModel() }
    viewModel { HomeViewModel() }
    viewModel { ChildConfirmViewModel() }
    viewModel { ChatViewModel() }


}