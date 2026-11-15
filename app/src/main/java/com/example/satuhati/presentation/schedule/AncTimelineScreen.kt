package com.example.satuhati.presentation.schedule

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.AncSchedule
import com.example.satuhati.domain.model.UserRole
import com.example.satuhati.ui.theme.DarkNavy
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.SoftBlueTint
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun AncTimelineScreen(
    schedules: List<AncSchedule>,
    userRole: UserRole,
    onNavigateToAddSchedule: () -> Unit,
    onNavigateToHusbandSchedule: (AncSchedule) -> Unit,
    onToggleCompletion: (AncSchedule) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Semua") }

    val filteredSchedules = remember(schedules, selectedFilter) {
        when (selectedFilter) {
            "Mendatang" -> schedules.filter { !it.isCompleted }
            "Selesai" -> schedules.filter { it.isCompleted }
            else -> schedules
        }
    }

    val nextUpcoming = remember(schedules) {
        schedules.firstOrNull { !it.isCompleted && it.dateEpochMillis >= System.currentTimeMillis() - 86400000L }
            ?: schedules.firstOrNull { !it.isCompleted }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddSchedule,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Jadwal Kontrol"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .padding(innerPadding)
        ) {
            // Header Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Jadwal Pemeriksaan ANC",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Standar 6 Kali Kunjungan Kemenkes RI",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Hero Card: Next Upcoming Visit
                if (nextUpcoming != null) {
                    item {
                        UpcomingVisitHeroCard(
                            schedule = nextUpcoming,
                            userRole = userRole,
                            onChecklistClick = { onNavigateToHusbandSchedule(nextUpcoming) },
                            onToggleCompletion = { onToggleCompletion(nextUpcoming) }
                        )
                    }
                }

                // Filter Tabs
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Semua", "Mendatang", "Selesai").forEach { tab ->
                            val isSelected = selectedFilter == tab
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) PrimaryBlue else Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) PrimaryBlue else Color(0xFFE2E8F0),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedFilter = tab }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = tab,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextDark
                                )
                            }
                        }
                    }
                }

                // Vertical Timeline
                itemsIndexed(filteredSchedules) { index, schedule ->
                    TimelineItemRow(
                        schedule = schedule,
                        isLast = index == filteredSchedules.size - 1,
                        onToggleCompletion = { onToggleCompletion(schedule) },
                        onOpenHusbandChecklist = { onNavigateToHusbandSchedule(schedule) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun UpcomingVisitHeroCard(
    schedule: AncSchedule,
    userRole: UserRole,
    onChecklistClick: () -> Unit,
    onToggleCompletion: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
    val formattedDate = dateFormat.format(Date(schedule.dateEpochMillis))

    val diffMillis = schedule.dateEpochMillis - System.currentTimeMillis()
    val daysLeft = TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()

    val countdownText = when {
        daysLeft < 0 -> "Lewat ${-daysLeft} Hari"
        daysLeft == 0 -> "Hari Ini"
        else -> "$daysLeft Hari Lagi"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
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
                        text = "JADWAL TERDEKAT",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF2A93C))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = countdownText,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = schedule.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = SoftBlueTint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = formattedDate,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SoftBlueTint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = schedule.location.ifBlank { "Fasilitas Kesehatan Terdekat" },
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = SoftBlueTint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = schedule.doctorOrMidwife.ifBlank { "Dokter Sp.OG / Bidan" },
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onChecklistClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "Checklist Suami",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onToggleCompletion,
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.6f))
                    )
                ) {
                    Text(
                        text = if (schedule.isCompleted) "Selesai ✓" else "Tandai Selesai",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineItemRow(
    schedule: AncSchedule,
    isLast: Boolean,
    onToggleCompletion: () -> Unit,
    onOpenHusbandChecklist: () -> Unit
) {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
    val formattedDate = dateFormat.format(Date(schedule.dateEpochMillis))

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Vertical indicator & line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (schedule.isCompleted) StatusSuccess else PrimaryBlue
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (schedule.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selesai",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(130.dp)
                        .background(Color(0xFFCBD5E1))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Schedule Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SoftBlueTint)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = schedule.visitType.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (schedule.isCompleted) Color(0xFFEDF7F1) else Color(0xFFF1F5F9))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (schedule.isCompleted) "Selesai" else "Mendatang",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (schedule.isCompleted) StatusSuccess else TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = schedule.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "📅 $formattedDate  •  📍 ${schedule.location}",
                    fontSize = 12.sp,
                    color = TextMuted
                )

                if (schedule.doctorOrMidwife.isNotBlank()) {
                    Text(
                        text = "👨‍⚕️ ${schedule.doctorOrMidwife}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                if (schedule.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = schedule.notes,
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Perlengkapan Suami",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlue,
                        modifier = Modifier
                            .clickable { onOpenHusbandChecklist() }
                            .padding(end = 16.dp)
                    )

                    OutlinedButton(
                        onClick = onToggleCompletion,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = if (schedule.isCompleted) "Batal Selesai" else "Tandai Selesai",
                            fontSize = 11.sp,
                            color = if (schedule.isCompleted) Color(0xFFDC2626) else PrimaryBlue
                        )
                    }
                }
            }
        }
    }
}
