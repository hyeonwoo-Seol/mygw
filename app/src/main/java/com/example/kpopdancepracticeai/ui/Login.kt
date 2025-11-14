package com.example.kpopdancepracticeai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    // 1. 상태 관리: 사용자의 입력을 기억하기 위한 변수
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // 전체 화면 사용 및 여백
        contentAlignment = Alignment.Center // 중앙 정렬
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // 컴포넌트 간 간격
        ) {
            // 앱 제목
            Text(
                text = "KPOP 댄스 연습 앱",
                style = MaterialTheme.typography.headlineMedium, // 미리 정의된 스타일 사용
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 2. 입력 필드: 'Text' 대신 'OutlinedTextField' 사용
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일 (아이디)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 3. 비밀번호 입력 필드
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "비밀번호 보이기/숨기기")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. 버튼: 'Box' 대신 실제 'Button' 사용
            Button(
                onClick = {
                    // TODO: ViewModel을 통해 로그인 로직 호출
                    // 예: viewModel.loginWithEmail(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "로그인", fontSize = 16.sp)
            }

            // 회원가입 / 아이디/비밀번호 찾기
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = { /* TODO: 회원가입 화면 이동 */ }) {
                    Text("회원가입")
                }
                TextButton(onClick = { /* TODO: 아이디/비밀번호 찾기 이동 */ }) {
                    Text("아이디/비밀번호 찾기")
                }
            }

            // "또는" 구분선
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("또는", color = Color.Gray)
                Divider(modifier = Modifier.weight(1f))
            }

            // 소셜 로그인 (보고서 내용 기반)
            Text("소셜 로그인", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                // TODO: 실제 구글/카카오 로그인 버튼으로 교체
                Button(onClick = { /* TODO: 구글 로그인 로직 */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB4437))) {
                    Text("Google", color = Color.White)
                }
                Button(onClick = { /* TODO: 카카오 로그인 로직 */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500))) {
                    Text("Kakao", color = Color.Black)
                }
            }
        }
    }
}

// Android Studio에서 미리보기를 위한 코드
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // 앱의 테마를 여기에 적용하면 좋습니다.
    // 예: MyProjectTheme { ... }
    Surface {
        LoginScreen()
    }
}