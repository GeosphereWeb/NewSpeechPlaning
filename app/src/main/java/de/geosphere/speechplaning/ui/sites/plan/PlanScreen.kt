package de.geosphere.speechplaning.ui.sites.plan

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.geosphere.speechplaning.mockup.MockedListOfDummyClasses
import de.geosphere.speechplaning.viewModels.SpeakerViewModel
import de.geosphere.speechplaning.viewModels.congregation.CongregationViewModel
import de.geosphere.speechplaning.viewModels.speeches.SpeechViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

private const val DURARION = 500

@Composable
fun PlanScreen(
    modifier: Modifier = Modifier,
    vmSpeech: SpeechViewModel = koinViewModel<SpeechViewModel>(),
    vmSpeaker: SpeakerViewModel = koinViewModel<SpeakerViewModel>(),
    vmCongregation: CongregationViewModel = koinViewModel<CongregationViewModel>()
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = PlanScreenRoute.PlanDetail,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(DURARION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(DURARION)
            )
        }
    ) {
        composable<PlanScreenRoute.PlanDetail> {
            Text("Plan Detail")
            Button(onClick = {
                // MockedListOfDummyClasses.speakersMockupList.forEach {
                //     vmSpeaker.saveSpeaker(it)
                // }
                // MockedListOfDummyClasses.speechesMockupList.forEach {
                //     vmSpeech.saveSpeech(it)
                // }
                MockedListOfDummyClasses.congregationMockupList.forEach {
                    vmCongregation.saveCongregation(it)
                }
            }) { Text("make mockups") }
        }
    }
}

internal sealed interface PlanScreenRoute {
    @Serializable
    data object PlanDetail : PlanScreenRoute

    @Serializable
    data object PlanList : PlanScreenRoute
}
