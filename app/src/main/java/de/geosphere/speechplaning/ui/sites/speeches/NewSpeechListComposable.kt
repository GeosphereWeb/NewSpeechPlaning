package de.geosphere.speechplaning.ui.sites.speeches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.di.PreviewKoin
import de.geosphere.speechplaning.ui.atoms.SpeechListItemComposable
import de.geosphere.speechplaning.ui.theme.SpeechPlaningTheme
import de.geosphere.speechplaning.ui.theme.ThemePreviews
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NewSpeechListComposable(
    modifier: Modifier = Modifier,
    speechList: StateFlow<List<Speech>>,
    editMode: Boolean = false,
    onCheckedChange: ((checked: Boolean, speech: Speech) -> Boolean),
) {
    val speechItems = speechList.collectAsState()
    val lazyListState = rememberLazyListState()


    LazyColumn(
        modifier = modifier.padding(WindowInsets.systemBars.asPaddingValues()),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            speechItems.value.size,
        ) {
            val aktSpeech = remember { speechItems.value[it] }
            val checked = remember { mutableStateOf(true) }

            SpeechListItemComposable(
                modifier = Modifier.fillMaxWidth(),
                speech = aktSpeech,
                trailingContent = {
                    if (editMode) {
                        Switch(
                            enabled = true,
                            checked = checked.value,
                            onCheckedChange = { isChecked ->
                                checked.value = onCheckedChange(isChecked, aktSpeech)
                            }
                        )
                    }
                }
            )
        }
    }
}

// @ThemePreviews
// @Composable
// private fun NewSpeechListComposablePreview() = PreviewKoin {
//     SpeechPlaningTheme {
//         val vmMocked = SpeechViewModelMocked()
//         val speechListStateMocked = vmMocked.speeches
//         NewSpeechListComposable(
//             speechList = speechListStateMocked,
//             editMode = false,
//             onCheckedChange = { _, speech ->
//                 return@NewSpeechListComposable false
//             }
//         )
//     }
// }
