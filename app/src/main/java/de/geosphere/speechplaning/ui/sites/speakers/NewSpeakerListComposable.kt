package de.geosphere.speechplaning.ui.sites.speakers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.di.PreviewKoin
import de.geosphere.speechplaning.domain.SpeakerFilter
import de.geosphere.speechplaning.ui.atoms.OrderName
import de.geosphere.speechplaning.ui.atoms.SpeakerListItemComposable
import de.geosphere.speechplaning.ui.theme.SpeechPlaningTheme
import de.geosphere.speechplaning.ui.theme.ThemePreviews
import de.geosphere.speechplaning.viewModels.SpeakerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewSpeakerListComposable(
    modifier: Modifier = Modifier,
    selectedSpeaker: (Speaker) -> Unit = {},
    speakerViewModel: SpeakerViewModel = koinViewModel()
) {
    // val filteredSpeakerList by speakerViewModel.filteredSpeakers.collectAsState()
    // val scrollState = rememberLazyListState()
    // val groupedSpeakers = groupSpeakersByLastNameInitial(filteredSpeakerList)
    // val selFilter = speakerViewModel.currentFilter.collectAsState()
    //
    // Column(
    //     modifier = Modifier
    //         .fillMaxSize()
    //         .then(modifier)
    // ) {
    //     // Remember the scroll state of the LazyRow
    //     Row(
    //         modifier = Modifier
    //             .fillMaxWidth()
    //             .padding(8.dp),
    //         horizontalArrangement = Arrangement.spacedBy(8.dp)
    //     ) {
    //         FilterChip(
    //             selected = (selFilter.value == SpeakerFilter.ACTIVE),
    //             onClick = {
    //                 when (selFilter.value) {
    //                     SpeakerFilter.ACTIVE -> speakerViewModel.setFilter(SpeakerFilter.INACTIVE)
    //                     SpeakerFilter.INACTIVE -> speakerViewModel.setFilter(SpeakerFilter.ACTIVE)
    //                     SpeakerFilter.ALL -> speakerViewModel.setFilter(SpeakerFilter.INACTIVE)
    //                 }
    //             },
    //             label = { Text(if (selFilter.value == SpeakerFilter.ACTIVE) "aktiv" else "inaktiv") }
    //         )
    //
    //         FilterChip(
    //             selected = (selFilter.value == SpeakerFilter.ALL),
    //             onClick = {
    //                 when (selFilter.value) {
    //                     SpeakerFilter.ACTIVE -> speakerViewModel.setFilter(SpeakerFilter.ALL)
    //                     SpeakerFilter.INACTIVE -> speakerViewModel.setFilter(SpeakerFilter.ALL)
    //                     SpeakerFilter.ALL -> speakerViewModel.setFilter(SpeakerFilter.ACTIVE)
    //                 }
    //             },
    //             label = { Text("all") }
    //         )
    //     }
    //
    //     LazyColumn(
    //         state = scrollState,
    //         verticalArrangement = Arrangement.spacedBy(4.dp),
    //     ) {
    //         groupedSpeakers.forEach { (initial, speakerList) ->
    //             stickyHeader {
    //                 Surface(color = MaterialTheme.colorScheme.secondary) {
    //                     Text(
    //                         text = initial.toString(),
    //                         style = MaterialTheme.typography.titleMedium,
    //                         modifier = Modifier
    //                             .fillMaxWidth()
    //                             .padding(horizontal = 16.dp, vertical = 4.dp)
    //                     )
    //                 }
    //             }
    //             items(speakerList, key = { it.speakerId!! }) { speaker ->
    //                 SpeakerListItemComposable(
    //                     speakers = speaker,
    //                     modifier = Modifier,
    //                     orderName = OrderName.LASTNAME_FIRSTNAME,
    //                     moreIsRequested = { selectedSpeaker(it) }
    //                 )
    //             }
    //         }
    //     }
    // }
}

private fun groupSpeakersByLastNameInitial(speakers: List<Speaker>): Map<Char, List<Speaker>> {
    return speakers.groupBy { it.nameLast.firstOrNull()?.uppercaseChar() ?: '#' }
}

@ThemePreviews
@Composable
private fun NewSpeakerListScreenPreview() = PreviewKoin {
    SpeechPlaningTheme {
        // NewSpeakerListComposable(
        //     selectedSpeaker = {},
        //     speakerViewModel = MockedSpeakerViewModel()
        // )
    }
}
