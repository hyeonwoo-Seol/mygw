package com.example.kpopdancepracticeai.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// --- 1. 내비게이션 경로(Route) 정의 ---
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Login : Screen("login", "로그인", Icons.Default.Home) // 아이콘은 사용되지 않음
    object Home : Screen("home", "홈", Icons.Default.Home)
    object Search : Screen("search", "검색", Icons.Default.Search)
    object Analysis : Screen("analysis", "분석", Icons.Default.Analytics) // 탭에는 없지만 경로는 유지
    object Profile : Screen("profile", "프로필", Icons.Default.Person)
    object ProfileEdit : Screen("profileEdit", "프로필 설정", Icons.Default.Edit)
    object PracticeSettings : Screen("practiceSettings", "연습 화면 설정", Icons.Outlined.Settings)
    object NotificationSettings : Screen("notificationSettings", "알림 설정", Icons.Outlined.Notifications)
}

// 피그마 디자인에 맞춰 3개 탭만 표시
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Profile,
)

// --- 2. 앱의 메인 Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KpopDancePracticeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 로그인/프로필수정/연습설정/알림설정 화면에서 상/하단 바 숨김
    val showMainBars = currentRoute != Screen.Login.route &&
            currentRoute != Screen.ProfileEdit.route &&
            currentRoute != Screen.PracticeSettings.route &&
            currentRoute != Screen.NotificationSettings.route

    // 피그마 디자인의 그라데이션 배경
    val appGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDE3FF), // 상단 연한 파랑
            Color(0xFFF0E8FF)  // 하단 연한 보라
        )
    )

    // 그라데이션을 Scaffold 배경으로 적용
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent, // Scaffold 배경을 투명하게
            topBar = {
                AnimatedVisibility(
                    visible = showMainBars,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                ) {
                    AppTopBar()
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = showMainBars,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    AppBottomNavigationBar(navController = navController)
                }
            }
        ) { innerPadding ->
            // --- 3. 내비게이션 호스트 ---
            AppNavHost(
                navController = navController,
                innerPadding = innerPadding
            )
        }
    }
}

// 상단 앱 바 (피그마 디자인 반영)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "KPOP 댄스 연습 앱",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent // 그라데이션 배경이 보이도록 투명화
        )
    )
}

// --- 4. 하단 네비게이션 바 ---
@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.8f), // 반투명 흰색
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(50.dp)), // 둥근 모서리
        tonalElevation = 4.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // 클릭 시 배경색 없음
                )
            )
        }
    }
}

// --- 5. 내비게이션 경로 설정 ---
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // 로그인 화면 (패딩 적용 X)
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 홈 화면 (Scaffold 패딩 적용)
        composable(Screen.Home.route) {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                paddingValues = innerPadding
            )
        }

        // 검색 화면 (Scaffold 패딩 적용)
        composable(Screen.Search.route) {
            SearchScreen(
                paddingValues = innerPadding
            )
        }

        // 분석 화면 (Scaffold 패딩 적용)
        composable(Screen.Analysis.route) {
            PlaceholderScreen(screenName = "분석", paddingValues = innerPadding)
        }

        // 프로필 화면 (Scaffold 패딩 적용)
        composable(Screen.Profile.route) {
            ProfileScreen(
                paddingValues = innerPadding,
                onNavigateToProfileEdit = {
                    navController.navigate(Screen.ProfileEdit.route)
                },
                onNavigateToPracticeSettings = {
                    navController.navigate(Screen.PracticeSettings.route)
                },
                onNavigateToNotificationSettings = {
                    navController.navigate(Screen.NotificationSettings.route)
                }
            )
        }

        // 프로필 설정 화면 (전체 화면, innerPadding 적용 X)
        composable(Screen.ProfileEdit.route) {
            ProfileEditScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 연습 화면 설정 (전체 화면, innerPadding 적용 X)
        composable(Screen.PracticeSettings.route) {
            PracticeSettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 알림 설정 화면 (전체 화면, innerPadding 적용 X)
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}