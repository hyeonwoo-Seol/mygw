package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
    object Login : Screen("login", "로그인", Icons.Default.Home) // 로그인 화면
    object Home : Screen("home", "홈", Icons.Default.Home)
    object Search : Screen("search", "검색", Icons.Default.Search)
    object Analysis : Screen("analysis", "분석", Icons.Default.Analytics)
    object Profile : Screen("profile", "프로필", Icons.Default.Person)
}

// ⭐️ [변경] 하단 탭에 표시할 화면 리스트 (피그마 디자인에 맞춰 'Analysis' 제거)
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    // Screen.Analysis, // 피그마 디자인에 따라 탭에서 제거
    Screen.Profile
)

// --- 2. 앱의 메인 Composable ---
@Composable
fun KpopDancePracticeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 로그인 화면에서는 하단 탭을 숨겨야 함
    val showBottomBar = currentRoute != Screen.Login.route

    Scaffold(
        // ⭐️ [변경] bottomBar 람다를 Box로 감싸서 'floating' 효과를 위한 패딩 적용
        bottomBar = {
            if (showBottomBar) {
                // Box로 감싸서 하단과 좌우에 패딩을 주어 '떠 있는' 효과를 줍니다.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp) // 좌우, 하단 패딩
                ) {
                    AppBottomNavigationBar(navController = navController)
                }
            }
        }
    ) { innerPadding ->
        // --- 3. 내비게이션 호스트 (화면 교체) ---
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// --- 4. 하단 네비게이션 바 (중앙 관리) ---
@Composable
fun AppBottomNavigationBar(navController: NavController) {
    // ⭐️ [변경] NavigationBar에 clip 모디파이어를 적용해 둥근 모서리 생성
    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(28.dp)) // 둥근 모서리 적용
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // ⭐️ [변경] 3개의 아이템(bottomNavItems)만 순회
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // 스택 맨 위로 팝업 (뒤로가기 시 중복 방지)
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // 같은 탭 재선택 시 리로드 방지
                        launchSingleTop = true
                        // 이전 상태 복원
                        restoreState = true
                    }
                }
            )
        }
    }
}

// --- 5. 내비게이션 경로 설정 (NavHost) ---
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route, // 앱 시작 시 첫 화면은 '로그인'
        modifier = modifier
    ) {
        // 로그인 화면
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // 로그인 성공 시, '홈' 화면으로 이동
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 홈 화면
        composable(Screen.Home.route) {
            HomeScreen(
                onSearchClick = {
                    // 홈 화면의 검색창 클릭 시 '검색' 탭으로 이동
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        // 검색 화면
        composable(Screen.Search.route) {
            SearchScreen()
        }

        // 분석 화면 (임시) - 탭에서는 제거되었지만 경로는 남아있음
        composable(Screen.Analysis.route) {
            PlaceholderScreen(screenName = "분석")
        }

        // 프로필 화면 (임시)
        composable(Screen.Profile.route) {
            PlaceholderScreen(screenName = "프로필")
        }
    }
}