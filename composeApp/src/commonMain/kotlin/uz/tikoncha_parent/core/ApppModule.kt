package uz.tikoncha_parent.core

import uz.tikoncha_parent.data.remote.AvatarApiService
import uz.tikoncha_parent.data.remote.ChildApiService
import uz.tikoncha_parent.data.remote.LoginApiService
import uz.tikoncha_parent.data.remote.TikonchaClient

import uz.tikoncha_parent.data.remote.TodoApiService
import uz.tikoncha_parent.data.repository.AvatarRepositoryImpl
import uz.tikoncha_parent.data.repository.ChildRepositoryImpl
import uz.tikoncha_parent.data.repository.LoginRepositoryImpl
import uz.tikoncha_parent.data.repository.TodoRepositoryImpl
import uz.tikoncha_parent.domain.repository.AvatarRepository
import uz.tikoncha_parent.domain.repository.ChildRepository
import uz.tikoncha_parent.domain.repository.LoginRepository
import uz.tikoncha_parent.domain.repository.TodoRepository
import uz.tikoncha_parent.domain.use_case.AddChildUseCase
import uz.tikoncha_parent.domain.use_case.AppUsagesUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.domain.use_case.ChildrenUseCase
import uz.tikoncha_parent.domain.use_case.LoadAvatarFromServerUseCase
import uz.tikoncha_parent.domain.use_case.RegisterUseCase
import uz.tikoncha_parent.domain.use_case.SendOtpUseCase
import uz.tikoncha_parent.domain.use_case.todo.TodoListUseCase
import uz.tikoncha_parent.domain.use_case.todo.TodoUseCase
import uz.tikoncha_parent.domain.use_case.UploadAvatarToServerUseCase
import uz.tikoncha_parent.domain.use_case.UserInfoUseCase
import uz.tikoncha_parent.domain.use_case.VerifyOtpUseCase
import uz.tikoncha_parent.presentation.child_confirm_cod.ChildConfirmViewModel
import uz.tikoncha_parent.presentation.statistic.StatisticViewModel
import uz.tikoncha_parent.presentation.profile.ProfileViewModel
import uz.tikoncha_parent.presentation.login.LoginViewModel
import uz.tikoncha_parent.presentation.otp.OtpViewmodel
import uz.tikoncha_parent.presentation.register.RegisterViewmodel
import org.koin.dsl.module
import uz.tikoncha_parent.data.remote.ChatApiService
import uz.tikoncha_parent.data.remote.ChatSocketService
import uz.tikoncha_parent.data.remote.DeviceApiService
import uz.tikoncha_parent.data.remote.GetCoinPackageApiService
import uz.tikoncha_parent.data.remote.ParentRequestsApiService
import uz.tikoncha_parent.data.remote.MyCoinsApiService
import uz.tikoncha_parent.data.remote.NewApiService
import uz.tikoncha_parent.data.remote.PaymentApiService
import uz.tikoncha_parent.data.remote.PermissionStatusApiService
import uz.tikoncha_parent.data.remote.PolicyApiService
import uz.tikoncha_parent.data.remote.TutorialApiService
import uz.tikoncha_parent.data.repository.ChatRepositoryImpl
import uz.tikoncha_parent.data.repository.DeviceRepositoryImpl
import uz.tikoncha_parent.data.repository.GetCoinPackageRepositoryImpl
import uz.tikoncha_parent.data.repository.ParentRequestsRepositoryImpl
import uz.tikoncha_parent.data.repository.MyCoinsRepositoryImpl
import uz.tikoncha_parent.data.repository.NewsRepositoryImpl
import uz.tikoncha_parent.data.repository.PaymentRepositoryImpl
import uz.tikoncha_parent.data.repository.PermissionStatusRepositoryImpl
import uz.tikoncha_parent.data.repository.PlayerRepositoryImpl
import uz.tikoncha_parent.data.repository.PolicyRepositoryImpl
import uz.tikoncha_parent.data.repository.TutorialRepositoryImpl
import uz.tikoncha_parent.data.repository.UpdateRepositoryImpl
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.domain.repository.ChatRepository
import uz.tikoncha_parent.domain.repository.CoinPackageRepository
import uz.tikoncha_parent.domain.repository.DeviceRepository
import uz.tikoncha_parent.domain.repository.ParentRequestsRepository
import uz.tikoncha_parent.domain.use_case.device.RegisterDeviceUseCase
import uz.tikoncha_parent.domain.repository.MyCoinsRepository
import uz.tikoncha_parent.domain.repository.NewsRepository
import uz.tikoncha_parent.domain.repository.PaymentRepository
import uz.tikoncha_parent.domain.repository.PermissionStatusRepository
import uz.tikoncha_parent.domain.repository.PlayerRepository
import uz.tikoncha_parent.domain.repository.PolicyRepository
import uz.tikoncha_parent.domain.repository.TutorialRepository
import uz.tikoncha_parent.domain.repository.UpdateRepository
import uz.tikoncha_parent.domain.use_case.ChildInfoEditUseCase
import uz.tikoncha_parent.domain.use_case.payment.GetCoinPackageListUseCase
import uz.tikoncha_parent.domain.use_case.policy.CreatePolicyUseCase
import uz.tikoncha_parent.domain.use_case.GetPoliciesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.ParentRequestsUseCase
import uz.tikoncha_parent.domain.use_case.NewsUseCase
import uz.tikoncha_parent.domain.use_case.UpdateParentRequestStatusUseCase
import uz.tikoncha_parent.domain.use_case.UserInfoEditUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPaymentUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatStatusUseCase
import uz.tikoncha_parent.domain.use_case.chat.ChatUnreadCountUseCase
import uz.tikoncha_parent.domain.use_case.chat.ConnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.DeleteMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.DisconnectChatWebSocketUseCase
import uz.tikoncha_parent.domain.use_case.chat.EditMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatListFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetChatMessagesFromServerUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkReadUseCase
import uz.tikoncha_parent.domain.use_case.chat.MarkUnreadUseCase
import uz.tikoncha_parent.domain.use_case.chat.GetMyCoinsUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatEventUseCase
import uz.tikoncha_parent.domain.use_case.chat.ObserveChatStatusUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageApiUseCase
import uz.tikoncha_parent.domain.use_case.chat.SendMessageUseCase
import uz.tikoncha_parent.domain.use_case.chat.UpdateTodoUseCase
import uz.tikoncha_parent.domain.use_case.device.LogoutUseCase
import uz.tikoncha_parent.domain.use_case.in_app_update.CheckUpdateUseCase
import uz.tikoncha_parent.domain.use_case.in_app_update.CompleteFlexibleUpdateUseCase
import uz.tikoncha_parent.domain.use_case.in_app_update.ObserveInstallEventsUseCase
import uz.tikoncha_parent.domain.use_case.payment.GetPaymentTransactionsUseCase
import uz.tikoncha_parent.domain.use_case.payment.PaymentStatusUseCase
import uz.tikoncha_parent.domain.use_case.payment.PromoCodeValidationUseCase
import uz.tikoncha_parent.domain.use_case.payment.PurchaseCoinUseCase
import uz.tikoncha_parent.domain.use_case.payment.PurchaseIApPremiumUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionLimitUseCase
import uz.tikoncha_parent.domain.use_case.payment.SubscriptionPlanUseCase
import uz.tikoncha_parent.domain.use_case.permission_status.PermissionStatusUseCase
import uz.tikoncha_parent.domain.use_case.policy.DeletePolicyUseCase
import uz.tikoncha_parent.domain.use_case.policy.GetChildAppsUseCase
import uz.tikoncha_parent.domain.use_case.policy.UpdatePolicyUseCase
import uz.tikoncha_parent.domain.use_case.todo.CompleteTodoUseCase
import uz.tikoncha_parent.domain.use_case.todo.DeleteTodoUseCase
import uz.tikoncha_parent.domain.use_case.todo.GetTodoByIdUseCase
import uz.tikoncha_parent.domain.use_case.todo.GetTodosUseCase
import uz.tikoncha_parent.domain.use_case.tutorial.VideoTutorialUseCase
import uz.tikoncha_parent.platform.PlatformPurchaseService
import uz.tikoncha_parent.presentation.add_child.AddChildScreenModel
import uz.tikoncha_parent.presentation.chat.ChatConnectionManager
import uz.tikoncha_parent.presentation.chat.chat_list.ChatViewModel
import uz.tikoncha_parent.presentation.chat.chat_room.ChatRoomViewModel
import uz.tikoncha_parent.presentation.in_app_update.UpdateViewModel
import uz.tikoncha_parent.presentation.new_home.HomeViewModel
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestViewModel
import uz.tikoncha_parent.presentation.notification.NotificationViewModel
import uz.tikoncha_parent.presentation.player.PlayerScreenModel
import uz.tikoncha_parent.presentation.policy.policy_list.PolicyViewModel
import uz.tikoncha_parent.presentation.policy.app_site_selection.AppWebViewModel
import uz.tikoncha_parent.presentation.policy.limit_rule.setup.LimitRuleSetupViewModel
import uz.tikoncha_parent.presentation.profile.coins.CoinsViewModel
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupViewModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.template.sleep.SleepTemplateSetupViewModel
import uz.tikoncha_parent.presentation.policy.time_rule.setup.TimeRuleSetupViewModel
import uz.tikoncha_parent.presentation.profile.child_user_edit.ChildInfoEditViewModel
import uz.tikoncha_parent.presentation.profile.coin_purchase.CoinPurchaseViewModel
import uz.tikoncha_parent.presentation.profile.payment_history.PaymentHistoryScreenModel
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentViewModel
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentViewModel
import uz.tikoncha_parent.presentation.profile.user_edit.UserInfoEditViewModel
import uz.tikoncha_parent.presentation.task.create_task.CreateTaskViewModel
import uz.tikoncha_parent.presentation.task.TaskListViewModel
import uz.tikoncha_parent.presentation.video_tutorial.VideoTutorialScreenModel

