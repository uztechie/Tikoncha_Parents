import UIKit
import UserNotifications
import FirebaseCore
import FirebaseMessaging
import ComposeApp


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
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        if #available(iOS 14.0, *) { completionHandler([.banner, .badge, .sound]) }
           else { completionHandler([.alert, .badge, .sound]) }
        let c = notification.request.content
           let raw = c.userInfo["payload"] as? String
           
        print("Notification is received B=\(raw)")
        KmpFcmRouterBridge().handle(rawPayload: raw, fallbackTitle: c.title, fallbackBody: c.body, uiReady: false)
        KmpDeepLinkBridge().openFromPayload(rawPayload: raw, fallbackTitle: c.title, fallbackBody: c.body, uiReady: false)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let c = response.notification.request.content
        let raw = c.userInfo["payload"] as? String
        let isActive = UIApplication.shared.applicationState == .active
        
        print("Notification is received F=\(raw)")
        
        KmpFcmRouterBridge().handle(rawPayload: raw, fallbackTitle: c.title, fallbackBody: c.body, uiReady: isActive)
        KmpDeepLinkBridge().openFromPayload(rawPayload: raw, fallbackTitle: c.title, fallbackBody: c.body, uiReady: isActive)
        
        completionHandler()
    }
    
    
    @objc func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("FCM_TOKEN=\(String(describing: fcmToken))")
        if let t = fcmToken {
            print("FCM_TOKEN=\(t)")
            KmpTokenBridge().onNewToken(token: t)
        }
    }
}
