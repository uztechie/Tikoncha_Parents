import SwiftUI
import ComposeApp
import YandexMapsMobile

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        YMKMapKit.setApiKey("21612db3-4394-4fde-b579-d2e7a1f9afa3")
        YMKMapKit.setLocale("uz_UZ")  // yoki "en_US"
                let _ = YMKMapKit.sharedInstance()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
