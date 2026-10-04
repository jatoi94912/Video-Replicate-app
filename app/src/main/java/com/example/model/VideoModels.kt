package com.example.model

enum class ProjectStatus {
    IDLE,
    SCANNING_CHARACTERS,
    PHASE1_CHARACTER_LOCK,
    DURATION_SELECTION,
    GENERATING_STORYBOARD,
    PHASE2_STORYBOARD_READY
}

enum class TargetPromptEngine(val displayName: String, val extensionNote: String) {
    KLING("Kling AI", "Optimized for Kling 1.5 camera control & high realism"),
    SORA("OpenAI Sora", "Structured for Sora timeline continuity & physics"),
    SEEDANCE("Seedance", "Engineered for Seedance cinematic render"),
    RUNWAY_GEN3("Runway Gen-3 Alpha", "Multi-prompt director mode format")
}

data class CharacterCard(
    val id: String,
    val name: String,
    val role: String,
    val appearanceSummary: String,
    val designSheetPrompt: String,
    val isApproved: Boolean = false,
    val refTimestamp: String = "0:02",
    val refFrameBase64: String? = null,
    val avatarColorHex: Long = 0xFF00F2FE,
    val regenReason: String? = null,
    val customNotes: String? = null,
    val isRegenerating: Boolean = false
)

data class StoryboardCard(
    val id: String,
    val segmentIndex: Int,
    val totalSegments: Int,
    val timeRange: String, // e.g. "0:00–0:10"
    val startSeconds: Int,
    val endSeconds: Int,
    val referenceTimestamp: String,
    val refFrameBase64: String? = null,
    val videoPrompt: String,
    val visualSummary: String,
    val audioTranscript: String,
    val audioAtmosphere: String,
    val cameraPhysics: String,
    val lightingTexture: String,
    val prevSegmentSummary: String? = null,
    val nextSegmentSummary: String? = null,
    val targetEngine: TargetPromptEngine = TargetPromptEngine.KLING
)

data class TranscriptLine(
    val id: String,
    val timestamp: String,
    val speaker: String,
    val speechText: String,
    val audioFx: String,
    val backgroundMusic: String
)

data class VideoMetadata(
    val durationSeconds: Int,
    val formattedDuration: String,
    val resolution: String = "1920x1080 (16:9)",
    val frameRate: String = "24 FPS",
    val audioSpecs: String = "Stereo 48kHz, 24-bit PCM",
    val fileSizeFormatted: String = "14.2 MB"
)

data class VideoProject(
    val id: String,
    val title: String,
    val sourceName: String,
    val metadata: VideoMetadata,
    val selectedSegmentDuration: Int = 10, // 10s, 15s, or 30s
    val characters: List<CharacterCard> = emptyList(),
    val storyboardCards: List<StoryboardCard> = emptyList(),
    val transcript: List<TranscriptLine> = emptyList(),
    val status: ProjectStatus = ProjectStatus.IDLE,
    val promptEngine: TargetPromptEngine = TargetPromptEngine.KLING
)
