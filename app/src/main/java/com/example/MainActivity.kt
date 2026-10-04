package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ProjectStatus
import com.example.ui.components.DurationSelectorScreen
import com.example.ui.components.ExportPackageDialog
import com.example.ui.components.RegenDialog
import com.example.ui.screens.CharacterLockScreen
import com.example.ui.screens.StoryboardScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconOledBlack
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextPrimary
import com.example.viewmodel.ReconViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ReconViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
                val status by viewModel.status.collectAsStateWithLifecycle()
                val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
                val processingStatusText by viewModel.processingStatusText.collectAsStateWithLifecycle()
                val selectedDuration by viewModel.selectedSegmentDuration.collectAsStateWithLifecycle()
                val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
                val regenChar by viewModel.regenDialogCharacter.collectAsStateWithLifecycle()
                val showExport by viewModel.showExportDialog.collectAsStateWithLifecycle()
                val showParity by viewModel.showParityDialog.collectAsStateWithLifecycle()
                val showVideoInspector by viewModel.showVideoInspector.collectAsStateWithLifecycle()
                val activeVideoUri by viewModel.activeVideoUri.collectAsStateWithLifecycle()
                val extractedFrames by viewModel.extractedFrames.collectAsStateWithLifecycle()
                val copiedPromptId by viewModel.copiedPromptId.collectAsStateWithLifecycle()
                val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(toastMessage) {
                    toastMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                // Back navigation handlers
                BackHandler(enabled = status != ProjectStatus.IDLE) {
                    when (status) {
                        ProjectStatus.PHASE2_STORYBOARD_READY -> viewModel.proceedToDurationSelector()
                        ProjectStatus.DURATION_SELECTION -> {
                            val proj = currentProject
                            if (proj?.characters?.isNotEmpty() == true) {
                                viewModel.startReverseEngineering()
                            } else {
                                viewModel.resetToUpload()
                            }
                        }
                        ProjectStatus.PHASE1_CHARACTER_LOCK -> viewModel.resetToUpload()
                        else -> viewModel.resetToUpload()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = ReconOledBlack
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (status) {
                            ProjectStatus.IDLE, ProjectStatus.SCANNING_CHARACTERS -> {
                                UploadScreen(
                                    currentProject = currentProject,
                                    isProcessing = isProcessing,
                                    processingStatusText = processingStatusText,
                                    onVideoSelected = { uri, name -> viewModel.onLocalVideoPicked(uri, name) },
                                    onSampleSelected = { type -> viewModel.loadSampleProject(type) },
                                    onStartScan = { viewModel.startReverseEngineering() },
                                    onInspectVideo = { viewModel.openVideoInspector() }
                                )
                            }

                            ProjectStatus.PHASE1_CHARACTER_LOCK -> {
                                currentProject?.let { proj ->
                                    CharacterLockScreen(
                                        project = proj,
                                        copiedPromptId = copiedPromptId,
                                        onApprove = { viewModel.approveCharacter(it) },
                                        onUnapprove = { viewModel.unapproveCharacter(it) },
                                        onRegen = { viewModel.openRegenDialog(it) },
                                        onCopyPrompt = { text, id -> viewModel.copyToClipboard(text, id, "Design Sheet") },
                                        onProceedToPhase2 = { viewModel.proceedToDurationSelector() },
                                        onBackToUpload = { viewModel.resetToUpload() }
                                    )
                                }
                            }

                            ProjectStatus.DURATION_SELECTION, ProjectStatus.GENERATING_STORYBOARD -> {
                                DurationSelectorScreen(
                                    currentDuration = selectedDuration,
                                    videoLengthSeconds = currentProject?.metadata?.durationSeconds ?: 30,
                                    onDurationSelected = { sec -> viewModel.selectSegmentDuration(sec) }
                                )
                            }

                            ProjectStatus.PHASE2_STORYBOARD_READY -> {
                                currentProject?.let { proj ->
                                    StoryboardScreen(
                                        project = proj,
                                        activeTab = activeTab,
                                        copiedPromptId = copiedPromptId,
                                        extractedFrames = extractedFrames,
                                        onTabSelected = { viewModel.setActiveTab(it) },
                                        onEngineChanged = { viewModel.setTargetEngine(it) },
                                        onCopyPrompt = { text, id -> viewModel.copyToClipboard(text, id, "Storyboard Prompt") },
                                        onOpenExport = { viewModel.openExportDialog() },
                                        onOpenParityDialog = { viewModel.openParityDialog() },
                                        onInspectVideo = { viewModel.openVideoInspector() },
                                        onBack = { viewModel.proceedToDurationSelector() },
                                        onRegenCharacter = { viewModel.openRegenDialog(it) }
                                    )
                                }
                            }
                        }

                        // Processing Overlay (Animated Scanning Loader)
                        AnimatedVisibility(
                            visible = isProcessing,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(56.dp),
                                        color = ReconCyan,
                                        strokeWidth = 3.dp
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = processingStatusText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ReconTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "RECON-AI Neural Reverse Engineering Engine",
                                        fontSize = 12.sp,
                                        color = ReconCyan
                                    )
                                }
                            }
                        }

                        // Regeneration Dialog Modal
                        regenChar?.let { char ->
                            RegenDialog(
                                character = char,
                                onDismiss = { viewModel.dismissRegenDialog() },
                                onSubmit = { issue, note ->
                                    viewModel.submitRegeneration(char.id, issue, note)
                                }
                            )
                        }

                        // Export Package Dialog Modal
                        if (showExport) {
                            ExportPackageDialog(
                                markdownText = viewModel.getExportPackageMarkdown(),
                                jsonText = viewModel.getExportPackageJson(),
                                onDismiss = { viewModel.dismissExportDialog() },
                                onCopy = { content, label ->
                                    viewModel.copyToClipboard(content, null, label)
                                }
                            )
                        }

                        // Parity Verification Audit Dialog Modal
                        if (showParity) {
                            currentProject?.let { proj ->
                                com.example.ui.components.ParityValidationDialog(
                                    projectTitle = proj.title,
                                    verificationItems = viewModel.getParityVerificationItems(),
                                    onDismiss = { viewModel.dismissParityDialog() }
                                )
                            }
                        }

                        // Real Video & Frame Inspector Modal
                        if (showVideoInspector) {
                            currentProject?.let { proj ->
                                com.example.ui.components.VideoInspectorDialog(
                                    project = proj,
                                    videoUri = activeVideoUri,
                                    extractedFrame = extractedFrames["sb_1"],
                                    onDismiss = { viewModel.dismissVideoInspector() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
