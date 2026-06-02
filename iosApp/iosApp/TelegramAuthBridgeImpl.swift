import Foundation
import TelegramLogin
import ComposeApp

final class TelegramAuthBridgeImpl: TelegramAuthBridge {

    func startLogin(callback: TelegramAuthBridgeCallback) {
        MainActor.assumeIsolated {
            TelegramLogin.login { result in
                switch result {
                case .success(let data):
                    callback.onSuccess(idToken: data.idToken)
                case .failure(let error):
                    if let tgError = error as? TelegramLoginError, case .cancelled = tgError {
                        callback.onCancel()
                    } else {
                        callback.onError(message: "\(error)")
                    }
                }
            }
        }
    }
}
