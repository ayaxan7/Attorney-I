// iosApp/AppDelegate.swift
import UIKit
import ComposeApp  // exposes KMP code to Swift

//@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {

        KMPInitializer().configureFirebase()   // call into Kotlin; name may appear as `KMPInitializer` or `KmpInitializer`
        return true
    }
}
