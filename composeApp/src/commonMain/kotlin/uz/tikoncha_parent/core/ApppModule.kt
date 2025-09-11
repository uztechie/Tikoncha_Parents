package uz.tikoncha_parent.core

import uz.tikoncha_parent.data.remote.AvatarApiService
import uz.tikoncha_parent.data.remote.ChildApiService
import uz.tikoncha_parent.data.remote.LoginApiService
import uz.tikoncha_parent.data.remote.RulesApiService
import uz.tikoncha_parent.data.remote.TikonchaClient
import uz.tikoncha_parent.data.remote.TodoApiService
import uz.tikoncha_parent.data.repository.AvatarRepositoryImpl
import uz.tikoncha_parent.data.repository.ChildRepositoryImpl
import uz.tikoncha_parent.data.repository.LoginRepositoryImpl
import uz.tikoncha_parent.data.repository.RulesRepositoryImpl
import uz.tikoncha_parent.data.repository.TodoRepositoryImpl
import uz.tikoncha_parent.domain.repository.AvatarRepository
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.tikoncha_parent.domain.repository.RulesRepository
import uz.tikoncha_parent.domain.repository.TodoRepository
import uz.tikoncha_parent.domain.use_case.AddChildUseCase
import uz.tikoncha_parent.domain.use_case.AppUsagesUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.CreatePolicyUseCase
import uz.tikoncha_parent.domain.use_case.CreateRuleUseCase
import uz.tikoncha_parent.domain.use_case.LoadAvatarFromServerUseCase
import uz.tikoncha_parent.domain.use_case.RefreshRulesUseCase
import uz.tikoncha_parent.domain.use_case.RegisterUseCase
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.tikoncha_parent.domain.use_case.TodoListUseCase
import uz.tikoncha_parent.domain.use_case.TodoUseCase
import uz.tikoncha_parent.domain.use_case.UploadAvatarToServerUseCase
import uz.tikoncha_parent.domain.use_case.UpsertRuleUseCase
import uz.tikoncha_parent.domain.use_case.UserInfoUseCase
import uz.tikoncha_parent.domain.use_case.VerifyOtpUseCase
import uz.tikoncha_parent.presentation.add_child.ChildViewmodel
import uz.tikoncha_parent.presentation.child_confirm_cod.ChildConfirmViewModel
import uz.tikoncha_parent.presentation.home.HomeViewModel
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.presentation.login.LoginViewmodel
import uz.tikoncha_parent.presentation.otp.OtpViewmodel
import uz.tikoncha_parent.presentation.register.RegisterViewmodel
import uz.tikoncha_parent.presentation.task.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.saidburxon.newedu.presentation.feature.chat.ChatViewModel
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordViewmodel
import uz.saidburxon.newedu.presentation.feature.login_password.LoginPasswordViewmodel
import uz.tikoncha_parent.data.remote.ChatApiService
import uz.tikoncha_parent.data.remote.ChatSocketService
import uz.tikoncha_parent.data.repository.ChatRepositoryImpl
import uz.tikoncha_parent.domain.repository.ChatRepository

val sharedModule = module {
    single {
        HttpClientEngineFactory().getHttpEngine()
    }

    single {
        TikonchaClient(get()).client
    }
    //api service
    single { LoginApiService(get()) }
    single { TodoApiService(get()) }
    single { ChildApiService(get()) }
    single { RulesApiService(get()) }
    single { AvatarApiService(get()) }
    single { ChatApiService(get()) }
    single { ChatSocketService(get()) }

    //repository
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single<ChildRepository> { ChildRepositoryImpl(get()) }
    single<RulesRepository> { RulesRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }



    //use case module
    single { SendOtpUseCase(get()) }
    single { VerifyOtpUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { TodoUseCase(get()) }
    single { UserInfoUseCase(get()) }
    single { TodoListUseCase(get()) }
    single { AddChildUseCase(get()) }
    single { ChildrenUseCase(get()) }
    single { AppUsagesUseCase(get()) }
    single { UploadAvatarToServerUseCase(get()) }
    single { LoadAvatarFromServerUseCase(get()) }
    single { RefreshRulesUseCase(get()) }
    single { CreatePolicyUseCase(get()) }
    single { CreateRuleUseCase(get()) }
    single { UpsertRuleUseCase(get()) }
    single { ChildrenLocationUseCase(get()) }





    viewModel { LoginViewmodel(get()) }
    viewModel { OtpViewmodel(get()) }
    viewModel { RegisterViewmodel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { CreatePasswordViewmodel() }
    viewModel { ChildViewmodel(get()) }
    viewModel { LoginPasswordViewmodel() }
    viewModel { TaskViewModel(get (), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { ChildConfirmViewModel() }
    viewModel { ChatViewModel() }





}