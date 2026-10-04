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
}
