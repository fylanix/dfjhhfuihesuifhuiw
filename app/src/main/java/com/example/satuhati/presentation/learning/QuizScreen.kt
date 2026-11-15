package com.example.satuhati.presentation.learning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.QuizQuestion
import com.example.satuhati.ui.theme.BorderNeutral
import com.example.satuhati.ui.theme.InputBackground
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.StatusSuccessSoft
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

@Composable
fun QuizScreen(
    uiState: LearningUiState,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onQuizCompleted: () -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(uiState.quizResult) {
        if (uiState.quizResult != null) {
            onQuizCompleted()
        }
    }

    val currentMod = uiState.selectedModule ?: return
    val totalQuestions = currentMod.questions.size
    val currentQ = currentMod.questions.getOrNull(uiState.currentQuestionIndex) ?: return
    val progress = (uiState.currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat()

    val optionLetters = listOf("A", "B", "C", "D")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = TextDark
                )
            }
            Text(
                text = "Kuis Pemahaman",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // Progress row (Segmented completion)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pertanyaan ${uiState.currentQuestionIndex + 1} dari $totalQuestions",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "${(progress * 100).toInt()}% Selesai",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = PrimaryBlue,
                trackColor = Color(0xFFE2E8F0)
            )
        }

        // Role Partnership Badge
        Row(
            modifier = Modifier
                .background(Color(0xFFE7EBFE), CircleShape)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Kemitraan",
                tint = PrimaryBlue,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Kemitraan Suami & Istri Siaga",
                color = PrimaryBlue,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = currentQ.question,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 24.sp
                )

                // Options List
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    currentQ.options.forEachIndexed { index, optionText ->
                        val letter = optionLetters.getOrElse(index) { "$index" }
                        val isSelected = uiState.selectedOptionIndex == index
                        val isCorrect = index == currentQ.correctOptionIndex
                        val hasAnswered = uiState.selectedOptionIndex != null

                        val cardBg = when {
                            hasAnswered && isCorrect -> Color(0xFFEDF7F1)
                            isSelected && !isCorrect -> Color(0xFFFEE2E2)
                            isSelected -> Color(0xFFE7EBFE)
                            else -> InputBackground
                        }

                        val border = when {
                            hasAnswered && isCorrect -> BorderStroke(1.5.dp, StatusSuccess)
                            isSelected && !isCorrect -> BorderStroke(1.5.dp, Color.Red)
                            isSelected -> BorderStroke(1.5.dp, PrimaryBlue)
                            else -> BorderStroke(1.dp, BorderNeutral)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(cardBg)
                                .clickable(enabled = !hasAnswered) {
                                    onSelectOption(index)
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Letter Avatar
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (isSelected || (hasAnswered && isCorrect)) PrimaryBlue else Color(0xFFCBD5E1),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Text(
                                text = optionText,
                                color = TextDark,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )

                            if (hasAnswered && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Benar",
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Instant Clinical Feedback Accordion (Figma Frame 8:643)
        AnimatedVisibility(
            visible = uiState.isFeedbackExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFF), RoundedCornerShape(20.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Klinis",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Penjelasan Klinis Langsung",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = currentQ.clinicalWarningTitle,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    text = currentQ.clinicalExplanation,
                    color = TextMuted,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

                // Couple Interactive Sharing Prompt Card
                if (currentQ.coupleSharingPrompt.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "💡 Diskusikan Berdua: ${currentQ.coupleSharingPrompt}",
                            color = PrimaryBlue,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = if (uiState.currentQuestionIndex + 1 < totalQuestions) "Lanjut ke Pertanyaan Berikutnya" else "Lihat Evaluasi Kuis",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
