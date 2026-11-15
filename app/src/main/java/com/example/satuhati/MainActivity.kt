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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.satuhati.domain.model.UserRole
import com.example.satuhati.presentation.auth.AuthViewModel
import com.example.satuhati.presentation.common.BottomNavItem
import com.example.satuhati.presentation.common.SatuHatiBottomNavBar
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                LaunchedEffect(uiState.currentUser?.coupleId) {
                    uiState.currentUser?.coupleId?.let { cId ->
                        notesViewModel.loadNotes(cId)
                        scheduleViewModel.loadSchedules(cId)
                    }
                }

                val showBottomNav = currentRoute in listOf(
                    Screen.MainDashboard.route,
                    Screen.WifeNotes.route,
                    Screen.HusbandNotes.route,
                    Screen.LearningCatalog.route,
                    Screen.AncTimeline.route,
                    Screen.AccountProfile.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            SatuHatiBottomNavBar(
                                currentRoute = currentRoute,
                                onNavigate = { navItem ->
                                    val isWife = uiState.currentUser?.role == UserRole.ISTRI
                                    when (navItem) {
                                        BottomNavItem.HOME -> {
                                            navController.navigate(Screen.MainDashboard.route) {
                                                popUpTo(Screen.MainDashboard.route) { inclusive = true }
                                            }
                                        }
                                        BottomNavItem.DIARY -> {
                                            val target = if (isWife) Screen.WifeNotes.route else Screen.HusbandNotes.route
                                            navController.navigate(target) {
                                                launchSingleTop = true
                                            }
                                        }
                                        BottomNavItem.LEARNING -> {
                                            navController.navigate(Screen.LearningCatalog.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                        BottomNavItem.SCHEDULE -> {
                                            navController.navigate(Screen.AncTimeline.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                        BottomNavItem.PROFILE -> {
                                            navController.navigate(Screen.AccountProfile.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    SatuHatiNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        notesViewModel = notesViewModel,
                        learningViewModel = learningViewModel,
                        scheduleViewModel = scheduleViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
