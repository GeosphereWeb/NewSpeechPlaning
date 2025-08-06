package de.geosphere.speechplaning.ui.atoms

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.di.PreviewKoin
import de.geosphere.speechplaning.mockup.MockedListOfDummyClasses
import de.geosphere.speechplaning.ui.theme.SpeechPlaningTheme
import de.geosphere.speechplaning.ui.theme.ThemePreviews

@Suppress("MagicNumber")
@Composable
fun SpeechListItemComposable(
    speech: Speech,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit) = {},
) {
    ListItem(
        modifier = modifier,
        shadowElevation = 1.dp,
        leadingContent = {
            Text(
                text = speech.number.toString(),
                maxLines = 1,
                textAlign = TextAlign.Right,
                modifier = Modifier.width(30.dp)
            )
        },
        headlineContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = speech.subject,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingContent = trailingContent
    )
    HorizontalDivider()
}

@Suppress("MagicNumber")
@ThemePreviews
@Composable
private fun SpeechComposablePreview() = PreviewKoin {
    SpeechPlaningTheme {
        Column {
            SpeechListItemComposable(MockedListOfDummyClasses.speechesMockupList[101])
        }
    }
}
