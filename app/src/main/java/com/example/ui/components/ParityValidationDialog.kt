package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ReconCardBorder
import com.example.ui.theme.ReconCardDark
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconMatrixGreen
import com.example.ui.theme.ReconNeonTeal
import com.example.ui.theme.ReconOledBlack
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextMuted
import com.example.ui.theme.ReconTextPrimary
import com.example.ui.theme.ReconTextSecondary

data class ParityVerificationItem(
    val sceneNumber: Int,
    val timestamp: String,
    val characterName: String,
    val sourceVideoDialogue: String,
    val promptDialogue: String,
    val isMatched: Boolean
)

@Composable
fun ParityValidationDialog(
    projectTitle: String,
    verificationItems: List<ParityVerificationItem>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ReconSurfaceDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ReconCardBorder, RoundedCornerShape(20.dp))
                .testTag("parity_validation_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(ReconMatrixGreen.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, ReconMatrixGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ReconMatrixGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "DIALOGUE & PARITY AUDIT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconMatrixGreen,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "100% Reverse-Eng Match",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("parity_dialog_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = ReconTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ReconCardDark, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ReconMatrixGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Strict Dialogue Parity Verified",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Every spoken word in the video is mapped to the exact same character in the synthesized AI video prompt.",
                            fontSize = 11.sp,
                            color = ReconTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Verification Items List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (item in verificationItems) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ReconOledBlack, RoundedCornerShape(12.dp))
                                .border(1.dp, if (item.isMatched) ReconMatrixGreen.copy(alpha = 0.6f) else Color.Red, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Scene ${item.sceneNumber} (${item.timestamp})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ReconNeonTeal,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(ReconMatrixGreen.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("PARITY 100%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ReconMatrixGreen)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Speaker: ${item.characterName}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ReconCyan
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Video Dialogue: \"${item.sourceVideoDialogue}\"",
                                    fontSize = 11.sp,
                                    color = ReconTextPrimary,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = "Prompt Audio: \"${item.promptDialogue}\"",
                                    fontSize = 11.sp,
                                    color = ReconMatrixGreen,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReconCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("parity_dialog_done")
                ) {
                    Text("Close Verification Report", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
