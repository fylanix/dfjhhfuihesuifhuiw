package com.example.satuhati.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.presentation.navigation.Screen
import com.example.satuhati.ui.theme.PrimaryBlue

enum class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    HOME("Beranda", Icons.Default.Home, Screen.MainDashboard.route),
    DIARY("Diari", Icons.Default.Description, "notes_flow"),
    LEARNING("Belajar", Icons.Default.MenuBook, Screen.LearningCatalog.route),
    SCHEDULE("Jadwal", Icons.Default.CalendarMonth, Screen.AncTimeline.route),
    PROFILE("Akun", Icons.Default.Person, "account_profile")
}

@Composable
fun SatuHatiBottomNavBar(
    currentRoute: String?,
    onNavigate: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.values().forEach { item ->
                val isSelected = when (item) {
                    BottomNavItem.HOME -> currentRoute == Screen.MainDashboard.route
                    BottomNavItem.DIARY -> currentRoute == Screen.WifeNotes.route || currentRoute == Screen.HusbandNotes.route || currentRoute == Screen.AddNote.route
                    BottomNavItem.LEARNING -> currentRoute == Screen.LearningCatalog.route || currentRoute == Screen.ModuleDetail.route || currentRoute == Screen.Quiz.route
                    BottomNavItem.SCHEDULE -> currentRoute == Screen.AncTimeline.route || currentRoute == Screen.AddSchedule.route || currentRoute == Screen.HusbandSchedule.route
                    BottomNavItem.PROFILE -> currentRoute == "account_profile"
                }

                val contentColor = if (isSelected) PrimaryBlue else Color(0xFF94A3B8)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onNavigate(item)
                        }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor
                    )
                }
            }
        }
    }
}
