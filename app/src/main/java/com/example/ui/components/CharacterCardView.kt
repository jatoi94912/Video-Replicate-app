package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CharacterCard
import com.example.ui.theme.ReconAmber
import com.example.ui.theme.ReconCardBorder
import com.example.ui.theme.ReconCardDark
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconMatrixGreen
import com.example.ui.theme.ReconMatrixGreenDark
import com.example.ui.theme.ReconNeonTeal
import com.example.ui.theme.ReconOledBlack
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextMuted
import com.example.ui.theme.ReconTextPrimary
import com.example.ui.theme.ReconTextSecondary

@Composable
fun CharacterCardView(
    character: CharacterCard,
    isCopied: Boolean,
    onApprove: () -> Unit,
    onUnapprove: () -> Unit,
    onRegen: () -> Unit,
    onCopyPrompt: () -> Unit,
    extractedFrame: android.graphics.Bitmap? = null,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (character.isApproved) ReconMatrixGreen else ReconCardBorder,
        label = "border_color"
    )

    Box(
        modifier = modifier
            .width(320.dp)
            .background(ReconCardDark, RoundedCornerShape(20.dp))
            .border(
                width = if (character.isApproved) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
            .testTag("character_card_${character.id}")
    ) {
        Column {
            // Top Image / Visual Frame representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(character.avatarColorHex).copy(alpha = 0.35f),
                                ReconOledBlack
                            )
                        )
                    )
                    .border(1.dp, Color(character.avatarColorHex).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            ) {
                if (extractedFrame != null) {
                    androidx.compose.foundation.Image(
                        bitmap = extractedFrame.asImageBitmap(),
                        contentDescription = character.name,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                } else {
                    // Background icon / graphic
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(character.avatarColorHex).copy(alpha = 0.25f),
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                    )
                }

                // Top left role pill
                Box(
                    modifier = Modifier
                        .background(ReconOledBlack.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .border(0.8.dp, ReconCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = character.role,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReconCyan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Top right timestamp overlay badge
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "⏱️ ${character.refTimestamp}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReconTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Bottom identity status badge
                if (character.isApproved) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(ReconMatrixGreenDark.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .border(1.dp, ReconMatrixGreen, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ReconMatrixGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LOCKED IDENTITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ReconMatrixGreen,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Character Name & Summary
            Text(
                text = character.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ReconTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = character.appearanceSummary,
                fontSize = 12.sp,
                color = ReconTextSecondary,
                lineHeight = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Design Sheet Prompt in Code Block
            CodeBlockView(
                codeText = character.designSheetPrompt,
                onCopy = onCopyPrompt,
                isCopied = isCopied,
                title = "🎭 DESIGN SHEET PROMPT",
                testTagPrefix = "char_${character.id}"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons (Approve / Regen)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (character.isRegenerating) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(ReconSurfaceDark, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ReconAmber, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Re-engineering...", fontSize = 12.sp, color = ReconAmber)
                        }
                    }
                } else {
                    // Approve Button
                    if (character.isApproved) {
                        OutlinedButton(
                            onClick = onUnapprove,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("unapprove_button_${character.id}"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ReconMatrixGreen
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ReconMatrixGreen)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Approved", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onApprove,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("approve_button_${character.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ReconMatrixGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Regen Button
                    OutlinedButton(
                        onClick = onRegen,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("regen_button_${character.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ReconAmber
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ReconAmber)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Regen", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Regen", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
