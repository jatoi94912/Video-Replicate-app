package com.example.ui.components

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.model.VideoProject
import com.example.ui.theme.ReconCardBorder
import com.example.ui.theme.ReconCardDark
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconNeonTeal
import com.example.ui.theme.ReconOledBlack
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextMuted
import com.example.ui.theme.ReconTextPrimary
import com.example.ui.theme.ReconTextSecondary
import kotlinx.coroutines.delay

@Composable
fun VideoInspectorDialog(
    project: VideoProject,
    videoUri: Uri?,
    extractedFrame: Bitmap?,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionMs by remember { mutableFloatStateOf(0f) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    val totalDurationSeconds = project.metadata.durationSeconds.toFloat().coerceAtLeast(10f)

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(200)
            videoViewRef?.let {
                if (it.isPlaying) {
                    currentPositionMs = it.currentPosition.toFloat()
                } else if (it.currentPosition >= it.duration && it.duration > 0) {
                    isPlaying = false
                }
            }
        }
    }

    val currentSeconds = (currentPositionMs / 1000f).toInt()
    val activeTranscript = project.transcript.find {
        val parts = it.timestamp.split("-")
        val start = parts.getOrNull(0)?.trim()?.let { parseTime(it) } ?: 0
        val end = parts.getOrNull(1)?.trim()?.let { parseTime(it) } ?: 999
        currentSeconds in start..end
    } ?: project.transcript.firstOrNull()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ReconSurfaceDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ReconCardBorder, RoundedCornerShape(20.dp))
                .testTag("video_inspector_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Movie, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "REAL VIDEO & FRAME INSPECTOR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = project.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = ReconTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Video View Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ReconOledBlack)
                        .border(1.dp, ReconCardBorder, RoundedCornerShape(12.dp))
                ) {
                    if (videoUri != null) {
                        AndroidView(
                            factory = { ctx ->
                                VideoView(ctx).apply {
                                    setVideoURI(videoUri)
                                    setOnPreparedListener { mp ->
                                        mp.isLooping = true
                                    }
                                    videoViewRef = this
                                }
                            },
                            modifier = Modifier.matchParentSize()
                        )
                    } else if (extractedFrame != null) {
                        Image(
                            bitmap = extractedFrame.asImageBitmap(),
                            contentDescription = "Video Frame",
                            modifier = Modifier.matchParentSize()
                        )
                    } else {
                        Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                            Text("Loading Video Stream...", color = ReconTextSecondary, fontSize = 12.sp)
                        }
                    }

                    // Timestamp overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60),
                            color = ReconCyan,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Playback Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            videoViewRef?.let {
                                if (it.isPlaying) {
                                    it.pause()
                                    isPlaying = false
                                } else {
                                    it.start()
                                    isPlaying = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = ReconCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Slider(
                        value = (currentPositionMs / 1000f).coerceIn(0f, totalDurationSeconds),
                        onValueChange = { sec ->
                            currentPositionMs = sec * 1000f
                            videoViewRef?.seekTo((sec * 1000).toInt())
                        },
                        valueRange = 0f..totalDurationSeconds,
                        colors = SliderDefaults.colors(
                            thumbColor = ReconCyan,
                            activeTrackColor = ReconCyan,
                            inactiveTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            videoViewRef?.seekTo(0)
                            currentPositionMs = 0f
                        }
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = "Restart", tint = ReconTextSecondary, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Synchronized Dialogue Subtitle at current time
                activeTranscript?.let { line ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ReconCardDark, RoundedCornerShape(12.dp))
                            .border(1.dp, ReconNeonTeal.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🎤 Spoken Dialogue at ${line.timestamp}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ReconNeonTeal
                                )
                                Text(
                                    text = line.speaker,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ReconCyan
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${line.speechText}\"",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ReconTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ReconCyan, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Text("Close Video Inspector", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun parseTime(str: String): Int {
    val parts = str.split(":")
    if (parts.size == 2) {
        val m = parts[0].toIntOrNull() ?: 0
        val s = parts[1].toIntOrNull() ?: 0
        return m * 60 + s
    }
    return 0
}
