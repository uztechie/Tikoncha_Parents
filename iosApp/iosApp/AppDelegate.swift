import UIKit
import UserNotifications
import FirebaseCore
import FirebaseMessaging
import ComposeApp // yoki `shared` — KMP framework nomingizga qarab

class AppDelegate: UIResponder,
                   UIApplicationDelegate,
                   UNUserNotificationCenterDelegate,
                   MessagingDelegate {

    private var hasAPNsToken = false

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        FirebaseApp.configure()

        // 1) Notification ruxsati va delegat
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        center.requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in }

        // 2) APNs ro'yxatga olish (main thread)
        DispatchQueue.main.async {
            UIApplication.shared.registerForRemoteNotifications()
        }

        // 3) FCM delegat
        Messaging.messaging().delegate = self

        // ❌ BU yerda FCM token so'ramaymiz (APNs hali yo‘q bo‘lishi mumkin)
        return true
    }

    // APNs device token → FCM
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
        hasAPNsToken = true

        // ✅ Endi FCM tokenni olish mumkin
        Messaging.messaging().token { token, error in
            if let token = token {
                print("FCM_TOKEN=\(token)")
                // faqat saqlang; yuborishni Home ekranda qiling (bearer tayyor bo‘lganda)
                KmpTokenBridge().onNewToken(token: token)
            } else if let error = error {
                print("FCM token error after APNs set: \(error)")
            }
        }
    }

    // Foreground ko'rsatish
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        if #available(iOS 14.0, *) {
            completionHandler([.banner, .sound, .badge])
        } else {
            completionHandler([.alert, .sound, .badge])
        }

        let c = notification.request.content
            let rawPayload = c.userInfo["payload"] as? String
            let title = c.title
            let body  = c.body
        
        KmpFcmRouterBridge().handle(rawPayload: rawPayload, fallbackTitle: title, fallbackBody: body)
    }

    // Tapping (background → foreground)
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completion: @escaping () -> Void) {

        let c = response.notification.request.content
            let rawPayload = c.userInfo["payload"] as? String
            let title = c.title
            let body  = c.body
        
        KmpFcmRouterBridge().handle(rawPayload: rawPayload, fallbackTitle: title, fallbackBody: body)
        KmpDeepLinkBridge().openFromPayload(rawPayload, fallbackTitle: title, fallbackBody: body)
            completion()
            completion()
    }

    // FCM token yangilanganda (faqat APNs bo'lsa foydali)
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        guard hasAPNsToken, let token = fcmToken else { return }
        print("FCM_TOKEN_UPDATE=\(token)")
        KmpTokenBridge().onNewToken(token: token)
    }
}

