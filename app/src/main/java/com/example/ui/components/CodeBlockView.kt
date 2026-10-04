package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ReconCodeBg
import com.example.ui.theme.ReconCodeBorder
import com.example.ui.theme.ReconCodeText
import com.example.ui.theme.ReconCyan
import com.example.ui.theme.ReconMatrixGreen
import com.example.ui.theme.ReconTextMuted

@Composable
fun CodeBlockView(
    codeText: String,
    onCopy: () -> Unit,
    isCopied: Boolean,
    title: String = "MASTER PROMPT",
    modifier: Modifier = Modifier,
    testTagPrefix: String = "code_block"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ReconCodeBg, RoundedCornerShape(12.dp))
            .border(1.dp, ReconCodeBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("${testTagPrefix}_container")
    ) {
        Column {
            // Header Bar inside Code Block
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Terminal dots
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFEF4444), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFF59E0B), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF10B981), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        color = ReconCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                // Copy Button
                ElevatedButton(
                    onClick = onCopy,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("${testTagPrefix}_copy_button"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (isCopied) ReconMatrixGreen else Color(0xFF1E293B),
                        contentColor = if (isCopied) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy code block",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCopied) "✅ Copied!" else "📋 Copy",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Code Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = codeText,
                    color = ReconCodeText,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
