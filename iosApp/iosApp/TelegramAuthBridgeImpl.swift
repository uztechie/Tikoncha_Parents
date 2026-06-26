import Foundation
import TelegramLogin
import ComposeApp

final class TelegramAuthBridgeImpl: TelegramAuthBridge {

    func startLogin(callback: TelegramAuthBridgeCallback) {
        print("TG_BRIDGE login boshlanmoqda, redirect=\(TelegramConfig.shared.redirectHost)")
        MainActor.assumeIsolated {
            TelegramLogin.login { result in
                print("TG_BRIDGE completion keldi: \(result)")   // ← bu chiqyaptimi?
                switch result {
                case .success(let data): callback.onSuccess(idToken: data.idToken)
                case .failure(let error):
                    if let tgError = error as? TelegramLoginError, case .cancelled = tgError {
                        callback.onCancel()
                    } else { callback.onError(message: "\(error)") }
                }
            }
        }
    }
}
