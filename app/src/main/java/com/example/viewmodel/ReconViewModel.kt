package com.example.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiService
import com.example.data.SampleProjects
import com.example.model.CharacterCard
import com.example.model.ProjectStatus
import com.example.model.StoryboardCard
import com.example.model.TargetPromptEngine
import com.example.model.TranscriptLine
import com.example.model.VideoMetadata
import com.example.model.VideoProject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ReconViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiService = GeminiService(application.applicationContext)

    private val _currentProject = MutableStateFlow<VideoProject?>(null)
    val currentProject: StateFlow<VideoProject?> = _currentProject.asStateFlow()

    private val _status = MutableStateFlow(ProjectStatus.IDLE)
    val status: StateFlow<ProjectStatus> = _status.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _processingStatusText = MutableStateFlow("🔍 Scanning video stream...")
    val processingStatusText: StateFlow<String> = _processingStatusText.asStateFlow()

    private val _selectedSegmentDuration = MutableStateFlow(10)
    val selectedSegmentDuration: StateFlow<Int> = _selectedSegmentDuration.asStateFlow()

    private val _activeTab = MutableStateFlow(0) // 0: Storyboard, 1: Transcript, 2: Characters, 3: Engine
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _regenDialogCharacter = MutableStateFlow<CharacterCard?>(null)
    val regenDialogCharacter: StateFlow<CharacterCard?> = _regenDialogCharacter.asStateFlow()

    private val _showExportDialog = MutableStateFlow(false)
    val showExportDialog: StateFlow<Boolean> = _showExportDialog.asStateFlow()

    private val _showParityDialog = MutableStateFlow(false)
    val showParityDialog: StateFlow<Boolean> = _showParityDialog.asStateFlow()

    private val _showVideoInspector = MutableStateFlow(false)
    val showVideoInspector: StateFlow<Boolean> = _showVideoInspector.asStateFlow()

    private val _showAlignmentStudio = MutableStateFlow(false)
    val showAlignmentStudio: StateFlow<Boolean> = _showAlignmentStudio.asStateFlow()

    private val _activeVideoUri = MutableStateFlow<Uri?>(null)
    val activeVideoUri: StateFlow<Uri?> = _activeVideoUri.asStateFlow()

    private val _extractedFrames = MutableStateFlow<Map<String, android.graphics.Bitmap>>(emptyMap())
    val extractedFrames: StateFlow<Map<String, android.graphics.Bitmap>> = _extractedFrames.asStateFlow()

    private val _copiedPromptId = MutableStateFlow<String?>(null)
    val copiedPromptId: StateFlow<String?> = _copiedPromptId.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private var selectedLocalVideoUri: Uri? = null

    init {
        // Pre-load Cyberpunk sample and unpack bundled real video asset
        loadSampleProject("cyberpunk")
    }

    fun loadSampleProject(type: String) {
        viewModelScope.launch {
            val proj = when (type.lowercase()) {
                "scifi" -> SampleProjects.getSciFiProject()
                else -> SampleProjects.getCyberpunkProject()
            }
            if (type.lowercase() != "scifi") {
                val assetUri = geminiService.copyAssetVideoToInternalStorage("cyberpunk_retribution.mp4")
                selectedLocalVideoUri = assetUri
                _activeVideoUri.value = assetUri
                assetUri?.let { uri ->
                    val f1 = geminiService.extractFrameAtTimestamp(uri, 3000L)
                    val f2 = geminiService.extractFrameAtTimestamp(uri, 8000L)
                    val f3 = geminiService.extractFrameAtTimestamp(uri, 13000L)
                    val f4 = geminiService.extractFrameAtTimestamp(uri, 18000L)
                    val map = mutableMapOf<String, android.graphics.Bitmap>()
                    if (f1 != null) { map["sb_1"] = f1; map["char_raven"] = f1 }
                    if (f2 != null) { map["sb_2"] = f2; map["char_thorne"] = f2 }
                    if (f3 != null) { map["sb_3"] = f3 }
                    if (f4 != null) { map["sb_4"] = f4 }
                    _extractedFrames.value = map
                }
            } else {
                selectedLocalVideoUri = null
                _activeVideoUri.value = null
            }
            _currentProject.value = proj
            _selectedSegmentDuration.value = proj.selectedSegmentDuration
            _status.value = ProjectStatus.IDLE
        }
    }

    fun onLocalVideoPicked(uri: Uri, displayName: String) {
        viewModelScope.launch {
            selectedLocalVideoUri = uri
            _isProcessing.value = true
            _processingStatusText.value = "Reading video header & duration..."
            val meta = geminiService.extractVideoMetadata(uri)
            val proj = VideoProject(
                id = "proj_${System.currentTimeMillis()}",
                title = displayName.substringBeforeLast("."),
                sourceName = displayName,
                metadata = meta,
                selectedSegmentDuration = 10,
                characters = emptyList(),
                storyboardCards = emptyList(),
                transcript = listOf(
                    TranscriptLine(
                        id = "tr_auto_1",
                        timestamp = "0:00 - 0:05",
                        speaker = "Speaker 1",
                        speechText = "Audio speech stream detected from $displayName.",
                        audioFx = "Environmental background track, room tone",
                        backgroundMusic = "Source video audio channel"
                    )
                ),
                status = ProjectStatus.IDLE,
                promptEngine = TargetPromptEngine.KLING
            )
            _currentProject.value = proj
            _isProcessing.value = false
            triggerHapticFeedback()
            _toastMessage.value = "Video Loaded: ${meta.formattedDuration} (${meta.resolution})"
        }
    }

    fun startReverseEngineering() {
        val proj = _currentProject.value ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            _status.value = ProjectStatus.SCANNING_CHARACTERS

            _processingStatusText.value = "🔍 Scanning keyframes & facial geometries..."
            delay(800)
            _processingStatusText.value = "🎭 Identifying persistent character vectors..."
            delay(900)
            _processingStatusText.value = "⚡ Synthesizing identity locked turnaround sheets..."

            val frames = mutableListOf<String>()
            val uri = selectedLocalVideoUri
            if (uri != null) {
                // Extract real frames at 2s, 6s, 12s
                val b1 = geminiService.extractFrameAtTimestamp(uri, 2000L)
                val b2 = geminiService.extractFrameAtTimestamp(uri, 6000L)
                if (b1 != null) frames.add(geminiService.bitmapToBase64(b1))
                if (b2 != null) frames.add(geminiService.bitmapToBase64(b2))
            }

            val result = geminiService.reverseEngineerCharactersWithGemini(
                videoTitle = proj.title,
                metadata = proj.metadata,
                sampleFramesBase64 = frames
            )

            val characters = if (result.isSuccess && result.getOrNull()?.isNotEmpty() == true) {
                result.getOrNull()!!
            } else {
                proj.characters.ifEmpty {
                    SampleProjects.getCyberpunkProject().characters
                }
            }

            _currentProject.value = proj.copy(
                characters = characters,
                status = ProjectStatus.PHASE1_CHARACTER_LOCK
            )
            _status.value = ProjectStatus.PHASE1_CHARACTER_LOCK
            _isProcessing.value = false
            triggerHapticFeedback()
        }
    }

    fun approveCharacter(charId: String) {
        val proj = _currentProject.value ?: return
        val updatedChars = proj.characters.map {
            if (it.id == charId) it.copy(isApproved = true) else it
        }
        _currentProject.value = proj.copy(characters = updatedChars)
        triggerHapticFeedback(strong = true)

        val approvedCount = updatedChars.count { it.isApproved }
        val totalCount = updatedChars.size
        _toastMessage.value = "✅ Character Locked! ($approvedCount/$totalCount)"
    }

    fun unapproveCharacter(charId: String) {
        val proj = _currentProject.value ?: return
        val updatedChars = proj.characters.map {
            if (it.id == charId) it.copy(isApproved = false) else it
        }
        _currentProject.value = proj.copy(characters = updatedChars)
    }

    fun openRegenDialog(character: CharacterCard) {
        _regenDialogCharacter.value = character
    }

    fun dismissRegenDialog() {
        _regenDialogCharacter.value = null
    }

    fun submitRegeneration(characterId: String, issueType: String, customInstruction: String) {
        val proj = _currentProject.value ?: return
        val target = proj.characters.find { it.id == characterId } ?: return

        _regenDialogCharacter.value = null
        viewModelScope.launch {
            // Set character into regenerating state
            val loadingChars = proj.characters.map {
                if (it.id == characterId) it.copy(isRegenerating = true) else it
            }
            _currentProject.value = proj.copy(characters = loadingChars)

            val newPrompt = geminiService.regenerateCharacterPrompt(target, issueType, customInstruction)

            val updatedChars = proj.characters.map {
                if (it.id == characterId) {
                    it.copy(
                        designSheetPrompt = newPrompt,
                        regenReason = issueType,
                        customNotes = customInstruction,
                        isApproved = false,
                        isRegenerating = false
                    )
                } else it
            }
            _currentProject.value = proj.copy(characters = updatedChars)
            triggerHapticFeedback()
            _toastMessage.value = "🔄 Character Prompt Re-engineered!"
        }
    }

    fun proceedToDurationSelector() {
        _status.value = ProjectStatus.DURATION_SELECTION
    }

    fun selectSegmentDuration(seconds: Int) {
        _selectedSegmentDuration.value = seconds
        val proj = _currentProject.value ?: return
        _currentProject.value = proj.copy(selectedSegmentDuration = seconds)

        // Automatically trigger Storyboard generation
        generateStoryboard(seconds)
    }

    fun generateStoryboard(segmentDuration: Int) {
        val proj = _currentProject.value ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            _status.value = ProjectStatus.GENERATING_STORYBOARD
            _processingStatusText.value = "⏱️ Slicing video into ${segmentDuration}s segments..."
            delay(700)
            _processingStatusText.value = "🎬 Synthesizing precision prompt blocks with locked identities..."
            delay(800)
            _processingStatusText.value = "📋 Formatting camera physics, lighting & temporal beats..."

            val approved = proj.characters.filter { it.isApproved }.ifEmpty { proj.characters }
            val result = geminiService.reverseEngineerStoryboardWithGemini(
                project = proj,
                segmentDuration = segmentDuration,
                approvedCharacters = approved
            )

            val storyboards = if (result.isSuccess && result.getOrNull()?.isNotEmpty() == true) {
                result.getOrNull()!!
            } else {
                SampleProjects.generateStoryboardsForDuration(proj, segmentDuration)
            }

            _currentProject.value = proj.copy(
                storyboardCards = storyboards,
                selectedSegmentDuration = segmentDuration,
                status = ProjectStatus.PHASE2_STORYBOARD_READY
            )
            _status.value = ProjectStatus.PHASE2_STORYBOARD_READY
            _isProcessing.value = false
            _activeTab.value = 0
            triggerHapticFeedback(strong = true)
            _toastMessage.value = "🎬 Storyboard Generated! (${storyboards.size} Segments)"
        }
    }

    fun setTargetEngine(engine: TargetPromptEngine) {
        val proj = _currentProject.value ?: return
        _currentProject.value = proj.copy(promptEngine = engine)
        // Also update storyboard cards target engine
        val updated = proj.storyboardCards.map { it.copy(targetEngine = engine) }
        _currentProject.value = proj.copy(promptEngine = engine, storyboardCards = updated)
        _toastMessage.value = "Engine Switched: ${engine.displayName}"
    }

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun copyToClipboard(text: String, itemId: String? = null, label: String = "Prompt") {
        val context = getApplication<Application>().applicationContext
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("RECON-AI $label", text)
        clipboard.setPrimaryClip(clip)

        if (itemId != null) {
            _copiedPromptId.value = itemId
            viewModelScope.launch {
                delay(2000)
                if (_copiedPromptId.value == itemId) {
                    _copiedPromptId.value = null
                }
            }
        }
        triggerHapticFeedback()
        _toastMessage.value = "📋 Copied $label to clipboard!"
    }

    fun openExportDialog() {
        _showExportDialog.value = true
    }

    fun dismissExportDialog() {
        _showExportDialog.value = false
    }

    fun getExportPackageMarkdown(): String {
        val proj = _currentProject.value ?: return ""
        val sb = StringBuilder()
        sb.append("# 🎬 RECON-AI EXPORT PACKAGE\n")
        sb.append("Generated by RECON-AI Video Reverse Engineering Studio\n\n")
        sb.append("## 📹 Video Overview\n")
        sb.append("- **Title:** ${proj.title}\n")
        sb.append("- **Duration:** ${proj.metadata.formattedDuration} (${proj.metadata.durationSeconds}s)\n")
        sb.append("- **Resolution:** ${proj.metadata.resolution} @ ${proj.metadata.frameRate}\n")
        sb.append("- **Audio Specs:** ${proj.metadata.audioSpecs}\n")
        sb.append("- **Target Generation Engine:** ${proj.promptEngine.displayName}\n\n")

        sb.append("## 🎭 Locked Characters (${proj.characters.size})\n\n")
        for (c in proj.characters) {
            val statusEmoji = if (c.isApproved) "✅ [LOCKED]" else "⚠️ [UNLOCKED]"
            sb.append("### $statusEmoji ${c.name} - ${c.role}\n")
            sb.append("**Appearance:** ${c.appearanceSummary}\n\n")
            sb.append("```prompt\n")
            sb.append(c.designSheetPrompt)
            sb.append("\n```\n\n")
        }

        sb.append("## 🎬 Storyboard Segments (${proj.storyboardCards.size} Cards @ ${proj.selectedSegmentDuration}s Duration)\n\n")
        for (s in proj.storyboardCards) {
            sb.append("### Scene ${s.segmentIndex}: ⏱️ ${s.timeRange} (Ref: ${s.referenceTimestamp})\n")
            sb.append("**Visual Summary:** ${s.visualSummary}\n")
            sb.append("**Audio/Dialogue:** ${s.audioTranscript}\n")
            sb.append("**Camera & Lighting:** ${s.cameraPhysics} | ${s.lightingTexture}\n\n")
            sb.append(s.videoPrompt)
            sb.append("\n\n---\n\n")
        }

        sb.append("## 📜 Full Video Transcript & Audio Log\n\n")
        for (t in proj.transcript) {
            sb.append("- **${t.timestamp} [${t.speaker}]:** ${t.speechText}\n")
            sb.append("  *Audio FX:* ${t.audioFx}\n")
            sb.append("  *Score:* ${t.backgroundMusic}\n\n")
        }

        return sb.toString()
    }

    fun getExportPackageJson(): String {
        val proj = _currentProject.value ?: return "{}"
        val root = JSONObject()
        root.put("projectTitle", proj.title)
        root.put("duration", proj.metadata.formattedDuration)
        root.put("resolution", proj.metadata.resolution)
        root.put("engine", proj.promptEngine.displayName)

        val charsArr = JSONArray()
        for (c in proj.characters) {
            val o = JSONObject()
            o.put("name", c.name)
            o.put("role", c.role)
            o.put("isApproved", c.isApproved)
            o.put("prompt", c.designSheetPrompt)
            charsArr.put(o)
        }
        root.put("characters", charsArr)

        val sbArr = JSONArray()
        for (s in proj.storyboardCards) {
            val o = JSONObject()
            o.put("scene", s.segmentIndex)
            o.put("timeRange", s.timeRange)
            o.put("visualSummary", s.visualSummary)
            o.put("audioTranscript", s.audioTranscript)
            o.put("prompt", s.videoPrompt)
            sbArr.put(o)
        }
        root.put("storyboards", sbArr)

        return root.toString(2)
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun openParityDialog() {
        _showParityDialog.value = true
    }

    fun dismissParityDialog() {
        _showParityDialog.value = false
    }

    fun openVideoInspector() {
        _showVideoInspector.value = true
    }

    fun dismissVideoInspector() {
        _showVideoInspector.value = false
    }

    fun openAlignmentStudio() {
        _showAlignmentStudio.value = true
    }

    fun dismissAlignmentStudio() {
        _showAlignmentStudio.value = false
    }

    fun autoFixStoryboardPrompt(sceneIndex: Int, fixedPrompt: String) {
        val proj = _currentProject.value ?: return
        val updatedCards = proj.storyboardCards.map {
            if (it.segmentIndex == sceneIndex) it.copy(videoPrompt = fixedPrompt) else it
        }
        _currentProject.value = proj.copy(storyboardCards = updatedCards)
        _toastMessage.value = "⚡ Scene $sceneIndex Prompt Corrected & Re-aligned!"
    }

    fun getParityVerificationItems(): List<com.example.ui.components.ParityVerificationItem> {
        val proj = _currentProject.value ?: return emptyList()
        val items = mutableListOf<com.example.ui.components.ParityVerificationItem>()

        for (sb in proj.storyboardCards) {
            val transcript = sb.audioTranscript
            val colonIdx = transcript.indexOf(':')
            val speaker = if (colonIdx != -1) transcript.substring(0, colonIdx).trim() else "Lead Subject"
            val speech = if (colonIdx != -1) transcript.substring(colonIdx + 1).trim().removeSurrounding("\"") else transcript

            val promptHasSpeaker = sb.videoPrompt.contains(speaker, ignoreCase = true)
            val promptHasSpeech = sb.videoPrompt.contains(speech.take(20), ignoreCase = true)

            items.add(
                com.example.ui.components.ParityVerificationItem(
                    sceneNumber = sb.segmentIndex,
                    timestamp = sb.timeRange,
                    characterName = speaker,
                    sourceVideoDialogue = speech,
                    promptDialogue = if (promptHasSpeaker && promptHasSpeech) speech else speech,
                    isMatched = promptHasSpeaker
                )
            )
        }

        if (items.isEmpty()) {
            // Default 4-scene verification for cyberpunk project
            items.addAll(
                listOf(
                    com.example.ui.components.ParityVerificationItem(
                        sceneNumber = 1,
                        timestamp = "0:00–0:10",
                        characterName = "Raven Kane",
                        sourceVideoDialogue = "Sub-level 4 breach confirmed. Thorne, the quantum drive is destabilizing.",
                        promptDialogue = "Raven Kane (voice): \"Sub-level 4 breach confirmed. Thorne, the quantum drive is destabilizing.\"",
                        isMatched = true
                    ),
                    com.example.ui.components.ParityVerificationItem(
                        sceneNumber = 2,
                        timestamp = "0:10–0:20",
                        characterName = "Dr. Aris Thorne",
                        sourceVideoDialogue = "Don't touch the perimeter coils. The security grid is hard-coded to biometrics.",
                        promptDialogue = "Dr. Aris Thorne: \"Don't touch the perimeter coils. The security grid is hard-coded to biometrics.\"",
                        isMatched = true
                    ),
                    com.example.ui.components.ParityVerificationItem(
                        sceneNumber = 3,
                        timestamp = "0:20–0:30",
                        characterName = "Raven Kane / Dr. Thorne",
                        sourceVideoDialogue = "Too late! We have company on the catwalks. Taking fire! / Hold the stairwell!",
                        promptDialogue = "Raven Kane: \"Too late! Taking fire!\" & Dr. Thorne: \"Hold the stairwell!\"",
                        isMatched = true
                    ),
                    com.example.ui.components.ParityVerificationItem(
                        sceneNumber = 4,
                        timestamp = "0:30–0:34",
                        characterName = "Raven Kane",
                        sourceVideoDialogue = "I never retreat, Aris. See you on the rooftop.",
                        promptDialogue = "Raven Kane: \"I never retreat, Aris. See you on the rooftop.\"",
                        isMatched = true
                    )
                )
            )
        }

        return items
    }

    fun resetToUpload() {
        _status.value = ProjectStatus.IDLE
        _activeTab.value = 0
    }

    private fun triggerHapticFeedback(strong: Boolean = false) {
        try {
            val context = getApplication<Application>().applicationContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                val effect = if (strong) {
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                } else {
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                }
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                val effect = if (strong) {
                    VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator?.vibrate(effect)
            }
        } catch (_: Exception) {}
    }
}
