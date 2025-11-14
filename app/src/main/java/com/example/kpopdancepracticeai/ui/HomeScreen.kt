package com.example.kpopdancepracticeai.ui // 본인의 패키지 이름 확인

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // ⭐️ [수정] clickable이 사용됩니다.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpopdancepracticeai.ui.theme.KpopDancePracticeAITheme // 테마 임포트

// --- 데이터 클래스 (임시) ---
data class Song(
    val id: String,
    val artist: String,
    val title: String,
    val views: String,
    val thumbnailUrl: String = "" // 이미지 URL
)

// 임시 데이터
val popularSongs = listOf(
    Song("1", "aespa", "Whiplash", "2.5만회 조회"),
    Song("2", "NMIXX", "Blue Valentine", "1.2만회 조회"),
    Song("3", "프로미스나인", "LIKE YOU BETTER", "3.4만회 조회")
)
val challengeSongs = listOf(
    Song("4", "aespa", "Whiplash", "2.5만회 조회"),
    Song("5", "NMIXX", "Blue Valentine", "1.2만회 조회"),
    Song("6", "프로미스나인", "LIKE YOU BETTER", "3.4만회 조회")
)

// --- 홈 화면 ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit, // 검색창 클릭 시 호출될 함수
    paddingValues: PaddingValues // ⭐️ [추가] Scaffold로부터 패딩을 받음
) {
    var searchText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        // ⭐️ [수정] contentPadding을 사용하여 Top/BottomBar 뒤에 콘텐츠가 가려지지 않게 함
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(16.dp) // 섹션 간 간격
    ) {
        // ⭐️ [삭제] "KPOP 댄스 연습 앱" Text item이 삭제됨 (AppTopBar로 이동)

        // --- 검색창 (클릭만 가능하도록 수정) ---
        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("연습할 곡을 검색하세요") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // 검색창 좌우 여백
                    .clickable { onSearchClick() }, // ⭐️ [오류 수정] clickable을 Modifier에 적용
                readOnly = true, // 실제 입력은 SearchScreen에서
                // enabled = false, // ⭐️ [오류 수정] 클릭을 받기 위해 enabled는 true여야 함 (기본값)
                // onClick = { onSearchClick() } // ⭐️ [오류 수정] 이 파라미터는 존재하지 않으므로 삭제
            )
        }

        // --- 섹션 1: 인기 급상승 안무 ---
        item {
            SectionTitle(title = "인기 급상승 안무")
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(popularSongs) { song ->
                    SongCard(
                        artist = song.artist,
                        title = song.title,
                        views = song.views,
                        onClick = { /* TODO: 곡 상세 화면으로 이동 */ }
                    )
                }
            }
        }

        // --- 섹션 2: 인기 급상승 챌린지 ---
        item {
            SectionTitle(title = "인기 급상승 챌린지")
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(challengeSongs) { song ->
                    SongCard(
                        artist = song.artist,
                        title = song.title,
                        views = song.views,
                        onClick = { /* TODO: 곡 상세 화면으로 이동 */ }
                    )
                }
            }
        }

        // --- 섹션 3: 최근 내가 조회한 안무 ---
        item {
            SectionTitle(title = "최근 내가 조회한 안무")
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 임시로 popularSongs 사용
                items(popularSongs.reversed()) { song ->
                    SongCard(
                        artist = song.artist,
                        title = song.title,
                        views = song.views,
                        onClick = { /* TODO: 곡 상세 화면으로 이동 */ }
                    )
                }
            }
        }
    }
}

// --- 공통 컴포넌트: 섹션 타이틀 ---
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium, // 16.sp, SemiBold
        fontWeight = FontWeight(600),
        // ⭐️ [수정] 패딩을 LazyColumn의 verticalArrangement로 옮겼으므로 top 패딩만 조절
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
    )
}

// --- 공통 컴포넌트: 곡 프로필 카드 (재사용) ---
@Composable
fun SongCard(
    artist: String,
    title: String,
    views: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp) // 카드의 너비 고정
            .clickable(onClick = onClick)
    ) {
        // 썸네일 이미지 (임시로 Box 사용)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = title, tint = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 곡 정보
        Text(
            text = artist,
            fontSize = 12.sp,
            fontWeight = FontWeight(400),
            color = Color(0x80000000), // 50% Black
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight(400),
            color = Color(0xff000000), // Black
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = views,
            fontSize = 16.sp,
            fontWeight = FontWeight(500), // Medium
            color = Color(0xff000000), // Black
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// --- 미리보기 ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KpopDancePracticeAITheme {
        // ⭐️ [수정] 프리뷰가 정상 작동하도록 빈 PaddingValues 전달
        HomeScreen(onSearchClick = {}, paddingValues = PaddingValues())
    }
}