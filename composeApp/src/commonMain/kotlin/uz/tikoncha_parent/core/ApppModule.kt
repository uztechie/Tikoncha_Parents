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
import uz.tikoncha_parent.domain.use_case.CreatePolicyTempUseCase
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
import uz.tikoncha_parent.presentation.statistic.StatisticViewModel
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
import uz.tikoncha_parent.data.remote.DeviceApiService
import uz.tikoncha_parent.data.remote.GetCoinPackageApiService
import uz.tikoncha_parent.data.remote.MyCoinsApiService
import uz.tikoncha_parent.data.remote.NewApiService
import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.PolicyApiService
import uz.tikoncha_parent.data.repository.ChatRepositoryImpl
import uz.tikoncha_parent.data.repository.DeviceRepositoryImpl
import uz.tikoncha_parent.data.repository.GetCoinPackageRepositoryImpl
import uz.tikoncha_parent.data.repository.MyCoinsRepositoryImpl
import uz.tikoncha_parent.data.repository.NewsRepositoryImpl
import uz.tikoncha_parent.data.repository.PaymentRepositoryImpl
import uz.tikoncha_parent.data.repository.PolicyRepositoryImpl
import uz.tikoncha_parent.domain.repository.ChatRepository
import uz.tikoncha_parent.domain.repository.CoinPackageRepository
import uz.tikoncha_parent.domain.repository.DeviceRepository
import uz.tikoncha_parent.domain.use_case.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.repository.MyCoinsRepository
import uz.tikoncha_parent.domain.repository.NewsRepository
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.domain.repository.PolicyRepository
import uz.tikoncha_parent.domain.use_case.GetCoinPackagesUseCase
import uz.tikoncha_parent.domain.use_case.policy.CreatePolicyUseCase
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.NewsUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPaymentUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatStatusUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatUnreadCountUseCase
import uz.tikoncha_parent.domain.use_case.chat.ConnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.DisconnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.EditMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatMessagesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkReadUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkUnreadUseCase
import uz.tikoncha_parent.domain.use_case.chat.MyCoinsUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatEventUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageApiUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.UpdateTodoUseCase
import uz.tikoncha_parent.domain.use_case.payment.PaymentStatusUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPlanUseCase
import uz.tikoncha_parent.domain.use_case.policy.DeletePolicyUseCase
import uz.tikoncha_parent.domain.use_case.policy.GetChildAppsUseCase
import uz.tikoncha_parent.domain.use_case.policy.UpdatePolicyUseCase
import uz.tikoncha_parent.presentation.chat.ChatConnectionManager
import uz.tikoncha_parent.presentation.chat_details.ChatDetailsViewModel
import uz.tikoncha_parent.presentation.monitoring.MonitorViewModel
import uz.tikoncha_parent.presentation.new_home.HomeViewModel
import uz.tikoncha_parent.presentation.notification.NotificationViewModel
import uz.tikoncha_parent.presentation.profile.coins.MyCoinsViewModel
import uz.tikoncha_parent.presentation.policy.PolicyViewModel
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebViewModel
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleViewModel
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupViewModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleViewModel
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentViewModel
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentViewModel

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
    single { DeviceApiService(get()) }
    single { NewApiService(get()) }
    single { MyCoinsApiService(get()) }
    single { PolicyApiService(get()) }
    single { PaymentApiService(get()) }
    single { GetCoinPackageApiService(get()) }

    //repository
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single<ChildRepository> { ChildRepositoryImpl(get()) }
    single<RulesRepository> { RulesRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
    single< MyCoinsRepository> { MyCoinsRepositoryImpl(get()) }
    single< CoinPackageRepository> { GetCoinPackageRepositoryImpl(get()) }
    single< PolicyRepository> { PolicyRepositoryImpl(get()) }
    single< PaymentRepository> { PaymentRepositoryImpl(get()) }



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
    single { CreatePolicyTempUseCase(get()) }
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
    single { RegisterDeviceUseCase(get()) }
    single { NewsUseCase(get()) }
    single { MyCoinsUseCase(get()) }
    single { CreatePolicyUseCase(get()) }
    single { UpdatePolicyUseCase(get()) }
    single { DeletePolicyUseCase(get()) }
    single { SubscriptionPaymentUseCase(get()) }
    single { PaymentStatusUseCase(get()) }
    single { SubscriptionLimitUseCase(get()) }
    single { SubscriptionPlanUseCase(get()) }
    single { GetChildAppsUseCase(get()) }

    single { ChatConnectionManager(get(), get()) }
    single { GetPoliciesFromServerUseCase(get() ) }
    single { GetCoinPackagesUseCase(get() ) }






    viewModel { LoginViewmodel(get()) }
    viewModel { OtpViewmodel(get() , get()) }
    viewModel { RegisterViewmodel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { CreatePasswordViewmodel() }
    viewModel { ChildViewmodel(get()) }
    viewModel { LoginPasswordViewmodel() }
    viewModel { ChildConfirmViewModel() }
    viewModel { TaskViewModel(get (), get(), get(), get(), get()) }
    factory { StatisticViewModel(get(), get(), get()) }
    factory { HomeViewModel(get(), get(), get()) }
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
    viewModel { PolicyViewModel(get(), get()) }
    viewModel { TimeRuleViewModel() }
    viewModel { LimitRuleViewModel() }
    viewModel { MyCoinsViewModel(get(), get(), get()) }

    viewModel { PolicySetupViewModel(get(), get(), get()) }
    single { PolicySharedModel(get()) }
    factory { AppWebViewModel(get()) }

    viewModel { PaymentViewModel(get(), get(), get(), get()) }
    viewModel { SubscriptionPaymentViewModel(get(), get()) }


}