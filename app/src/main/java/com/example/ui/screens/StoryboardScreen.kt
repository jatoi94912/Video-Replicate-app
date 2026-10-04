package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryboardCard
import com.example.model.TargetPromptEngine
import com.example.model.TranscriptLine
import com.example.model.VideoProject
import com.example.ui.components.CharacterCardView
import com.example.ui.components.StoryboardCardView
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
fun StoryboardScreen(
    project: VideoProject,
    activeTab: Int,
    copiedPromptId: String?,
    onTabSelected: (Int) -> Unit,
    onEngineChanged: (TargetPromptEngine) -> Unit,
    onCopyPrompt: (String, String) -> Unit,
    onOpenExport: () -> Unit,
    onBack: () -> Unit,
    onRegenCharacter: (com.example.model.CharacterCard) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEngineMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ReconOledBlack)
    ) {
        // Top Sticky Header
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
                            onClick = onBack,
                            modifier = Modifier.size(34.dp).testTag("storyboard_back_button")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ReconTextSecondary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "🎬 RECON STORYBOARD STUDIO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = project.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ReconTextPrimary
                            )
                        }
                    }

                    // Target AI Engine Selector Pill
                    Box {
                        Box(
                            modifier = Modifier
                                .background(ReconCardDark, RoundedCornerShape(10.dp))
                                .border(1.dp, ReconCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .clickable { showEngineMenu = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("engine_selector_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Tune, contentDescription = null, tint = ReconCyan, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = project.promptEngine.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ReconCyan
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showEngineMenu,
                            onDismissRequest = { showEngineMenu = false },
                            modifier = Modifier.background(ReconSurfaceDark)
                        ) {
                            for (engine in TargetPromptEngine.values()) {
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(engine.displayName, fontWeight = FontWeight.Bold, color = ReconTextPrimary)
                                            Text(engine.extensionNote, fontSize = 10.sp, color = ReconTextSecondary)
                                        }
                                    },
                                    onClick = {
                                        onEngineChanged(engine)
                                        showEngineMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MiniBadge("Duration", "${project.metadata.formattedDuration} (${project.selectedSegmentDuration}s Segments)")
                    MiniBadge("Storyboard Cards", "${project.storyboardCards.size} Cards")
                    MiniBadge("Locked Cast", "${project.characters.size} Characters")
                }
            }
        }

        // Navigation Tabs (Storyboard, Full Transcript, Characters)
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            containerColor = ReconSurfaceDark,
            contentColor = ReconCyan,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                    color = ReconCyan
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { onTabSelected(0) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🎬 Storyboard Cards (${project.storyboardCards.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )

            Tab(
                selected = activeTab == 1,
                onClick = { onTabSelected(1) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("📜 Video Transcript & Audio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )

            Tab(
                selected = activeTab == 2,
                onClick = { onTabSelected(2) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🎭 Locked Cast (${project.characters.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (activeTab) {
                0 -> {
                    // Vertical Stack of Storyboard Cards
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("storyboard_cards_list"),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(project.storyboardCards, key = { it.id }) { card ->
                            StoryboardCardView(
                                card = card,
                                isCopied = copiedPromptId == "storyboard_prompt_${card.segmentIndex}",
                                onCopyPrompt = {
                                    onCopyPrompt(card.videoPrompt, "storyboard_prompt_${card.segmentIndex}")
                                }
                            )
                        }
                    }
                }

                1 -> {
                    // Full Video Transcript & Audio Breakdown View
                    FullTranscriptView(transcript = project.transcript, onCopy = onCopyPrompt)
                }

                2 -> {
                    // Locked Cast Character Sheets View
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(project.characters, key = { it.id }) { character ->
                            CharacterCardView(
                                character = character,
                                isCopied = copiedPromptId == "char_${character.id}",
                                onApprove = {},
                                onUnapprove = {},
                                onRegen = { onRegenCharacter(character) },
                                onCopyPrompt = { onCopyPrompt(character.designSheetPrompt, "char_${character.id}") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // Sticky Bottom Export FAB Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ReconSurfaceDark)
                .border(1.dp, ReconCardBorder)
                .padding(14.dp)
        ) {
            Button(
                onClick = onOpenExport,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ReconCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("export_full_package_button")
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📤 Export Full Package (.txt / .json)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FullTranscriptView(
    transcript: List<TranscriptLine>,
    onCopy: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("transcript_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReconCardDark, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "📜 Synchronized Timeline Transcript",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ReconCyan
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Complete scene-by-scene spoken dialogue, environmental sound design, and music score timeline.",
                        fontSize = 11.sp,
                        color = ReconTextSecondary
                    )
                }
            }
        }

        items(transcript, key = { it.id }) { line ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ReconCardDark, RoundedCornerShape(14.dp))
                    .border(1.dp, ReconCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Speaker pill
                        Box(
                            modifier = Modifier
                                .background(ReconSurfaceDark, RoundedCornerShape(8.dp))
                                .border(0.8.dp, ReconCyan, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(line.speaker, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ReconCyan)
                        }

                        // Timestamp
                        Text(
                            line.timestamp,
                            fontSize = 11.sp,
                            color = ReconNeonTeal,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"${line.speechText}\"",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ReconTextPrimary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Audio effects & score
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = ReconTextMuted, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "FX: ${line.audioFx}",
                            fontSize = 11.sp,
                            color = ReconTextSecondary,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎵 Score: ${line.backgroundMusic}",
                            fontSize = 10.sp,
                            color = ReconTextMuted,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniBadge(label: String, value: String) {
    Box(
        modifier = Modifier
            .background(ReconOledBlack, RoundedCornerShape(6.dp))
            .border(0.6.dp, ReconCardBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label: ", fontSize = 10.sp, color = ReconTextMuted)
            Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ReconTextPrimary)
        }
    }
}
