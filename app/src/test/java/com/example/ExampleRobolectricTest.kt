package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RECON-AI", appName)
  }

  @Test
  fun `verify sample project and storyboards generation`() {
    val project = com.example.data.SampleProjects.getCyberpunkProject()
    assertEquals("Cyberpunk: Neon Retribution", project.title)
    assertEquals(2, project.characters.size)
    assertEquals(5, project.transcript.size)

    val storyboards = com.example.data.SampleProjects.generateStoryboardsForDuration(project, 10)
    assertEquals(4, storyboards.size)
    assertEquals("0:00–0:10", storyboards[0].timeRange)
  }

  @Test
  fun `verify video dialogue and prompt audio parity`() {
    val project = com.example.data.SampleProjects.getCyberpunkProject()
    val storyboards = com.example.data.SampleProjects.generateStoryboardsForDuration(project, 10)

    // Scene 1: Raven Kane dialogue parity
    val sb1 = storyboards[0]
    org.junit.Assert.assertTrue(sb1.audioTranscript.contains("Raven Kane"))
    org.junit.Assert.assertTrue(sb1.videoPrompt.contains("Raven Kane"))
    org.junit.Assert.assertTrue(sb1.videoPrompt.contains("Sub-level 4 breach confirmed. Thorne, the quantum drive is destabilizing."))

    // Scene 2: Dr. Aris Thorne dialogue parity
    val sb2 = storyboards[1]
    org.junit.Assert.assertTrue(sb2.audioTranscript.contains("Dr. Aris Thorne"))
    org.junit.Assert.assertTrue(sb2.videoPrompt.contains("Dr. Aris Thorne"))
    org.junit.Assert.assertTrue(sb2.videoPrompt.contains("Don't touch the perimeter coils. The security grid is hard-coded to biometrics."))

    // Scene 3 & 4: Parity check
    val sb4 = storyboards[3]
    org.junit.Assert.assertTrue(sb4.videoPrompt.contains("I never retreat, Aris. See you on the rooftop."))
  }
}
