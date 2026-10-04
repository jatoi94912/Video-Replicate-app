package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CharacterCard
import com.example.model.VideoProject
import com.example.ui.components.CharacterCardView
import com.example.ui.theme.ReconAmber
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

@Composable
fun CharacterLockScreen(
    project: VideoProject,
    copiedPromptId: String?,
    onApprove: (String) -> Unit,
    onUnapprove: (String) -> Unit,
    onRegen: (CharacterCard) -> Unit,
    onCopyPrompt: (String, String) -> Unit,
    onProceedToPhase2: () -> Unit,
    onBackToUpload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = project.characters.size
    val approvedCount = project.characters.count { it.isApproved }
    val isAllApproved = totalCount > 0 && approvedCount == totalCount
    val progress = if (totalCount > 0) approvedCount.toFloat() / totalCount else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ReconOledBlack)
    ) {
        // Sticky Top Header & Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReconSurfaceDark)
                .border(1.dp, ReconCardBorder)
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBackToUpload,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("char_lock_back_button")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ReconTextSecondary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "🎭 PHASE 1: CHARACTER LOCK",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = project.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }
                    }

                    // Approved count pill
                    Box(
                        modifier = Modifier
                            .background(
                                if (isAllApproved) ReconMatrixGreen else ReconCardDark,
                                RoundedCornerShape(20.dp)
                            )
                            .border(
                                1.dp,
                                if (isAllApproved) ReconMatrixGreen else ReconCardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("approval_progress_badge")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isAllApproved) Icons.Default.Lock else Icons.Default.Check,
                                contentDescription = null,
                                tint = if (isAllApproved) Color.Black else ReconCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$approvedCount / $totalCount Approved",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAllApproved) Color.Black else ReconTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = ReconMatrixGreen,
                    trackColor = Color(0xFF1E293B),
                )
            }
        }

        // Subheader instructions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Swipe horizontally to inspect each character. Click ✅ Approve to lock identity vectors.",
                fontSize = 12.sp,
                color = ReconTextSecondary,
                lineHeight = 16.sp
            )
        }

        // Horizontal Scrollable Cards
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                for (character in project.characters) {
                    CharacterCardView(
                        character = character,
                        isCopied = copiedPromptId == "char_${character.id}",
                        onApprove = { onApprove(character.id) },
                        onUnapprove = { onUnapprove(character.id) },
                        onRegen = { onRegen(character) },
                        onCopyPrompt = { onCopyPrompt(character.designSheetPrompt, "char_${character.id}") }
                    )
                }
            }
        }

        // Sticky Bottom CTA Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReconSurfaceDark)
                .border(1.dp, ReconCardBorder)
                .padding(14.dp)
        ) {
            Column {
                if (!isAllApproved) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ReconAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Identity Lock required: Approve all characters before proceeding.",
                            fontSize = 11.sp,
                            color = ReconAmber
                        )
                    }
                }

                Button(
                    onClick = onProceedToPhase2,
                    enabled = isAllApproved,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ReconCyan,
                        disabledContainerColor = Color(0xFF1E293B),
                        contentColor = Color.Black,
                        disabledContentColor = ReconTextMuted
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("proceed_to_duration_button")
                ) {
                    Text(
                        text = if (isAllApproved) "Proceed to Duration Selection →" else "Approve All Characters to Continue",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (isAllApproved) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
