import SwiftUI
import ComposeApp
import YandexMapsMobile
import TelegramLogin

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        // --- Yandex Maps ---
        YMKMapKit.setApiKey("21612db3-4394-4fde-b579-d2e7a1f9afa3")
        YMKMapKit.setLocale("uz_UZ")
        let _ = YMKMapKit.sharedInstance()

        // --- Telegram login ---
        TelegramLogin.configure(
            clientId: TelegramConfig.shared.CLIENT_ID,
            redirectUri: "https://\(TelegramConfig.shared.redirectHost)",
            scopes: ["openid", "profile", "phone"]
        )
        TelegramAuthBridgeProvider.shared.bridge = TelegramAuthBridgeImpl()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                // Universal Link (native Telegram qaytishi) — asosiy yo'l
                .onContinueUserActivity(NSUserActivityTypeBrowsingWeb) { activity in
                    if let url = activity.webpageURL {
                        handleTelegram(url)
                    }
                }
                // Zaxira yo'l
                .onOpenURL { url in
                    handleTelegram(url)
                }
        }
    }

    private func handleTelegram(_ url: URL) {
        if TelegramConfig.shared.isTelegramHost(host: url.host) {
            TelegramLogin.handle(url)
        }
    }
}
