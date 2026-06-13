import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        // InitIosKt.initialize()
        InitIosKt.initialize( onKoinStart: { koinApp in koinApp})
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
