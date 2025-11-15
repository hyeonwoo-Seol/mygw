package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kpopdancepracticeai.ui.theme.KpopDancePracticeAITheme

// UI 상태를 관리하기 위한 Sealed Class
sealed class SongDetailUiState {
    object Loading : SongDetailUiState()
    data class Success(
        val songInfo: SongInfo, // 캐시/AWS에서 가져온 곡 정보
        val userAnalysis: UserAnalysis? // AWS에서 가져온 사용자 분석 (없을 수도 있음)
    ) : SongDetailUiState()
    data class Error(val message: String) : SongDetailUiState()
}

// 임시 데이터 클래스 (실제로는 ViewModel에서 관리)
data class SongInfo(val id: String, val title: String, val artist: String, val albumArtUrl: String)
data class UserAnalysis(val averageAccuracy: Float, val practiceCount: Int, val lastPracticeDate: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailScreen(
    songId: String,
    onBackClick: () -> Unit
) {
    // --- ViewModel 로직 (임시) ---
    // 실제로는 ViewModel을 주입받아 사용합니다.
    // 여기서는 Composable 내에서 임시로 상태를 시뮬레이션합니다.
    var uiState by remember { mutableStateOf<SongDetailUiState>(SongDetailUiState.Loading) }

    // songId가 변경될 때 데이터를 로드하는 효과 (시뮬레이션)
    LaunchedEffect(songId) {
        kotlinx.coroutines.delay(1000) // 1. 로딩 시뮬레이션

        // 2. 캐시/AWS에서 곡 정보 가져오기 (시뮬레이션)
        val songInfo = SongInfo(
            id = songId,
            title = "Whiplash",
            artist = "aespa",
            albumArtUrl = "https://placehold.co/600x600/000000/FFFFFF?text=aespa"
        )

        // 3. AWS에서 사용자 분석 결과 가져오기 (시뮬레이션)
        val userAnalysis = UserAnalysis(
            averageAccuracy = 88.5f,
            practiceCount = 12,
            lastPracticeDate = "2025. 11. 10."
        )

        // 4. UI 상태 업데이트
        uiState = SongDetailUiState.Success(songInfo, userAnalysis)
    }
    // --- ViewModel 로직 끝 ---


    val appGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDE3FF), // 상단 연한 파랑
            Color(0xFFF0E8FF)  // 하단 연한 보라
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.background(appGradient),
        topBar = {
            TopAppBar(
                title = {
                    // 상태가 Success일 때만 제목 표시
                    if (uiState is SongDetailUiState.Success) {
                        val data = (uiState as SongDetailUiState.Success).songInfo
                        Text("${data.artist} - ${data.title}", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is SongDetailUiState.Loading -> {
                    // 로딩 중
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SongDetailUiState.Success -> {
                    // 로드 성공
                    SongDetailContent(
                        songInfo = state.songInfo,
                        userAnalysis = state.userAnalysis
                    )
                }
                is SongDetailUiState.Error -> {
                    // 오류 발생
                    Text(
                        text = "오류 발생: ${state.message}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SongDetailContent(
    songInfo: SongInfo,
    userAnalysis: UserAnalysis?
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. 앨범 아트 (임시) ---
        item {
            // Coil 등의 라이브러리로 이미지 로드
            // Image(painter = ..., ... )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "앨범 아트", style = MaterialTheme.typography.titleLarge)
            }
        }

        // --- 2. 곡 정보 (캐시된 데이터) ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.7f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("곡 정보 (From Cache/AWS)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Song ID: ${songInfo.id}")
                    Text("제목: ${songInfo.title}")
                    Text("아티스트: ${songInfo.artist}")
                }
            }
        }

        // --- 3. 사용자 분석 (매번 새로고침) ---
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.7f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("내 분석 결과 (From AWS)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (userAnalysis != null) {
                        Text("평균 정확도: ${userAnalysis.averageAccuracy}%")
                        Text("총 연습 횟수: ${userAnalysis.practiceCount}회")
                        Text("마지막 연습: ${userAnalysis.lastPracticeDate}")
                    } else {
                        Text("아직 이 곡에 대한 연습 기록이 없습니다.")
                    }
                }
            }
        }

        // --- 4. 연습 시작 버튼 ---
        item {
            Button(
                onClick = { /* TODO: 연습 화면으로 이동 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("연습 시작하기", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongDetailScreenPreview() {
    KpopDancePracticeAITheme {
        SongDetailScreen(
            songId = "preview_song_id",
            onBackClick = {}
        )
    }
}