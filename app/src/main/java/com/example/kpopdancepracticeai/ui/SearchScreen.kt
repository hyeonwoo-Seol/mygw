package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpopdancepracticeai.ui.theme.KpopDancePracticeAITheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen() {
    // 1. 상태 관리: 검색어, 탭, 필터 선택 상태
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("search") } // "search" 탭을 기본값으로

    // 필터 선택 상태 (null은 "선택 안 함"을 의미)
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedArtist by remember { mutableStateOf<String?>(null) }
    var selectedTimeline by remember { mutableStateOf<String?>(null) }
    var selectedTempo by remember { mutableStateOf<String?>(null) }

    Scaffold(
        // 2. 하단 네비게이션 바 (홈, 검색, 프로필)
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("홈") },
                    selected = selectedTab == "home",
                    onClick = { selectedTab = "home" /* TODO: 홈 화면 이동 */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("검색") },
                    selected = selectedTab == "search",
                    onClick = { selectedTab = "search" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Analysis") },
                    label = { Text("분석") },
                    selected = selecteddTab == "analysis",
                    onClick = { selectedTab = "analysis" /* TODO: 분석 화면 이동 */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("프로필") },
                    selected = selectedTab == "profile",
                    onClick = { selectedTab = "profile" /* TODO: 프로필 화면 이동 */ }
                )
            }
        }
    ) { innerPadding ->
        // 3. 스크롤 가능한 본문
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 하단 탭에 가려지지 않도록 패딩 적용
                .padding(horizontal = 16.dp), // 좌우 여백
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 화면 타이틀 ---
            item {
                Text(
                    text = "검색",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // --- 4. 검색 입력창 (TextField) ---
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("노래, 아티스트, 챌린지 검색") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp), // 피그마의 둥근 모서리
                    singleLine = true
                )
            }

            // --- 5. 검색 필터 (Card 안에 배치) ---
            item {
                Surface( // 피그마의 '검색 배경'
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, Color(0xffd6deff)) // 피그마의 테두리
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // --- 난이도별 검색 ---
                        FilterSection(
                            title = "난이도별 검색",
                            options = listOf("쉬움", "보통", "어려움"),
                            selectedOption = selectedDifficulty,
                            onOptionSelected = { selectedDifficulty = it }
                        )

                        // --- 아티스트 ---
                        FilterSection(
                            title = "아티스트",
                            options = listOf("보이그룹", "걸그룹"),
                            selectedOption = selectedArtist,
                            onOptionSelected = { selectedArtist = it }
                        )

                        // --- 타임라인 ---
                        FilterSection(
                            title = "타임라인",
                            options = listOf("최신곡", "인기곡"),
                            selectedOption = selectedTimeline,
                            onOptionSelected = { selectedTimeline = it }
                        )

                        // --- 탬포 ---
                        FilterSection(
                            title = "탬포",
                            options = listOf("빠른 탬포", "보통 탬포", "느린 탬포"),
                            selectedOption = selectedTempo,
                            onOptionSelected = { selectedTempo = it }
                        )
                    }
                }
            }

            // --- 6. 검색 버튼 ---
            item {
                Button(
                    onClick = {
                        // TODO: ViewModel을 통해 검색 로직 호출
                        // (searchText, selectedDifficulty, selectedArtist, ...)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("검색", fontSize = 16.sp, fontWeight = FontWeight(500))
                }
            }

            // --- 하단 여백 ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// 7. 재사용 가능한 필터 섹션
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        // 8. FlowRow: 칩이 화면 너비를 넘어가면 자동으로 줄바꿈
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 칩 사이의 가로 간격
        ) {
            options.forEach { option ->
                val isSelected = selectedOption == option

                // 피그마 디자인의 커스텀 색상 가져오기
                val chipColors = getChipColors(option = option)

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        // 이미 선택된 것을 다시 누르면 선택 해제
                        if (isSelected) onOptionSelected(null)
                        else onOptionSelected(option)
                    },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        // 선택되었을 때의 색상
                        selectedContainerColor = chipColors.container,
                        selectedLabelColor = chipColors.content,
                        // 선택 안 되었을 때의 색상
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        selectedBorderColor = chipColors.border,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.5.dp
                    )
                )
            }
        }
    }
}

// 9. 피그마 디자인에 맞춘 칩 색상 반환 헬퍼
private data class ChipUiColors(
    val container: Color,
    val content: Color,
    val border: Color
)

@Composable
private fun getChipColors(option: String): ChipUiColors {
    return when (option) {
        // 난이도
        "쉬움" -> ChipUiColors(Color(0xfff0fdf4), Color(0xff00a63e), Color(0xff7bf1a8))
        "보통" -> ChipUiColors(Color(0xfffefce8), Color(0xffd08700), Color(0xffffdf20))
        "어려움" -> ChipUiColors(Color(0xfffef2f2), Color(0xffe7000b), Color(0xffffa2a2))
        // 아티스트
        "보이그룹" -> ChipUiColors(Color(0xffeff6ff), Color(0xff155dfc), Color(0xffbedbff))
        "걸그룹" -> ChipUiColors(Color(0xfffdf2f8), Color(0xffe60076), Color(0xfffccee8))
        // 타임라인
        "최신곡" -> ChipUiColors(Color(0xfffaf5ff), Color(0xff9810fa), Color(0xffe9d4ff))
        "인기곡" -> ChipUiColors(Color(0xfffff1f2), Color(0xffec003f), Color(0xffffccd3))
        // 탬포
        "빠른 탬포" -> ChipUiColors(Color(0xfffff7ed), Color(0xfff54900), Color(0xffffd6a7))
        "보통 탬포" -> ChipUiColors(Color(0xfff0fdfa), Color(0xff009689), Color(0xff96f7e4))
        "느린 탬포" -> ChipUiColors(Color(0xffeef2ff), Color(0xff4f39f6), Color(0xffc6d2ff))
        // 기본값
        else -> ChipUiColors(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            MaterialTheme.colorScheme.outline
        )
    }
}

// --- 미리보기 ---
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    KpopDancePracticeAITheme { // 본인의 테마 적용
        SearchScreen()
    }
}