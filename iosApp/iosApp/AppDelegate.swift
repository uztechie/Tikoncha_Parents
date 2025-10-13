import FirebaseCore
import FirebaseMessaging
import UIKit
import UserNotifications
import ComposeApp

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, UISplitViewControllerDelegate, MessagingDelegate {
    func application(_ application: UIApplication,
                         didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

            FirebaseApp.configure()

            let center = UNUserNotificationCenter.current()
            center.delegate = self
            center.requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in }

            UIApplication.shared.registerForRemoteNotifications()

            Messaging.messaging().delegate = self
            return true
        }

        func application(_ application: UIApplication,
                         didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
            Messaging.messaging().apnsToken = deviceToken
        }

        // Foregroundda ko‘rsatish va KMP’ga uzatish
        func userNotificationCenter(_ center: UNUserNotificationCenter,
                                    willPresent notification: UNNotification,
                                    withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
            completionHandler([.banner, .sound, .badge])

            let c = notification.request.content
            let ms = (c.userInfo["date"] as? String).flatMap { Int64($0) }
                ?? Int64(Date().timeIntervalSince1970 * 1000)
            
            KmpPushBridge().onMessage(
                        title: c.title,
                        body: c.body,
                        dateMillis: ms
                    )
        }

        // Notificationga bosilganda (background→foreground)
        func userNotificationCenter(_ center: UNUserNotificationCenter,
                                    didReceive response: UNNotificationResponse,
                                    withCompletionHandler completion: @escaping () -> Void) {
            let c = response.notification.request.content
            let ms = (c.userInfo["date"] as? String).flatMap { Int64($0) }
                ?? Int64(Date().timeIntervalSince1970 * 1000)
            
          

            KmpPushBridge().onMessage(
                        title: c.title,
                        body: c.body,
                        dateMillis: ms
                    )
            
            completion()
        }

        // (ixtiyoriy) Tokenni olish (test uchun log)
        func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
            print("FCM Token: \(fcmToken ?? "-")")
        }
}
