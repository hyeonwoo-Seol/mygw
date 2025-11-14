package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpopdancepracticeai.ui.theme.KpopDancePracticeAITheme

/**
 * 프로필 화면 Composable
 * Scaffold로부터 innerPadding을 전달받습니다.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToPracticeSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit
) {
    // "통계", "업적", "설정" 탭 상태 관리
    var selectedTab by remember { mutableStateOf("통계") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), // 좌우 기본 여백
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(16.dp) // 각 항목(item) 간의 수직 간격
    ) {
        // --- 1. 내 프로필 카드 ---
        item {
            ProfileHeaderCard()
        }

        // --- 2. 탭 버튼 (통계, 업적, 설정) ---
        item {
            ProfileTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }

        // --- 3. 탭 선택에 따른 콘텐츠 표시 ---
        when (selectedTab) {
            "통계" -> {
                // --- 3-1. 통계 요약 (3개 카드) ---
                item {
                    StatisticsRow()
                }
                // --- 3-2. 진행중인 업적 요약 ---
                item {
                    AchievementsSummaryCard()
                }
                // --- 3-3. 획득한 뱃지 ---
                item {
                    AcquiredBadgesCard()
                }
            }
            "업적" -> {
                item {
                    // TODO: 업적 탭 콘텐츠
                    PlaceholderContent(text = "업적 전체 목록 (준비중)")
                }
            }
            "설정" -> {
                item {
                    SettingsContent(
                        onNavigateToProfileEdit = onNavigateToProfileEdit,
                        onNavigateToPracticeSettings = onNavigateToPracticeSettings,
                        onNavigateToNotificationSettings = onNavigateToNotificationSettings
                    )
                }
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 1. 상단 '내 프로필' 정보 카드
 */
@Composable
fun ProfileHeaderCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 (임시 아이콘)
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                tint = Color.LightGray
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 프로필 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "내 프로필",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "김원준", // 피그마 예시 이름
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 상세 스탯 (경험치, 레벨)
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    StatColumn(label = "경험치", value = "N/A")
                    StatColumn(label = "Level", value = "N/A")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 평균 정확도 프로그레스 바
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "평균 정확도",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "0/100",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { 0.0f }, // TODO: 실제 값 연동
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

/**
 * 1-1. '경험치', 'Level' 등 작은 스탯 표시용
 */
@Composable
fun StatColumn(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 2. "통계", "업적", "설정" 탭 버튼
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val tabs = listOf("통계", "업적", "설정")
        tabs.forEach { tabName ->
            val isSelected = selectedTab == tabName
            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(tabName) },
                label = {
                    Text(
                        text = tabName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(50.dp), // 알약 모양
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White.copy(alpha = 0.7f),
                    selectedContainerColor = Color.White,
                    labelColor = Color.Black.copy(alpha = 0.7f),
                    selectedLabelColor = Color.Black
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

/**
 * 3-1. 통계 요약 (3개 카드)
 */
@Composable
fun StatisticsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = "41H",
            label = "총 연습시간"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "5개",
            label = "완료한 곡 개수"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "89%",
            label = "평균 정확도"
        )
    }
}

/**
 * 3-1-1. 통계 카드 (재사용)
 */
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier.height(100.dp), // 카드 높이 고정
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 3-2. 진행중인 업적 요약 카드
 */
@Composable
fun AchievementsSummaryCard() {
    // 임시 데이터
    val achievements = listOf(
        "완벽주의자" to 0.8f,
        "연습 벌레" to 0.3f,
        "BTS 마스터" to 0.5f,
        "챌린지 헌터" to 0.1f
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🏆 진행중인 업적 요약",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            achievements.forEach { (label, progress) ->
                AchievementProgressItem(
                    label = label,
                    progress = progress,
                    progressText = "${(progress * 100).toInt()}%"
                )
            }
        }
    }
}

/**
 * 3-2-1. 업적 진행도 항목 (재사용)
 */
@Composable
fun AchievementProgressItem(
    label: String,
    progress: Float,
    progressText: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = progressText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.width(36.dp), // 너비 고정으로 정렬 맞춤
                textAlign = TextAlign.End
            )
        }
    }
}

