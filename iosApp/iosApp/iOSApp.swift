import SwiftUI
import ComposeApp
import YandexMapsMobile

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        AppKt.doInitMapKit()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
