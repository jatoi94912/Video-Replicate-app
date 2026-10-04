package com.example.data

import com.example.model.CharacterCard
import com.example.model.StoryboardCard
import com.example.model.TargetPromptEngine
import com.example.model.TranscriptLine
import com.example.model.VideoMetadata
import com.example.model.VideoProject

object SampleProjects {

    fun getCyberpunkProject(): VideoProject {
        val characters = listOf(
            CharacterCard(
                id = "char_raven",
                name = "Raven Kane",
                role = "Lead Infiltration Operative",
                appearanceSummary = "East-Asian female, 28, athletic build, undercut dark raven hair with electric blue tips. Wears matte obsidian carbon-fiber tactical trench coat with glowing cyan micro-seams, augmented cybernetic right iris with hex-grid reticle.",
                designSheetPrompt = """[ref_image] = master turnaround sheet
CHARACTER: Raven Kane, female 28, elite cyberpunk operative.
VIEWS: Front orthographic, 3/4 left profile, full back view, high-detail macro headshot.
ATTIRE: High-collar matte black nanoweave coat, ballistic tactical harness, modular utility pouches, fingerless carbon-knuckle gloves.
FACIAL DETAILS: Sharp jawline, faint chrome scar running down left temple, glowing neon-cyan biometric right iris, calm hyper-focused expression.
LIGHTING: Studio volumetric rim lighting, subtle rain droplets glistening on synthetic leather, 8k resolution, Unreal Engine 5 render style, pure neutral dark studio backdrop.""".trimIndent(),
                isApproved = false,
                refTimestamp = "0:04",
                avatarColorHex = 0xFF00F2FE
            ),
            CharacterCard(
                id = "char_thorne",
                name = "Dr. Aris Thorne",
                role = "Synthetic Neuro-Detective",
                appearanceSummary = "Caucasian male, mid-40s, greying silver temples, heavy fatigue under eyes. High-collared distressed slate duster over tailored synthetic vest, exposed brushed brass cybernetic left forearm with exposed glowing vacuum tubes.",
                designSheetPrompt = """[ref_image] = master turnaround sheet
CHARACTER: Dr. Aris Thorne, male 45, weary cyber-detective.
VIEWS: Orthographic front, profile 3/4 right, full-length back, close-up emotive portrait.
ATTIRE: Weathered heavy wool slate trench coat, loosened tie, dark charcoal suit vest, brushed brass hydraulic arm prosthetic visible from rolled sleeve.
FACIAL DETAILS: Deep-set brooding hazel eyes, 3-day stubble, furrowed brow, faint blue sub-dermal neuro-interface pulse along neck carotid.
LIGHTING: Hard noir rim light, moody amber neon key, particulate smoke in air, photorealistic octane render, 8k identity locked master still.""".trimIndent(),
                isApproved = false,
                refTimestamp = "0:12",
                avatarColorHex = 0xFFF59E0B
            )
        )

        val transcript = listOf(
            TranscriptLine(
                id = "tr_1",
                timestamp = "0:01 - 0:05",
                speaker = "Raven Kane",
                speechText = "Sub-level 4 breach confirmed. Thorne, the quantum drive is destabilizing.",
                audioFx = "Heavy rain pounding metal grating, distant neon hum, muffled pneumatic door release",
                backgroundMusic = "Low pulsating sub-bass drone with eerie synthesizer arpeggio"
            ),
            TranscriptLine(
                id = "tr_2",
                timestamp = "0:06 - 0:11",
                speaker = "Dr. Aris Thorne",
                speechText = "Don't touch the perimeter coils. The security grid is hard-coded to biometrics.",
                audioFx = "Radio static crackle, mechanical whir of brass prosthetic forearm, high-voltage spark hiss",
                backgroundMusic = "Bass swell intensifying with distorted percussive industrial ticking"
            ),
            TranscriptLine(
                id = "tr_3",
                timestamp = "0:12 - 0:19",
                speaker = "Raven Kane",
                speechText = "Too late. We have company on the catwalks.",
                audioFx = "Heavy tactical combat boots sprinting on wet sheet metal, sniper targeting laser chime",
                backgroundMusic = "Adrenaline-fueled breakbeat percussion kicking in, dark synthwave crescendo"
            ),
            TranscriptLine(
                id = "tr_4",
                timestamp = "0:20 - 0:28",
                speaker = "Dr. Aris Thorne",
                speechText = "Extraction shuttle ETA is twenty seconds. Hold the stairwell!",
                audioFx = "Silenced magnetic pulse shots, glass shattering in slow motion, siren klaxon echo",
                backgroundMusic = "Full orchestral strings blended with aggressive modular bass synthesis"
            ),
            TranscriptLine(
                id = "tr_5",
                timestamp = "0:29 - 0:34",
                speaker = "Raven Kane",
                speechText = "I never retreat, Aris. See you on the rooftop.",
                audioFx = "Grappling cable firing recoil, grapple zip-line whir, deafening thunderclap",
                backgroundMusic = "Epic resolving synth chord sustaining into echoing reverb tail"
            )
        )

        return VideoProject(
            id = "proj_cyberpunk",
            title = "Cyberpunk: Neon Retribution",
            sourceName = "cyberpunk_action_trailer.mp4",
            metadata = VideoMetadata(
                durationSeconds = 34,
                formattedDuration = "0:34",
                resolution = "3840x2160 (16:9 4K UHD)",
                frameRate = "24.00 FPS",
                audioSpecs = "Dolby Atmos 5.1 / 48kHz 24-bit",
                fileSizeFormatted = "48.6 MB"
            ),
            selectedSegmentDuration = 10,
            characters = characters,
            storyboardCards = emptyList(), // Generated on Phase 2 selection!
            transcript = transcript,
            promptEngine = TargetPromptEngine.KLING
        )
    }