val sharedModule = module {
    single {
        HttpClientEngineFactory().getHttpEngine()
    }

    single {
        TikonchaClient(get()).client
    }


    single {
        PlatformPurchaseService()
    }

    //api service
    single { LoginApiService(get()) }
    single { TodoApiService(get()) }
    single { ChildApiService(get()) }
    single { AvatarApiService(get()) }
    single { ChatApiService(get()) }
    single { ChatSocketService(get()) }
    single { DeviceApiService(get()) }
    single { NewApiService(get()) }
    single { MyCoinsApiService(get()) }
    single { PolicyApiService(get()) }
    single { PaymentApiService(get()) }
    single { GetCoinPackageApiService(get()) }
    single { ParentRequestsApiService(get()) }
    single { PermissionStatusApiService(get()) }
    single { TutorialApiService(get()) }

    //repository
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    single<ChildRepository> { ChildRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }
    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
    single< MyCoinsRepository> { MyCoinsRepositoryImpl(get()) }
    single< CoinPackageRepository> { GetCoinPackageRepositoryImpl(get()) }
    single< PolicyRepository> { PolicyRepositoryImpl(get()) }
    single< PaymentRepository> { PaymentRepositoryImpl(get()) }
    single< ParentRequestsRepository> { ParentRequestsRepositoryImpl(get()) }
    single< UpdateRepository> { UpdateRepositoryImpl(get()) }
    single< PermissionStatusRepository> { PermissionStatusRepositoryImpl(get()) }

    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
    single<TutorialRepository> { TutorialRepositoryImpl(get()) }



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
    single { ChildrenLocationUseCase(get()) }

    single { ChatStatusUseCase(get()) }
    single { ObserveChatStatusUseCase(get()) }
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
    single { GetMyCoinsUseCase(get()) }
    single { CreatePolicyUseCase(get()) }
    single { UpdatePolicyUseCase(get()) }
    single { DeletePolicyUseCase(get()) }
    single { SubscriptionPaymentUseCase(get()) }
    single { PromoCodeValidationUseCase(get()) }
    single { PaymentStatusUseCase(get()) }
    single { SubscriptionLimitUseCase(get()) }
    single { SubscriptionPlanUseCase(get()) }
    single { GetChildAppsUseCase(get()) }

    single { ChatConnectionManager(get(), get()) }
    single { GetPoliciesFromServerUseCase(get() ) }
    single { GetCoinPackageListUseCase(get() ) }
    single { ParentRequestsUseCase(get() ) }
    single { UpdateParentRequestStatusUseCase(get() ) }
    single { PurchaseIApPremiumUseCase(get() ) }
    single { ChildInfoEditUseCase(get() ) }
    single { UserInfoEditUseCase(get()) }
    single { DeleteMessageUseCase(get()) }
    single { PurchaseCoinUseCase(get()) }
    single { CheckUpdateUseCase(get()) }
    single { ObserveInstallEventsUseCase(get()) }
    single { CompleteFlexibleUpdateUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { PermissionStatusUseCase(get()) }
    single { VideoTutorialUseCase(get()) }
    factory { GetPaymentTransactionsUseCase(get()) }

    single { GetTodosUseCase(get()) }
    single { GetTodoByIdUseCase(get()) }
    single { uz.tikoncha_parent.domain.use_case.todo.CreateTodoUseCase(get()) }
    single { uz.tikoncha_parent.domain.use_case.todo.UpdateTodoUseCase(get()) }
    single { DeleteTodoUseCase(get()) }
    single { CompleteTodoUseCase(get()) }





    factory { LoginViewModel() }
    factory { OtpViewmodel(get() , get()) }
    factory { RegisterViewmodel(get()) }
    factory { ProfileViewModel(get(), get(), get(), get(), get()) }
    factory { AddChildScreenModel(get()) }
    factory { ChildConfirmViewModel() }
    factory { TaskListViewModel(get(), get(), get(), get()) }
    factory { CreateTaskViewModel(get(), get(), get()) }
    factory { StatisticViewModel(get(), get(), get(), get()) }
    factory { HomeViewModel(get(), get(), get(), get(), get(), get()) }

    factory {
        ChatViewModel(
            get(),
            get(),
            get()
        )
    }

    factory {
        ChatRoomViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    factory { NotificationViewModel(get(), get()) }
    factory { PolicyViewModel(get(), get(), get(), get()) }
    factory { TimeRuleSetupViewModel() }
    factory { LimitRuleSetupViewModel() }

    factory { PolicySetupViewModel(get(), get(), get()) }
    single { PolicySharedModel(get()) }
    factory { AppWebViewModel(get()) }
    factory { SleepTemplateSetupViewModel(get(), get(), get()) }

    factory { PaymentViewModel(get(), get(), get(), get(), get(), get()) }
    factory { SubscriptionPaymentViewModel(get(), get()) }
    factory { ParentRequestViewModel(get(), get()) }
    factory { CoinsViewModel(get(), get(), get()) }
    factory { (child: UserInfo) ->
        ChildInfoEditViewModel(
            childInfoEditUseCase = get(),
            child = child
        )
    }
    factory { (userInfo: UserInfo) ->
        UserInfoEditViewModel(
            userInfoEditUseCase = get(),
            userInfo = userInfo
        )
    }
    factory { CoinPurchaseViewModel(get(), get(), get()) }
    single { UpdateViewModel(get(), get(), get()) }
    factory { PlayerScreenModel(get(), get()) }
    factory { VideoTutorialScreenModel(get()) }
    factory { PaymentHistoryScreenModel(get()) }

}