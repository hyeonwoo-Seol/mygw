package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
    object Login : Screen("login", "로그인", Icons.Default.Home)
    object Home : Screen("home", "홈", Icons.Default.Home)
    object Search : Screen("search", "검색", Icons.Default.Search)
    object Analysis : Screen("analysis", "분석", Icons.Default.Analytics) // 탭에는 없지만 경로는 유지
    object Profile : Screen("profile", "프로필", Icons.Default.Person)
}

// ⭐️ [변경] 피그마 디자인에 맞춰 3개 탭만 표시
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Profile
)

// --- 2. 앱의 메인 Composable (⭐️ 대폭 수정) ---
@OptIn(ExperimentalMaterial3Api::class) // TopAppBar를 위해 추가
@Composable
fun KpopDancePracticeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 로그인 화면에서는 상단/하단 탭 숨김
    val showBars = currentRoute != Screen.Login.route

    // ⭐️ [추가] 피그마 디자인의 그라데이션 배경
    val appGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDE3FF), // 상단 연한 파랑
            Color(0xFFF0E8FF)  // 하단 연한 보라
        )
    )

    // ⭐️ [추가] 그라데이션을 Scaffold 배경으로 적용
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent, // Scaffold 배경을 투명하게
            topBar = {
                // ⭐️ [추가] 상단 앱 바 (로그인 화면 제외)
                if (showBars) {
                    AppTopBar()
                }
            },
            bottomBar = {
                // ⭐️ [수정] 피그마 디자인의 '떠 있는' 하단 바
                if (showBars) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp) // 좌우, 하단 여백
                    ) {
                        AppBottomNavigationBar(navController = navController)
                    }
                }
            }
        ) { innerPadding ->
            // --- 3. 내비게이션 호스트 (⭐️ 수정) ---
            AppNavHost(
                navController = navController,
                innerPadding = innerPadding // Scaffold가 계산한 패딩을 전달
            )
        }
    }
}

// ⭐️ [추가] 상단 앱 바 (피그마 디자인 반영)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "KPOP 댄스 연습 앱",
                style = MaterialTheme.typography.headlineLarge, // 32.sp
                fontWeight = FontWeight(600),
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // 제목 중앙 정렬
            )
        },
        // 배경을 투명하게, 스크롤 시 색상 변경 없음
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

// --- 4. 하단 네비게이션 바 (⭐️ 수정) ---
@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar(
        // ⭐️ [수정] 둥근 모서리 + 흰색 배경
        modifier = Modifier.clip(RoundedCornerShape(28.dp)),
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen -> // 3개 아이템만 순회
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
                }
            )
        }
    }
}

// --- 5. 내비게이션 경로 설정 (⭐️ 수정) ---
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues // ⭐️ [추가] Scaffold의 패딩을 받음
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier // ⭐️ [수정] 패딩을 NavHost 자체에 적용하지 않음
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

        // 홈 화면 (⭐️ Scaffold 패딩 적용)
        composable(Screen.Home.route) {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                paddingValues = innerPadding // ⭐️ [추가] 패딩 전달
            )
        }

        // 검색 화면 (⭐️ Scaffold 패딩 적용)
        composable(Screen.Search.route) {
            SearchScreen(
                paddingValues = innerPadding // ⭐️ [추가] 패딩 전달
            )
        }

        // 분석 화면 (⭐️ Scaffold 패딩 적용)
        composable(Screen.Analysis.route) {
            PlaceholderScreen(screenName = "분석", paddingValues = innerPadding)
        }

        // 프로필 화면 (⭐️ Scaffold 패딩 적용)
        composable(Screen.Profile.route) {
            PlaceholderScreen(screenName = "프로필", paddingValues = innerPadding)
        }
    }
}