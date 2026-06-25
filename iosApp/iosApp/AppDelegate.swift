import UIKit
import UserNotifications
import FirebaseCore
import FirebaseMessaging
import ComposeApp
import TelegramLogin

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        
        if #available(iOS 14.0, *) {
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]){ granted, error in
                print("Permission granted: \(granted)")
            }
            UNUserNotificationCenter.current().delegate = self
            Messaging.messaging().delegate = self
        }
        application.registerForRemoteNotifications()
        return true
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("Device token:\(deviceToken)")
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        guard userActivity.activityType == NSUserActivityTypeBrowsingWeb,
              let url = userActivity.webpageURL else { return false }
        print("AppDelegate continue: \(url)")
        if TelegramConfig.shared.isTelegramHost(host: url.host) {
            TelegramLogin.handle(url)
            return true
        }
        return false
    }

    func application(_ app: UIApplication, open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        print("AppDelegate open: \(url)")
        if TelegramConfig.shared.isTelegramHost(host: url.host) {
            TelegramLogin.handle(url)
            return true
        }
        return false
    }
    
    
    
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                    willPresent notification: UNNotification,
                                    withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
            let raw = notification.request.content.userInfo["payload"] as? String
            IosPushEntry.shared.onMessage(payloadRaw: raw)
            completionHandler([.banner, .badge, .sound])
        }

        func userNotificationCenter(_ center: UNUserNotificationCenter,
                                    didReceive response: UNNotificationResponse,
                                    withCompletionHandler completionHandler: @escaping () -> Void) {
            let raw = response.notification.request.content.userInfo["payload"] as? String
            IosPushEntry.shared.onTap(payloadRaw: raw)
            completionHandler()
        }
    
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
         if let token = fcmToken { IosPushEntry.shared.submitToken(token: token) }
     }
}
