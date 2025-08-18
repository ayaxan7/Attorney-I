import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize Firebase and other platform-specific setup
       AppContextKt.AppContext.shared.setUp()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}