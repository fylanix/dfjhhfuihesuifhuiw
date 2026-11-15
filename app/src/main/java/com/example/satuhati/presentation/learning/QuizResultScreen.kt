package com.example.satuhati.presentation.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.QuizResult
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.StatusSuccessSoft
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

@Composable
fun QuizResultScreen(
    quizResult: QuizResult?,
    onFinishClick: () -> Unit
) {
    if (quizResult == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 32.dp, vertical = 40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Trophy / Badge Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(if (quizResult.isPassed) StatusSuccessSoft else Color(0xFFFEF3C7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Badge",
                tint = if (quizResult.isPassed) StatusSuccess else Color(0xFFD97706),
                modifier = Modifier.size(56.dp)
            )
        }

        Text(
            text = "Hasil Evaluasi Kuis",
            color = TextDark,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )

        // Score Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFF), RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Skor Anda",
                color = TextMuted,
                fontSize = 14.sp
            )
            Text(
                text = "${quizResult.score}%",
                color = if (quizResult.isPassed) StatusSuccess else PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 44.sp
            )
            Text(
                text = "${quizResult.correctCount} dari ${quizResult.totalQuestions} pertanyaan dijawab dengan benar",
                color = TextDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .background(if (quizResult.isPassed) StatusSuccessSoft else Color(0xFFE2E8F0), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "🏆 ${quizResult.achievementBadge}",
                    color = if (quizResult.isPassed) StatusSuccess else TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Recommendation Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Rekomendasi Tindakan Klinis:",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = quizResult.recommendation,
                color = TextMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Return button
        Button(
            onClick = onFinishClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(
                text = "Kembali ke Kurikulum Belajar",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
