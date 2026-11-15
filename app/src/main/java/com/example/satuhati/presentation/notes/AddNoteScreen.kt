package com.example.satuhati.presentation.notes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satuhati.domain.model.User
import com.example.satuhati.ui.theme.BorderNeutral
import com.example.satuhati.ui.theme.InputBackground
import com.example.satuhati.ui.theme.PrimaryBlue
import com.example.satuhati.ui.theme.ScreenBackground
import com.example.satuhati.ui.theme.TextDark
import com.example.satuhati.ui.theme.TextMuted

@Composable
fun AddNoteScreen(
    uiState: NotesUiState,
    currentUser: User?,
    coupleId: String?,
    onSaveClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedMood by remember { mutableStateOf("Senang") }
    var contentText by remember { mutableStateOf("") }

    val moodOptions = listOf("Senang", "Netral", "Lelah", "Cemas", "Sakit")

    LaunchedEffect(uiState.isSavingSuccess) {
        if (uiState.isSavingSuccess) {
            onBackClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Top Header
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
                text = "Tambah Catatan",
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        uiState.errorMessage?.let { msg ->
            Text(
                text = msg,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        // Mood Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Mood anda hari ini",
                color = TextDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moodOptions.take(3).forEach { mood ->
                    val isSelected = selectedMood == mood
                    OutlinedButton(
                        onClick = { selectedMood = mood },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isSelected) PrimaryBlue else BorderNeutral),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) PrimaryBlue else InputBackground
                        )
                    ) {
                        Text(
                            text = mood,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moodOptions.drop(3).forEach { mood ->
                    val isSelected = selectedMood == mood
                    OutlinedButton(
                        onClick = { selectedMood = mood },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isSelected) PrimaryBlue else BorderNeutral),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) PrimaryBlue else InputBackground
                        )
                    ) {
                        Text(
                            text = mood,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Note Textarea
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Catatan untuk suami",
                color = TextDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            OutlinedTextField(
                value = contentText,
                onValueChange = { contentText = it },
                placeholder = { Text("Berikan catatan untuk suami...", color = Color(0xFF94A3B8)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBackground,
                    unfocusedContainerColor = InputBackground,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderNeutral
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button (Figma Node 72:1403)
        Button(
            onClick = {
                onSaveClick(selectedMood, contentText)
            },
            enabled = !uiState.isLoading && contentText.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Simpan",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
