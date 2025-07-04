import SwiftUI

@main
struct iOSApp: App {
    
    init(){
        try? YMKMapKit.setApiKey("SIZNING_API_KALITINGIZ")
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
