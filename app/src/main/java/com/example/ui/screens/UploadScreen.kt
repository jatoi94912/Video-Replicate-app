package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoProject
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
fun UploadScreen(
    currentProject: VideoProject?,
    isProcessing: Boolean,
    processingStatusText: String,
    onVideoSelected: (Uri, String) -> Unit,
    onSampleSelected: (String) -> Unit,
    onStartScan: () -> Unit,
    onInspectVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment ?: "user_upload.mp4"
            onVideoSelected(uri, fileName)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ReconOledBlack)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Branding Banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(ReconCardDark, CircleShape)
                    .border(1.5.dp, ReconCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Radar,
                    contentDescription = "RECON-AI Logo",
                    tint = ReconCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "RECON-AI",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ReconTextPrimary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Video-to-Prompt Reverse Engineering Studio",
                    fontSize = 11.sp,
                    color = ReconCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Dashed Border Upload Zone
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ReconSurfaceDark)
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(listOf(ReconCyan, ReconNeonTeal)),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(enabled = !isProcessing) {
                    videoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    )
                }
                .padding(24.dp)
                .testTag("upload_dropzone"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(60.dp)
                        .background(ReconCardDark, CircleShape)
                        .border(1.dp, ReconCyan.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload Video",
                        tint = ReconCyan,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tap to Select Video (MP4 / MOV / WebM)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ReconTextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Supports local device videos or choose a studio benchmark sample below",
                    fontSize = 12.sp,
                    color = ReconTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Selected Video Specs & Duration Preview
        if (currentProject != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = ReconCardDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ReconCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("video_specs_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VideoFile, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currentProject.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }

                        // Auto-detected duration badge
                        Box(
                            modifier = Modifier
                                .background(ReconOledBlack, RoundedCornerShape(8.dp))
                                .border(1.dp, ReconNeonTeal, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "⏱️ ${currentProject.metadata.formattedDuration} detected",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconNeonTeal,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecBadge("Resolution", currentProject.metadata.resolution)
                        SpecBadge("Frame Rate", currentProject.metadata.frameRate)
                        SpecBadge("Characters", "${currentProject.characters.size} Detected")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onInspectVideo,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ReconCyan),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ReconCyan.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("play_inspect_video_button")
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("▶️ Play & Inspect Real Video Source", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Scanning CTA Button
            Button(
                onClick = onStartScan,
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ReconCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_scan_button")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(processingStatusText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🔍 Scan Characters & Reverse Engineer →", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Studio Benchmark Samples Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "⚡ Studio Benchmark Samples (1-Tap Test)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ReconTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pre-loaded with deep second-by-second timeline, audio transcripts, and characters",
                fontSize = 11.sp,
                color = ReconTextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SampleCard(
                    title = "Cyberpunk: Retribution",
                    duration = "0:34",
                    genre = "Action / Cyber-Noir",
                    isSelected = currentProject?.id == "proj_cyberpunk",
                    onClick = { onSampleSelected("cyberpunk") },
                    modifier = Modifier.weight(1f)
                )

                SampleCard(
                    title = "Orbital Horizon",
                    duration = "0:45",
                    genre = "Sci-Fi / Space Station",
                    isSelected = currentProject?.id == "proj_scifi",
                    onClick = { onSampleSelected("scifi") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun SpecBadge(label: String, value: String) {
    Box(
        modifier = Modifier
            .background(ReconOledBlack, RoundedCornerShape(8.dp))
            .border(0.6.dp, ReconCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column {
            Text(text = label, fontSize = 9.sp, color = ReconTextMuted)
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ReconTextPrimary)
        }
    }
}

@Composable
private fun SampleCard(
    title: String,
    duration: String,
    genre: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(if (isSelected) ReconSurfaceDark else ReconCardDark, RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) ReconCyan else ReconCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = if (isSelected) ReconCyan else ReconTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = duration,
                    fontSize = 11.sp,
                    color = ReconNeonTeal,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ReconTextPrimary,
                maxLines = 1
            )

            Text(
                text = genre,
                fontSize = 10.sp,
                color = ReconTextMuted
            )
        }
    }
}
