package com.example.satuhati.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.Couple
import com.example.satuhati.domain.model.User
import com.example.satuhati.domain.model.UserRole
import com.example.satuhati.ui.theme.DarkNavy
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.SoftBlueTint
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

@Composable
fun HomeScreen(
    currentUser: User?,
    currentCouple: Couple?,
    onNavigateToNotes: () -> Unit,
    onNavigateToLearning: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToHusbandPrep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWife = currentUser?.role == UserRole.ISTRI
    val partnerName = if (isWife) currentCouple?.husbandName ?: "Suami" else currentCouple?.wifeName ?: "Istri"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Top Greeting Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Halo, ${currentUser?.name ?: "Bunda & Ayah"}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Terhubung dengan $partnerName 💕",
                        fontSize = 13.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (currentUser?.name?.take(1) ?: "S").uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // Pregnancy Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TRIMESTER 2",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Minggu ke-18",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF2A93C)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Si Kecil Seukuran Buah Mangga 🥭",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Pendengaran janin semakin berkembang! Ayah dan Ibu dianjurkan mulai rutin mengajak si kecil berbicara.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { 0.45f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFFF2A93C),
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Perkiraan Hari Lahir: 154 Hari Lagi",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Quick Action Grid
        item {
            Text(
                text = "Fitur Pendampingan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickFeatureCard(
                    title = if (isWife) "Diari Istri" else "Diari Istri",
                    subtitle = if (isWife) "Tulis Catatan Mood" else "Baca & Tanggapi",
                    icon = Icons.Default.Description,
                    badgeColor = Color(0xFFE0E7FF),
                    iconTint = PrimaryBlue,
                    onClick = onNavigateToNotes,
                    modifier = Modifier.weight(1f)
                )

                QuickFeatureCard(
                    title = "Modul Belajar",
                    subtitle = "Edukasi & Kuis",
                    icon = Icons.Default.MenuBook,
                    badgeColor = Color(0xFFFEF3C7),
                    iconTint = Color(0xFFD97706),
                    onClick = onNavigateToLearning,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickFeatureCard(
                    title = "Jadwal ANC",
                    subtitle = "Timeline K1 - K6",
                    icon = Icons.Default.CalendarMonth,
                    badgeColor = Color(0xFFCCFBF1),
                    iconTint = Color(0xFF0F766E),
                    onClick = onNavigateToSchedule,
                    modifier = Modifier.weight(1f)
                )

                QuickFeatureCard(
                    title = "Suami Siaga",
                    subtitle = "Checklist Kontrol",
                    icon = Icons.Default.Shield,
                    badgeColor = Color(0xFFDCFCE7),
                    iconTint = StatusSuccess,
                    onClick = onNavigateToHusbandPrep,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Daily Tip Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF8FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3E8FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFF9333EA),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tips SatuHati Hari Ini",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pastikan Ibu minum air putih minimal 2,5 liter sehari dan konsumsi tablet tambah darah sebelum tidur.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}
