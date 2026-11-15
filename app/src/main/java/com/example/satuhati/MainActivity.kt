package com.example.satuhati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.satuhati.domain.model.UserRole
import com.example.satuhati.presentation.auth.AuthViewModel
import com.example.satuhati.presentation.learning.LearningViewModel
import com.example.satuhati.presentation.navigation.SatuHatiNavGraph
import com.example.satuhati.presentation.navigation.Screen
import com.example.satuhati.presentation.notes.NotesViewModel
import com.example.satuhati.presentation.schedule.ScheduleViewModel
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.SatuHatiTheme
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as SatuHatiApp
        val authRepository = app.container.authRepository
        val noteRepository = app.container.noteRepository
        val learningRepository = app.container.learningRepository
        val scheduleRepository = app.container.scheduleRepository

        setContent {
            SatuHatiTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel {
                    AuthViewModel(authRepository)
                }
                val notesViewModel: NotesViewModel = viewModel {
                    NotesViewModel(noteRepository)
                }
                val learningViewModel: LearningViewModel = viewModel {
                    LearningViewModel(learningRepository)
                }
                val scheduleViewModel: ScheduleViewModel = viewModel {
                    ScheduleViewModel(scheduleRepository)
                }

                val uiState by authViewModel.uiState.collectAsState()

                LaunchedEffect(uiState.currentUser?.coupleId) {
                    uiState.currentUser?.coupleId?.let { cId ->
                        notesViewModel.loadNotes(cId)
                        scheduleViewModel.loadSchedules(cId)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SatuHatiNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        notesViewModel = notesViewModel,
                        learningViewModel = learningViewModel,
                        scheduleViewModel = scheduleViewModel,
                        modifier = Modifier.padding(innerPadding),
                        onDashboardScreen = {
                            DashboardPlaceholder(
                                authViewModel = authViewModel,
                                onOpenNotes = {
                                    val isWife = uiState.currentUser?.role == UserRole.ISTRI
                                    if (isWife) {
                                        navController.navigate(Screen.WifeNotes.route)
                                    } else {
                                        navController.navigate(Screen.HusbandNotes.route)
                                    }
                                },
                                onOpenLearning = {
                                    navController.navigate(Screen.LearningCatalog.route)
                                },
                                onOpenSchedule = {
                                    navController.navigate(Screen.AncTimeline.route)
                                },
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate(Screen.Landing.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardPlaceholder(
    authViewModel: AuthViewModel,
    onOpenNotes: () -> Unit,
    onOpenLearning: () -> Unit,
    onOpenSchedule: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val user = uiState.currentUser
    val couple = uiState.currentCouple

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SATUHATI",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEDF7F1), RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Akun Pasangan Terhubung!",
                color = StatusSuccess,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Pengguna: ${user?.name ?: "-"} (${user?.role?.name ?: "-"})",
                color = TextDark,
                fontSize = 14.sp
            )
            Text(
                text = "Istri: ${couple?.wifeName ?: "-"}",
                color = TextMuted,
                fontSize = 14.sp
            )
            Text(
                text = "Suami: ${couple?.husbandName ?: "-"}",
                color = TextMuted,
                fontSize = 14.sp
            )
            Text(
                text = "Kode Pairing: ${couple?.pairingCode ?: "-"}",
                color = TextMuted,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onOpenNotes,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(
                text = if (user?.role == UserRole.ISTRI) "Buka Catatan Kehamilan (Diari)" else "Lihat Catatan Dari Istri",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onOpenLearning,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2E86))
        ) {
            Text(
                text = "Buka Modul Belajar & Kuis",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onOpenSchedule,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E))
        ) {
            Text(
                text = "Jadwal Pemeriksaan ANC & Suami Siaga",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B))
        ) {
            Text("Keluar (Logout)", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
