package de.geosphere.speechplaning.ui.atoms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.di.PreviewKoin
import de.geosphere.speechplaning.mockup.MockedListOfDummyClasses
import de.geosphere.speechplaning.ui.theme.SpeechPlaningTheme
import de.geosphere.speechplaning.ui.theme.ThemePreviews

@Suppress("MagicNumber", "LongMethod")
@Composable
fun SpeakerListItemComposable(
    speakers: Speaker,
    modifier: Modifier,
    orderName: OrderName = OrderName.FIRSTNAME_LASTNAME,
    moreIsRequested: (Speaker) -> Unit,
    expandCard: Boolean = false
) {
    var showItem by remember { mutableStateOf(expandCard) }
    ListItem(
        modifier = Modifier
            .selectable(
                selected = true,
                onClick = { showItem = !showItem },
            )
            .then(modifier),
        tonalElevation = 3.dp,
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = when (orderName) {
                        OrderName.FIRSTNAME_LASTNAME -> "${speakers.nameFirst} ${speakers.nameLast}"
                        OrderName.LASTNAME_FIRSTNAME -> "${speakers.nameLast} ${speakers.nameFirst}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable(onClick = { moreIsRequested(speakers) }),
                )
            }
        },
        supportingContent = {
            AnimatedVisibility(
                visible = showItem,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300)),
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Smartphone,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = speakers.mobile, color = MaterialTheme.colorScheme.secondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = speakers.phone)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = speakers.email)
                    }
                }
            }
        },
        leadingContent = {
            Box(modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 300))) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(AvatarProvider.getAvatar(speakers.spiritualStatus)),
                    contentDescription = null,
                )
            }
        },
    )
}

enum class OrderName {
    FIRSTNAME_LASTNAME,
    LASTNAME_FIRSTNAME
}

@ThemePreviews
@Composable
private fun SpeakerListItemPreview() = PreviewKoin {
    SpeechPlaningTheme {
        Column {
            SpeakerListItemComposable(
                speakers = MockedListOfDummyClasses.speakersMockupList[0],
                modifier = Modifier,
                orderName = OrderName.FIRSTNAME_LASTNAME,
                moreIsRequested = {},
                expandCard = false
            )
            HorizontalDivider()
            SpeakerListItemComposable(
                speakers = MockedListOfDummyClasses.speakersMockupList[1],
                modifier = Modifier,
                orderName = OrderName.LASTNAME_FIRSTNAME,
                expandCard = true,
                moreIsRequested = {})
        }
    }
}
