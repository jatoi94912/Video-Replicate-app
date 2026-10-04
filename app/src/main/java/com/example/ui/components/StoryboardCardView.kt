package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryboardCard
import com.example.ui.theme.ReconCardBorder
import com.example.ui.theme.ReconCardDark
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconNeonTeal
import com.example.ui.theme.ReconOledBlack
import com.example.ui.theme.ReconPurple
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextMuted
import com.example.ui.theme.ReconTextPrimary
import com.example.ui.theme.ReconTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoryboardCardView(
    card: StoryboardCard,
    isCopied: Boolean,
    onCopyPrompt: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ReconCardDark, RoundedCornerShape(20.dp))
            .border(1.dp, ReconCardBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
            .testTag("storyboard_card_${card.segmentIndex}")
    ) {
        Column {
            // Header Bar: Scene Index & Coverage Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(ReconCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, ReconCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "SCENE ${card.segmentIndex} OF ${card.totalSegments}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ReconCyan,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Coverage Tag
                Box(
                    modifier = Modifier
                        .background(ReconSurfaceDark, RoundedCornerShape(8.dp))
                        .border(1.dp, ReconCardBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏱️ ${card.timeRange}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReconNeonTeal,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 16:9 Reference Frame Representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E293B),
                                ReconOledBlack,
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    .border(1.dp, ReconCardBorder, RoundedCornerShape(14.dp))
            ) {
                // Background artistic visual cue
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = ReconCyan.copy(alpha = 0.5f),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = card.visualSummary,
                        fontSize = 12.sp,
                        color = ReconTextPrimary,
                        lineHeight = 16.sp,
                        maxLines = 3
                    )
                }

                // Reference timestamp badge bottom-right
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                        .border(0.6.dp, ReconNeonTeal.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "REF: ${card.referenceTimestamp}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReconCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Visual Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReconSurfaceDark, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Visual Summary & Physics", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ReconCyan)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.visualSummary,
                        fontSize = 12.sp,
                        color = ReconTextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LightMode, contentDescription = null, tint = ReconPurple, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lighting & Textures: ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ReconPurple)
                        Text(card.lightingTexture, fontSize = 11.sp, color = ReconTextSecondary, maxLines = 1)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Audio & Transcript snippet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReconSurfaceDark, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = ReconNeonTeal, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Audio & Speech Transcript", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ReconNeonTeal)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.audioTranscript,
                        fontSize = 12.sp,
                        color = ReconTextPrimary,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = ReconTextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(card.audioAtmosphere, fontSize = 11.sp, color = ReconTextMuted, maxLines = 1)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Master Prompt Code Block (Structured Header, Timeline, Style, Ref Anchor)
            CodeBlockView(
                codeText = card.videoPrompt,
                onCopy = onCopyPrompt,
                isCopied = isCopied,
                title = "📋 RECON MASTER PROMPT",
                testTagPrefix = "storyboard_prompt_${card.segmentIndex}"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Continuity Links (Between Scenes)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReconOledBlack, RoundedCornerShape(10.dp))
                    .border(0.8.dp, ReconCardBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = ReconTextMuted, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = card.prevSegmentSummary ?: "Opening",
                        fontSize = 11.sp,
                        color = ReconTextMuted,
                        maxLines = 1
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = card.nextSegmentSummary ?: "End",
                        fontSize = 11.sp,
                        color = ReconCyan,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}
