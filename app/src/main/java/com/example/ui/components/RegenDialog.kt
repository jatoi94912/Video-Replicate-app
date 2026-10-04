package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CharacterCard
import com.example.ui.theme.ReconAmber
import com.example.ui.theme.ReconCardBorder
import com.example.ui.theme.ReconCardDark
import com.example.ui.theme.ReconSurfaceDark
import com.example.ui.theme.ReconTextMuted
import com.example.ui.theme.ReconTextPrimary
import com.example.ui.theme.ReconTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegenDialog(
    character: CharacterCard,
    onDismiss: () -> Unit,
    onSubmit: (issueType: String, customNote: String) -> Unit
) {
    val issueOptions = listOf("Face & Likeness", "Outfit & Attire", "Pose & Perspective", "Lighting / Style", "Other")
    var selectedIssue by remember { mutableStateOf(issueOptions[0]) }
    var customNote by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ReconSurfaceDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ReconCardBorder, RoundedCornerShape(20.dp))
                .testTag("regen_dialog_container")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "🔄 Refine Character Sheet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ReconAmber
                        )
                        Text(
                            text = "Targeted regeneration for: ${character.name}",
                            fontSize = 12.sp,
                            color = ReconTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "What needs adjustment?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ReconTextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Issue Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (option in issueOptions) {
                        val isSelected = selectedIssue == option
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) ReconAmber else ReconCardDark,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) ReconAmber else ReconCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedIssue = option }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("regen_option_${option.replace(" ", "_")}")
                        ) {
                            Text(
                                text = option,
                                color = if (isSelected) Color.Black else ReconTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Specific Correction (Optional):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ReconTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customNote,
                    onValueChange = { customNote = it },
                    placeholder = {
                        Text(
                            "e.g. Make cybernetic arm brass instead of chrome, add dark trench coat hood...",
                            color = ReconTextMuted,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("regen_custom_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ReconTextPrimary,
                        unfocusedTextColor = ReconTextPrimary,
                        focusedBorderColor = ReconAmber,
                        unfocusedBorderColor = ReconCardBorder,
                        focusedContainerColor = ReconCardDark,
                        unfocusedContainerColor = ReconCardDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ReconTextSecondary),
                        modifier = Modifier.testTag("regen_cancel_button")
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { onSubmit(selectedIssue, customNote) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ReconAmber,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.testTag("regen_submit_button")
                    ) {
                        Text("🔄 Regenerate Sheet", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
