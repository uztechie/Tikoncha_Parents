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
import uz.tikoncha_parent.presentation.chat.ChatViewModel
import uz.saidburxon.newedu.presentation.feature.create_password.CreatePasswordViewmodel
import uz.saidburxon.newedu.presentation.feature.login_password.LoginPasswordViewmodel
import uz.tikoncha_parent.data.remote.ChatApiService
import uz.tikoncha_parent.data.remote.ChatSocketService
import uz.tikoncha_parent.data.remote.NewApiService
import uz.tikoncha_parent.data.repository.ChatRepositoryImpl
import uz.tikoncha_parent.data.repository.NewsRepositoryImpl
import uz.tikoncha_parent.domain.repository.ChatRepository
import uz.tikoncha_parent.domain.repository.NewsRepository
import uz.tikoncha_parent.domain.use_case.NewsUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatStatusUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatUnreadCountUseCase
import uz.tikoncha_parent.domain.use_case.chat.ConnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.DisconnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.EditMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatMessagesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkReadUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkUnreadUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatEventUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageApiUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.UpdateTodoUseCase
import uz.tikoncha_parent.presentation.chat.ChatConnectionManager
import uz.tikoncha_parent.presentation.chat_details.ChatDetailsViewModel
import uz.tikoncha_parent.presentation.monitoring.MonitorViewModel
import uz.tikoncha_parent.presentation.notification.NotificationViewModel

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
    single { NewApiService(get()) }

    //repository
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single<ChildRepository> { ChildRepositoryImpl(get()) }
    single<RulesRepository> { RulesRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }



    //use case module
    single { SendOtpUseCase(get()) }
    single { VerifyOtpUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { TodoUseCase(get()) }
    single { UserInfoUseCase(get()) }
    single { TodoListUseCase(get()) }
    single { UpdateTodoUseCase(get()) }
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

    single { ChatStatusUseCase(get()) }
    single { ChatUnreadCountUseCase(get()) }
    single { ConnectChatWebSocketUseCase(get()) }
    single { DisconnectChatWebSocketUseCase(get()) }
    single { EditMessageUseCase(get()) }
    single { GetChatListFromServerUseCase(get()) }
    single { GetChatMessagesFromServerUseCase(get()) }
    single { MarkReadUseCase(get()) }
    single { MarkUnreadUseCase(get()) }
    single { ObserveChatEventUseCase(get()) }
    single { SendMessageUseCase(get()) }
    single { SendMessageApiUseCase(get()) }
    single { NewsUseCase(get()) }

    single { ChatConnectionManager(get(), get()) }






    viewModel { LoginViewmodel(get()) }
    viewModel { OtpViewmodel(get()) }
    viewModel { RegisterViewmodel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { CreatePasswordViewmodel() }
    viewModel { ChildViewmodel(get()) }
    viewModel { LoginPasswordViewmodel() }
    viewModel { TaskViewModel(get (), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { ChildConfirmViewModel() }
    viewModel { ChatDetailsViewModel(get()) }
    viewModel { ChatViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get()
    ) }
    viewModel { MonitorViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(), get()) }





}