import Shared
import SwiftUI
import UIKit

// MARK: - KMP Bridge

struct ComposeView: UIViewControllerRepresentable {
    let topLevelRoute: TopLevelRoute

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(topLevelRoute: topLevelRoute)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// MARK: - Navigation Types

@available(iOS 26.0, *)
struct RouteWrapper: Hashable, Identifiable {
    let id = UUID()
    let route: AppRoute

    static func == (lhs: RouteWrapper, rhs: RouteWrapper) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
}

// MARK: - Coordinators

@available(iOS 26.0, *)
@Observable
class TabNavigationCoordinator {
    var path: [RouteWrapper] = []

    func push(_ route: AppRoute) {
        path.append(RouteWrapper(route: route))
    }

    func pop() {
        if !path.isEmpty {
            path.removeLast()
        }
    }

    func popToRoot() {
        path.removeAll()
    }
}

@available(iOS 26.0, *)
@Observable
class AppNavigationCoordinator {

    var selectedTabIndex: Int = 1

    let tabCoordinators: [Int: TabNavigationCoordinator] = [
        1: TabNavigationCoordinator(),  // Home
        2: TabNavigationCoordinator(),  // Search
        3: TabNavigationCoordinator(),  // Profile
    ]

    func tabIndex(for route: AppRoute) -> Int {
        switch route {
        case is Home,
            is HomeDetails:
            return 1
        case is Search,
            is SearchDetails:
            return 2
        case is Profile,
            is ProfileDetails,
            is ProfileDetailsNext:
            return 3
        default:
            assertionFailure("Unhandled route: \(route)")
            return selectedTabIndex
        }
    }

    /// Открывает любой роут — переключает вкладку и обновляет стек.
    /// Порядок операций важен: стек обновляется до смены selectedTabIndex,
    /// чтобы isTabBarHidden в TabContentView пересчитался без мигания.
    func open(route: AppRoute) {
        let targetTab = tabIndex(for: route)

        guard let coordinator = tabCoordinators[targetTab] else {
            assertionFailure("No coordinator for tab \(targetTab)")
            return
        }

        coordinator.popToRoot()
        if !(route is TopLevelRoute) {
            coordinator.push(route)
        }

        if targetTab != selectedTabIndex {
            tabCoordinators[selectedTabIndex]?.popToRoot()
        }

        selectedTabIndex = targetTab
    }
}

// MARK: - UIViewControllerRepresentable Bridges

/// Рутовый экран вкладки — KMP экран верхнего уровня
@available(iOS 26.0, *)
struct NativeNavComposeView: UIViewControllerRepresentable {
    let topLevelRoute: TopLevelRoute
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            topLevelRoute: topLevelRoute,
            onNavigate: { route in
                coordinator.push(route)
            },
            onActivate: { route in
                appCoordinator.open(route: route)
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

/// Экран внутри стека — любой detail экран
@available(iOS 26.0, *)
struct DetailComposeView: UIViewControllerRepresentable {
    let route: AppRoute
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.ScreenViewController(
            route: route,
            onNavigate: { route in
                coordinator.push(route)
            },
            onGoBack: {
                coordinator.pop()
            },
            onSet: { route in
                appCoordinator.open(route: route)
            },
            onActivate: { route in
                appCoordinator.open(route: route)
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// MARK: - Tab Bar Visibility

func shouldHideTabBar(for route: AppRoute) -> Bool {
    switch route {
    case is HomeDetails: return true
    case is SearchDetails: return true
    case is ProfileDetails: return true
    case is ProfileDetailsNextText: return true
    default: return false
    }
}
func shouMenu(for route: AppRoute) -> Bool {
    switch route {
    case is ProfileDetailsNextText: return true
    case is ProfileDetailsNext: return true
    default: return false
    }
}

// MARK: - Views

/// Одна вкладка с NavigationStack.
/// isTabBarHidden читается из coordinator.path.last синхронно —
/// @Observable гарантирует пересчёт при каждом изменении стека.
@available(iOS 26.0, *)
struct TabContentView: View {
    let topLevelRoute: TopLevelRoute
    let coordinator: TabNavigationCoordinator
    let appCoordinator: AppNavigationCoordinator
    let title: String

    var isTabBarHidden: Bool {
        guard let route = coordinator.path.last?.route else { return false }
        return shouldHideTabBar(for: route)
    }
    var isMenu: Bool {
        guard let route = coordinator.path.last?.route else { return false }
        return shouMenu(for: route)
    }

    var body: some View {
        NavigationStack(
            path: Binding(
                get: { coordinator.path },
                set: { coordinator.path = $0 }
            )
        ) {
            NativeNavComposeView(
                topLevelRoute: topLevelRoute,
                coordinator: coordinator,
                appCoordinator: appCoordinator
            )
            .ignoresSafeArea(.all)
            .navigationTitle(title)
            .navigationBarHidden(true)
            .navigationDestination(for: RouteWrapper.self) { wrapper in
                DetailComposeView(
                    route: wrapper.route,
                    coordinator: coordinator,
                    appCoordinator: appCoordinator
                )
                .toolbar {
                    ToolbarItem(placement: .topBarTrailing) {
                        if isMenu {
                            Menu {
                                Button("Изменить текст") {
                                    IosEventHandler().sendNotification(isEvent: true)
                                }
                            } label: {
                                Image(systemName: "ellipsis")
                            }
                        }
                    }
                }
                .ignoresSafeArea(.all)
                .navigationTitle(wrapper.route.title ?? "")
                .toolbarTitleDisplayMode(.inline)

            }
        }
        .toolbar(isTabBarHidden ? .hidden : .visible, for: .tabBar)
    }
}

/// Корневой экран приложения — TabView с тремя вкладками
@available(iOS 26.0, *)
struct NativeNavContentView: View {
    @State private var appCoordinator = AppNavigationCoordinator()

    var body: some View {
        TabView(selection: $appCoordinator.selectedTabIndex) {
            Tab(String(localized: "Home"), systemImage: "clock", value: 1) {
                TabContentView(
                    topLevelRoute: Home(),
                    coordinator: appCoordinator.tabCoordinators[1]!,
                    appCoordinator: appCoordinator,
                    title: String(localized: "Schedule")
                )
            }
            Tab(String(localized: "Search"), systemImage: "person.2", value: 2) {
                TabContentView(
                    topLevelRoute: Search(),
                    coordinator: appCoordinator.tabCoordinators[2]!,
                    appCoordinator: appCoordinator,
                    title: String(localized: "Speakers")
                )
            }
            Tab(String(localized: "Profile"), systemImage: "trophy", value: 3) {
                TabContentView(
                    topLevelRoute: Profile(),
                    coordinator: appCoordinator.tabCoordinators[3]!,
                    appCoordinator: appCoordinator,
                    title: String(localized: "Golden Kodee")
                )
            }
        }
        .tabBarMinimizeBehavior(.automatic)
    }
}

// MARK: - Entry Point

struct ContentView: View {
    var body: some View {
        if #available(iOS 26.0, *) {
            NativeNavContentView()
        } else {
            ComposeView(topLevelRoute: Home())
                .ignoresSafeArea(.all)
        }
    }
}
