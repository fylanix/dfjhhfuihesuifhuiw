package com.example.satuhati.presentation.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.Couple
import com.example.satuhati.domain.model.User
import com.example.satuhati.domain.model.UserRole
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.SoftBlueTint
import com.example.satuhati.ui.theme.StatusSuccess
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

@Composable
fun AccountProfileScreen(
    user: User?,
    couple: Couple?,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isWife = user?.role == UserRole.ISTRI

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(PrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (user?.name?.take(1) ?: "U").uppercase(),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user?.name ?: "Pengguna SatuHati",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user?.email ?: "-",
            fontSize = 13.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SoftBlueTint)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isWife) "Ibu Hamil (Istri)" else "Suami Siaga",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pasangan Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status Pasangan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEDF7F1))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Terhubung ✓",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )
                    }
                }

                Text(
                    text = "Istri: ${couple?.wifeName ?: "-"}",
                    fontSize = 13.sp,
                    color = TextDark
                )

                Text(
                    text = "Suami: ${couple?.husbandName ?: "-"}",
                    fontSize = 13.sp,
                    color = TextDark
                )

                // Pairing Code
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                        .clickable {
                            val code = couple?.pairingCode ?: ""
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Kode Pairing", code))
                            Toast.makeText(context, "Kode pairing disalin!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Kode Pairing Pasangan",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = couple?.pairingCode ?: "-",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Salin Kode",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About SatuHati Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Tentang Aplikasi SatuHati",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "SatuHati adalah aplikasi pendampingan kehamilan interaktif pasangan suami-istri berbasis MVVM Clean Architecture, Room Database (SQLite offline-first), dan Firebase Firestore cloud synchronization.",
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Keluar dari Akun",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