    fun generateStoryboardsForDuration(project: VideoProject, segmentDuration: Int): List<StoryboardCard> {
        return when (segmentDuration) {
            10 -> generate10sSegments(project)
            15 -> generate15sSegments(project)
            else -> generate30sSegments(project)
        }
    }

    private fun generate10sSegments(project: VideoProject): List<StoryboardCard> {
        return listOf(
            StoryboardCard(
                id = "sb_1",
                segmentIndex = 1,
                totalSegments = 4,
                timeRange = "0:00–0:10",
                startSeconds = 0,
                endSeconds = 10,
                referenceTimestamp = "0:04",
                visualSummary = "Rain-drenched alley in Neo-Tokyo. Raven drops silently from iron fire escape onto steaming grated catwalk, tactical optics glowing cyan in deep shadows.",
                audioTranscript = "\"Sub-level 4 breach confirmed. Thorne, the quantum drive is destabilizing.\" - Radio crackle, rain hum.",
                audioAtmosphere = "Heavy urban rainfall, distant aerial cruiser sirens, electrical transformer hum.",
                cameraPhysics = "Low-angle crane tracking shot, descending smoothly with subject momentum, settling into fluid 35mm medium close-up.",
                lightingTexture = "Wet asphalt reflections, 5600K cyan neon highlights hitting soaked carbon fibers, deep 0.5 lux shadow falloff.",
                prevSegmentSummary = "Initial Opening Sequence (Master Ref Frame Locked)",
                nextSegmentSummary = "Scene 2: Alleyway confrontation & security coil alert",
                videoPrompt = """```prompt
HEADER:
Format: Photorealistic Cinematic Film | Ratio: 16:9 | Duration: 10s | Atmosphere: Dystopian Cyber-Noir Rainstorm | Audio: Rain patter, transformer buzz, muffled radio | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven Kane)

TIMELINE:
0:00–0:03: Extreme high-angle looking down into narrow neon-lit alleyway; rain pours through steam vents. A dark figure leaps from upper fire escape.
0:03–0:06: Camera descends rapidly alongside falling subject; camera physics match free-fall drag, landing softly on metal grating with micro-impact camera shake.
0:06–0:10: Subject (Raven Kane, [ref_image]) stands in low athletic crouch; right biometric cyan iris glints; she taps temple comms device while surveying perimeter.

STYLE BOOSTERS:
Camera physics: Anamorphic Panavision C-series lens, shallow depth of field (T1.4), horizontal blue streak flares, optical barrel distortion.
Lighting: Volumetric rain diffusion, high-contrast cyan key light (#00F2FE) from billboard above, deep obsidian shadows.
Texture: Micro-droplets sliding down waterproof ballistic trench coat, steam condensation on camera lens edge.
Negative constraints: No cartoon stylization, no oversaturated skin, no frame stutter, no floating objects.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_2",
                segmentIndex = 2,
                totalSegments = 4,
                timeRange = "0:10–0:20",
                startSeconds = 10,
                endSeconds = 20,
                referenceTimestamp = "0:15",
                visualSummary = "Dr. Aris Thorne emerges from steam vent corridor. His mechanical brass forearm rotates into diagnostic mode as high-voltage perimeter coils spark violently.",
                audioTranscript = "\"Don't touch the perimeter coils. The security grid is hard-coded to biometrics.\" / \"Too late. We have company on the catwalks.\"",
                audioAtmosphere = "Electrical arc snaps, mechanical servo hum, rising synthesizer drone tension.",
                cameraPhysics = "Steadicam lateral tracking push-in; orbits from Thorne's shoulder over to Raven's profile in seamless parity.",
                lightingTexture = "Flickering amber sodium-vapor lamps contrasting with intermittent blue arc flashes.",
                prevSegmentSummary = "Scene 1: Raven drops into catwalk alley",
                nextSegmentSummary = "Scene 3: Gunfire & tactical ambush on the catwalks",
                videoPrompt = """```prompt
HEADER:
Format: Photorealistic Cinematic Film | Ratio: 16:9 | Duration: 10s | Atmosphere: High-Stakes Tactical Suspense | Audio: Heavy electrical arcing, mechanical gear whirs | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Dr. Aris Thorne & Raven Kane)

TIMELINE:
0:10–0:13: Steadicam push through billowing white alley steam; Dr. Thorne ([ref_image]) emerges in heavy slate trench coat, adjusting his rolled sleeve.
0:13–0:16: Close-up on Thorne's brushed brass prosthetic forearm as miniature hydraulic valves cycle and emit high-pressure vapor; fingers flex with tactile weight.
0:16–0:20: Two-shot profile: Thorne steps beside Raven; both look up simultaneously as red laser targeting grid sweeps across the brick facade behind them.

STYLE BOOSTERS:
Camera physics: 50mm prime, handheld breathing motion with stabilizer dampening, subtle rack focus between prosthetic gears and background laser dot.
Lighting: 2700K warm tungsten backlight through steam, pulsating red laser beam speculars, cold wet skin tones.
Texture: Scratched brass patina with visible grease seals, wool fiber dampness on coats, brick mortar erosion.
Negative constraints: No jittery blur, no face morphology shifts, no plastic skin sheen, no audio desync.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_3",
                segmentIndex = 3,
                totalSegments = 4,
                timeRange = "0:20–0:30",
                startSeconds = 20,
                endSeconds = 30,
                referenceTimestamp = "0:25",
                visualSummary = "Ambush on the catwalks! Silenced magnetic muzzle bursts illuminate the darkness. Raven slides beneath steel railing while firing suppressing rounds.",
                audioTranscript = "\"Extraction shuttle ETA is twenty seconds. Hold the stairwell!\" - Muzzle cracks, shattered glass falling.",
                audioAtmosphere = "Subsonic shockwaves, ricocheting tungsten darts, blaring perimeter alarm klaxon.",
                cameraPhysics = "Dynamic whip-pan into tracking dolly alongside sliding subject, accelerating with muzzle flashes.",
                lightingTexture = "Stroboscopic cyan and white muzzle flares slicing through rain sheets, deep shadows preserved.",
                prevSegmentSummary = "Scene 2: Targeting laser sweep & warning",
                nextSegmentSummary = "Scene 4: Rooftop grapple extraction finale",
                videoPrompt = """```prompt
HEADER:
Format: Action Cinematic Sequence | Ratio: 16:9 | Duration: 10s | Atmosphere: Intense Cybernetic Firefight | Audio: Suppressed hypersonic rounds, shattered glass, klaxon | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven Kane)

TIMELINE:
0:20–0:23: Sudden whip-pan right as security drone descends; Raven ([ref_image]) executes knee-slide across wet metal grate, coat billowing behind.
0:23–0:26: Over-the-shoulder slow-motion (48fps feel); Raven draws micro-rail pistol, firing three precise bursts; blue ionized smoke trails bloom from barrel.
0:26–0:30: Background reinforced glass partition shatters into thousands of shimmering crystalline cubes; Thorne provides covering fire with heavy kinetic revolver.

STYLE BOOSTERS:
Camera physics: Shutter angle 90 degrees for crisp action motion blur, intense cinematic kinetic energy, zero stabilization loss.
Lighting: Intermittent muzzle flash illumination acting as primary key light, neon blue rim highlights on flying glass fragments.
Texture: Holographic UI glare, wet metal floor spray, heat shimmer above weapon ventilation ports.
Negative constraints: No exaggerated anime effects, no muddy dark clipping, preserve exact character facial structure.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_4",
                segmentIndex = 4,
                totalSegments = 4,
                timeRange = "0:30–0:34",
                startSeconds = 30,
                endSeconds = 34,
                referenceTimestamp = "0:32",
                visualSummary = "Rooftop extraction climax. Raven fires magnetic grapple cable skyward into thunderous clouds as extraction ship spotlights cut through the storm.",
                audioTranscript = "\"I never retreat, Aris. See you on the rooftop.\" - Grapple launch thump, ascending jet turbine roar.",
                audioAtmosphere = "Deafening VTOL jet engines, wind shear blowing rain horizontally, triumphant synth swell.",
                cameraPhysics = "Dramatic vertigo push-in / tilt-up looking into the sky as subject launches upward.",
                lightingTexture = "Twin 10,000-lumen titanium white searchlights piercing through torrential rain down onto subject.",
                prevSegmentSummary = "Scene 3: Alley gun battle & glass shatter",
                nextSegmentSummary = "End of Sequence (Fade to Title Card)",
                videoPrompt = """```prompt
HEADER:
Format: Climactic Film Finale | Ratio: 16:9 | Duration: 4s | Atmosphere: Epic Storm Extraction | Audio: VTOL turbine roar, grapple cord tension whine | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven Kane)

TIMELINE:
0:30–0:32: Low angle medium shot; Raven ([ref_image]) holsters secondary sidearm, aims grapple launcher at upper cornice, firing high-tensile wire.
0:32–0:34: Wire goes taut with metallic resonance; camera booms upward in synchrony as she ascends into blinding white searchlight beams of hovering extraction shuttle.

STYLE BOOSTERS:
Camera physics: 24mm ultra-wide, vertical boom tracking with smooth velocity curve, organic anamorphic optical glare.
Lighting: Volumetric dual searchlight cones illuminating sheets of rain, stark rim silhouette turning into lens flare bloom.
Texture: Wind-whipped wet hair, high-speed cable vibration, condensation droplets on camera lens.
Negative constraints: No warping of proportions, no loss of character identity, no unnatural gravity transitions.
```""".trimIndent()
            )
        )
    }

    private fun generate15sSegments(project: VideoProject): List<StoryboardCard> {
        return listOf(
            StoryboardCard(
                id = "sb_15_1",
                segmentIndex = 1,
                totalSegments = 3,
                timeRange = "0:00–0:15",
                startSeconds = 0,
                endSeconds = 15,
                referenceTimestamp = "0:07",
                visualSummary = "Alleyway breach and initial infiltration. Raven Kane drops into the rain-slicked sub-level catwalks, joined by Dr. Thorne as perimeter security alarms activate.",
                audioTranscript = "\"Sub-level 4 breach confirmed... Don't touch the perimeter coils.\"",
                audioAtmosphere = "Continuous rainstorm, industrial transformer drone, mechanical arm calibrations.",
                cameraPhysics = "Descending crane shot into moving two-shot steadicam tracking alongside both operatives.",
                lightingTexture = "Neon cyan reflection on wet asphalt (#00F2FE), warm sodium streetlamps flickering.",
                prevSegmentSummary = "Opening Segment (Identity Locked)",
                nextSegmentSummary = "Scene 2: Firefight confrontation",
                videoPrompt = """```prompt
HEADER:
Format: Cinematic Infiltration Sequence | Ratio: 16:9 | Duration: 15s | Atmosphere: Cyber-Noir Rain Infiltration | Audio: Urban rain, comms chatter, mechanical whirs | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven & Thorne)

TIMELINE:
0:00–0:05: High-angle descent tracking Raven Kane ([ref_image]) leaping from fire escape onto rain-soaked iron grating.
0:05–0:10: Raven enters defensive crouch; Dr. Thorne ([ref_image]) steps out of steam curtain, brass prosthetic rotating into active cycle.
0:10–0:15: Both characters survey perimeter as biometric laser grid triggers along wet brick wall; mutual nod before drawing weapons.

STYLE BOOSTERS:
Camera physics: Panavision Anamorphic T1.5 prime lens, fluid tracking motion, subtle organic camera shake on impact.
Lighting: Wet pavement specular highlights, 5600K cyan key light, 3200K rim light on character silhouettes.
Texture: Matte carbon weave on tactical coat, water droplets running down cheeks, brass machinery patina.
Negative constraints: No cartoon shading, no blurred faces, no identity morphing between cuts.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_15_2",
                segmentIndex = 2,
                totalSegments = 3,
                timeRange = "0:15–0:30",
                startSeconds = 15,
                endSeconds = 30,
                referenceTimestamp = "0:22",
                visualSummary = "Catwalk ambush and synchronized tactical defense. Operatives return fire under stroboscopic muzzle flash illumination.",
                audioTranscript = "\"Too late. We have company on the catwalks... Extraction shuttle ETA is twenty seconds!\"",
                audioAtmosphere = "Hypersonic muzzle reports, shattering ballistic glass, emergency sirens.",
                cameraPhysics = "360-degree orbital combat tracking, shifting from over-shoulder Thorne to sliding low-angle Raven.",
                lightingTexture = "High-contrast dynamic muzzle flashes casting sharp instantaneous shadows against steam.",
                prevSegmentSummary = "Scene 1: Infiltration & grid alarm",
                nextSegmentSummary = "Scene 3: Rooftop grapple extraction",
                videoPrompt = """```prompt
HEADER:
Format: Tactical Action Sequence | Ratio: 16:9 | Duration: 15s | Atmosphere: Close-Quarters Alley Gunfight | Audio: Muzzle cracks, siren echo, shattered glass | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven & Thorne)

TIMELINE:
0:15–0:20: Security drones sweep alley with blinding spotlights; Raven ([ref_image]) slides under heavy railing firing suppressive kinetic rounds.
0:20–0:25: Dr. Thorne ([ref_image]) deflects debris with brass prosthetic arm; returns fire with heavy kinetic revolver; glass wall shatters behind him.
0:25–0:30: Both operatives push back-to-back toward rooftop stairwell as laser fire riddles the catwalk around their boots.

STYLE BOOSTERS:
Camera physics: 90-degree fast shutter, smooth orbital steadicam, macro depth of field on ejected copper shell casings.
Lighting: Strobe lightning and muzzle flash flashes illuminating rain sheets, cold cyan ambient fill.
Texture: Flying glass dust particles, steam billows, carbon scoring on masonry.
Negative constraints: No chaotic jump cuts, keep characters strictly consistent with design sheets.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_15_3",
                segmentIndex = 3,
                totalSegments = 3,
                timeRange = "0:30–0:34",
                startSeconds = 30,
                endSeconds = 34,
                referenceTimestamp = "0:32",
                visualSummary = "Climactic cable ascent into extraction craft searchlights above the storm clouds.",
                audioTranscript = "\"I never retreat, Aris. See you on the rooftop.\"",
                audioAtmosphere = "Thunderclap, grapple cord whir, twin VTOL turbines screaming.",
                cameraPhysics = "Rapid tilt-up from ground level following the grapple cable upward into aerial master view.",
                lightingTexture = "Blinding white aircraft searchlights piercing the rainstorm.",
                prevSegmentSummary = "Scene 2: Alley firefight",
                nextSegmentSummary = "End of Sequence",
                videoPrompt = """```prompt
HEADER:
Format: Climax Extraction | Ratio: 16:9 | Duration: 4s | Atmosphere: Stormy Aerial Extraction | Audio: Jet turbine, grapple wire snap | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven Kane)

TIMELINE:
0:30–0:32: Raven Kane ([ref_image]) locks grapple wire into heavy rooftop stanchion, triggering magnetic ascent winch.
0:32–0:34: Raven shoots upward through falling rain toward hovering gunship searchlights; dramatic silhouette against cloudy lightning.

STYLE BOOSTERS:
Camera physics: 28mm wide anamorphic, smooth upward crane acceleration, cinematic lens flare streaking horizontally.
Lighting: Volumetric twin spotlight cones illuminating horizontal rain streaks.
Texture: Glistening wet hair, taut vibrating steel cable, rain droplet lens flare bokeh.
Negative constraints: No clipping or distortion of anatomy during rapid movement.
```""".trimIndent()
            )
        )
    }

    private fun generate30sSegments(project: VideoProject): List<StoryboardCard> {
        return listOf(
            StoryboardCard(
                id = "sb_30_1",
                segmentIndex = 1,
                totalSegments = 2,
                timeRange = "0:00–0:30",
                startSeconds = 0,
                endSeconds = 30,
                referenceTimestamp = "0:15",
                visualSummary = "Complete sequence: Infiltration, operative rendezvous, alarm breach, and intense tactical alley firefight.",
                audioTranscript = "Full dialogue exchange between Raven Kane and Dr. Thorne with intensifying industrial synth score.",
                audioAtmosphere = "Continuous dystopian sound design: rain, steam, transformer buzz, alarms, gunfire.",
                cameraPhysics = "Seamless one-take simulation: descending crane transitions into tracking steadicam, culminating in dynamic action orbital motion.",
                lightingTexture = "Layered multi-point cyber-noir lighting: neon cyan key, sodium amber fill, stroboscopic weapon flashes.",
                prevSegmentSummary = "Master Video Opening",
                nextSegmentSummary = "Scene 2: Rooftop Climax",
                videoPrompt = """```prompt
HEADER:
Format: Master Long-Take Cinematic | Ratio: 16:9 | Duration: 30s | Atmosphere: Cyber-Noir Tactical Infiltration | Audio: Urban rainstorm, synth score, combat Foley | Identity Lock Clause: [ref_image] = locked master stills from Phase 1 (Raven & Thorne)

TIMELINE:
0:00–0:08: High crane shot tracking Raven Kane ([ref_image]) leaping from high fire escape, landing in tactical crouch on steaming catwalk.
0:08–0:16: Dr. Thorne ([ref_image]) emerges from steam; brass arm servos cycle; dialogue exchange as red security lasers sweep alley.
0:16–0:24: Ambush commences; Raven slides firing rail pistol; Thorne unleashes covering fire; glass partitions shatter in slow motion.
0:24–0:30: Back-to-back defense on metal stairwell as extraction ship spotlights begin sweeping down through the rain.

STYLE BOOSTERS:
Camera physics: Continuous 35mm Master Prime, seamless spatial continuity, realistic physical inertia and camera operator movement.
Lighting: Dynamic moving spotlights intersecting with fixed neon signage; high contrast ratio (8:1).
Texture: Authentic wet fabric draping, reflective puddles with ripples, muzzle smoke turbulence.
Negative constraints: No camera warping, no temporal flickering, keep character likenesses 100% frozen to Phase 1 sheets.
```""".trimIndent()
            ),
            StoryboardCard(
                id = "sb_30_2",
                segmentIndex = 2,
                totalSegments = 2,
                timeRange = "0:30–0:34",
                startSeconds = 30,
                endSeconds = 34,
                referenceTimestamp = "0:32",
                visualSummary = "Climactic cable ascent into the hovering extraction gunship.",
                audioTranscript = "\"See you on the rooftop!\" - Turbine roar and thunderclap.",
                audioAtmosphere = "VTOL aircraft roar, thunderous low-end frequency rumble.",
                cameraPhysics = "Vertical upward crane tracking subject toward the sky.",
                lightingTexture = "Massive volumetric spotlight beam.",
                prevSegmentSummary = "Scene 1: Master alleyway confrontation",
                nextSegmentSummary = "End of Sequence",
                videoPrompt = """```prompt
HEADER:
Format: Climactic Film Finale | Ratio: 16:9 | Duration: 4s | Atmosphere: Aerial Extraction | Audio: Jet turbine roar, cable whine | Identity Lock Clause: [ref_image] = locked master still from Phase 1 (Raven Kane)

TIMELINE:
0:30–0:34: Raven Kane ([ref_image]) launches grapple hook; vertical camera moves smoothly as she ascends into blinding searchlight beam of extraction vessel.

STYLE BOOSTERS:
Camera physics: Anamorphic wide prime, smooth vertical tracking, organic anamorphic horizontal streak flare.
Lighting: Ultra-bright 10,000 lumen spotlight cutting through rain.
Texture: Shimmering rain streaks, high-tension steel cable vibrations.
Negative constraints: No anatomy distortion or motion blur artifacts.
```""".trimIndent()
            )
        )
    }

    fun getSciFiProject(): VideoProject {
        val characters = listOf(
            CharacterCard(
                id = "char_elena",
                name = "Cmdr. Elena Vance",
                role = "Deep Space Expedition Lead",
                appearanceSummary = "Athletic astronaut, 34, sharp intelligent gaze, braided auburn hair tucked behind ear. Pressurized titanium-composite deep-space EVA suit with copper heating filaments.",
                designSheetPrompt = """[ref_image] = master turnaround sheet
CHARACTER: Commander Elena Vance, female 34, NASA deep-space veteran.
VIEWS: Orthographic front, 3/4 left, back view, close-up helmet visor portrait with HUD reflection.
ATTIRE: Matte white and slate gray EVA exploration suit, copper thermal conduits, magnetic chest harness, wrist telemetry terminal.
FACIAL DETAILS: Intense amber-hazel eyes, light freckles over bridge of nose, calm analytical expression under extreme pressure.
LIGHTING: Hard cosmic solar backlight, subtle cyan cockpit bounce light, 8k Unreal Engine photorealism.""".trimIndent(),
                isApproved = false,
                refTimestamp = "0:06",
                avatarColorHex = 0xFF38BDF8
            ),
            CharacterCard(
                id = "char_echo9",
                name = "Echo-9",
                role = "Synthesized Android Pilot",
                appearanceSummary = "Synthetic android with porcelain-finish white facial plating, translucent synthetic skin along jawline exposing glowing neural fiberoptics, glowing amber ocular sensors.",
                designSheetPrompt = """[ref_image] = master turnaround sheet
CHARACTER: Echo-9, humanoid synthetic pilot.
VIEWS: Front orthographic, side profile, rear view, macro eye sensor detail.
ATTIRE: Seamless carbon-weave flight jumpsuit, internal gyroscopic chest core glowing pale amber.
FACIAL DETAILS: Symmetrical porcelain features, fine micro-seams along temples, warm amber optical lenses with depth sensor rings.
LIGHTING: Stark fluorescent cockpit illumination, rim lighting from planetary rings, photorealistic 8k studio locked master still.""".trimIndent(),
                isApproved = false,
                refTimestamp = "0:14",
                avatarColorHex = 0xFFF59E0B
            )
        )

        val transcript = listOf(
            TranscriptLine(
                id = "tr_sf_1",
                timestamp = "0:01 - 0:08",
                speaker = "Cmdr. Elena Vance",
                speechText = "Station telemetry is offline. Echo, scan the docking ring for atmospheric leaks.",
                audioFx = "Heavy pressurized breathing in helmet mic, low hull vibration hum, thruster gas hiss",
                backgroundMusic = "Ethereal space ambient synthesizer with cold bell-like harmonic tones"
            ),
            TranscriptLine(
                id = "tr_sf_2",
                timestamp = "0:09 - 0:18",
                speaker = "Echo-9",
                speechText = "Sensors indicate residual argon pressure in sector 7. Warning: biological thermal signature detected.",
                audioFx = "Synthesized melodic vocal modulation, computer chime, mechanical joystick actuation",
                backgroundMusic = "Pulsing sub-bass thrum building ominous harmonic tension"
            ),
            TranscriptLine(
                id = "tr_sf_3",
                timestamp = "0:19 - 0:28",
                speaker = "Cmdr. Elena Vance",
                speechText = "Biological? The crew evacuated three months ago. Prepare the secondary airlock.",
                audioFx = "Heavy magnetic boot clunks on airlock steel, alarm sequence beeps, hydraulic seal clamp",
                backgroundMusic = "Dramatic string dissonance swelling with suspense"
            ),
            TranscriptLine(
                id = "tr_sf_4",
                timestamp = "0:29 - 0:45",
                speaker = "Echo-9",
                speechText = "Airlock engaged, Commander. Whatever is inside... it knows we have arrived.",
                audioFx = "Massive exterior bulkhead groaning, airlock equalization blast, distant metallic thump",
                backgroundMusic = "Full cosmic orchestral crescendo resolving into chilling silence"
            )
        )

        return VideoProject(
            id = "proj_scifi",
            title = "Orbital Horizon: Deep Space",
            sourceName = "deep_space_station.mp4",
            metadata = VideoMetadata(
                durationSeconds = 45,
                formattedDuration = "0:45",
                resolution = "3840x2160 (2.39:1 Anamorphic)",
                frameRate = "23.976 FPS",
                audioSpecs = "Spatial Audio 7.1.4 / 96kHz 24-bit",
                fileSizeFormatted = "62.4 MB"
            ),
            selectedSegmentDuration = 15,
            characters = characters,
            storyboardCards = emptyList(),
            transcript = transcript,
            promptEngine = TargetPromptEngine.SORA
        )
    }
}