/**
 * 3-3. 획득한 뱃지 카드
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AcquiredBadgesCard() {
    // 임시 데이터
    val badges = mapOf(
        "BTS 마스터" to Color(0xFFEBEBFF),
        "NewJeans 팬" to Color(0xFFD6F5FF),
        "BLACKPINK 전문가" to Color(0xFFFFD6EB),
        "초급자 졸업" to Color(0xFFD9FFE5),
        "중급자" to Color(0xFFFFFAD6)
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "✨ 획득한 뱃지",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // FlowRow: 칩이 화면 너비를 넘어가면 자동으로 줄바꿈
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                badges.forEach { (text, color) ->
                    BadgeChip(text = text, color = color)
                }
            }
        }
    }
}

/**
 * 3-3-1. 뱃지 칩 (재사용)
 */
@Composable
fun BadgeChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

/**
 * "업적" 탭을 위한 임시 플레이스홀더
 */
@Composable
fun PlaceholderContent(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
    }
}

// ---------------------------------------------------
// 설정 탭 콘텐츠
// ---------------------------------------------------

/**
 * 4. "설정" 탭에 표시될 콘텐츠
 */
@Composable
fun SettingsContent(
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToPracticeSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // 설정 타이틀
        Text(
            text = "설정",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 설정 메뉴 카드
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp), // 프로필 카드와 통일
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                SettingsMenuItem(
                    text = "프로필 설정",
                    icon = Icons.Outlined.Person,
                    iconBgColor = Color(0xFFEBF0FF), // 피그마 참고
                    onClick = onNavigateToProfileEdit
                )
                SettingsMenuDivider()
                SettingsMenuItem(
                    text = "연습 화면 설정",
                    icon = Icons.Outlined.Tv,
                    iconBgColor = Color(0xFFF0EFFF), // 피그마 참고
                    onClick = onNavigateToPracticeSettings
                )
                SettingsMenuDivider()
                SettingsMenuItem(
                    text = "알림 설정",
                    icon = Icons.Outlined.Notifications,
                    iconBgColor = Color(0xFFFFF9E6), // 피그마 참고
                    onClick = onNavigateToNotificationSettings
                )
                SettingsMenuDivider()
                SettingsMenuItem(
                    text = "개인정보 보호 및 권한",
                    icon = Icons.Outlined.Shield,
                    iconBgColor = Color(0xFFE6F7EB), // 피그마 참고
                    onClick = { /* TODO: 개인정보 화면으로 이동 */ }
                )
                SettingsMenuDivider()
                SettingsMenuItem(
                    text = "앱 정보",
                    icon = Icons.Outlined.Info,
                    iconBgColor = Color(0xFFF3F4F6), // 피그마 참고
                    onClick = { /* TODO: 앱 정보 화면으로 이동 */ }
                )
                SettingsMenuDivider()
                SettingsMenuItem(
                    text = "회원 탈퇴",
                    icon = Icons.Outlined.ExitToApp,
                    iconBgColor = Color(0xFFFFF0F0), // 피그마 참고
                    textColor = Color.Red, // 빨간색 텍스트
                    onClick = { /* TODO: 회원 탈퇴 다이얼로그 표시 */ }
                )
            }
        }
    }
}

/**
 * 4-1. 설정 메뉴 아이템 (재사용)
 */
@Composable
fun SettingsMenuItem(
    text: String,
    icon: ImageVector,
    iconBgColor: Color,
    textColor: Color = Color.Unspecified, // 기본 텍스트 색상 사용
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp), // 여백 조정
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 아이콘 배경
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color.Black.copy(alpha = 0.8f) // 아이콘 색상 통일
            )
        }

        // 텍스트
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor, // 지정된 경우 해당 색상 사용
            modifier = Modifier.weight(1f)
        )

        // 오른쪽 화살표 아이콘
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.7f)
        )
    }
}

/**
 * 4-2. 설정 메뉴 구분선
 */
@Composable
fun SettingsMenuDivider() {
    HorizontalDivider( // ⭐️ [오류 수정] Divider -> HorizontalDivider
        color = Color.Gray.copy(alpha = 0.15f),
        thickness = 1.dp,
        // 좌우 여백을 아이콘 영역 다음부터 시작하도록 조정
        modifier = Modifier.padding(start = 76.dp, end = 20.dp)
    )
}


// --- 미리보기 ---
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KpopDancePracticeAITheme {
        ProfileScreen(
            paddingValues = PaddingValues(),
            onNavigateToProfileEdit = {},
            onNavigateToPracticeSettings = {},
            onNavigateToNotificationSettings = {}
        )
    }
}