package com.hi88.app.role_dice

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.hi88.app.game.R
import com.hi88.app.role_dice.components.DropDownButton
import com.hi88.app.role_dice.components.GifAnimation
import com.hi88.app.settings.data.SettingsRepository
import com.hi88.app.util.CustomButton
import com.hi88.app.util.RollitTopAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.math.min

@Composable
fun RollDiceScreen(
    navController: NavController,
    diceViewModel: DiceViewModel = koinViewModel(),
    settingsRepository: SettingsRepository = koinInject()
) {
    val dice by diceViewModel.dice.collectAsState()
    val isRolling by diceViewModel.isRolling.collectAsState()
    val areAnimationsEnabled by settingsRepository.areAnimationsEnabledFlow.collectAsState(initial = true)
    val isSoundEnabled by settingsRepository.isSoundEnabledFlow.collectAsState(initial = true)

    val context = LocalContext.current

    val mediaPlayer = remember(isRolling) {
        MediaPlayer.create(context, R.raw.dice_rolling_sound)?.apply {
            setOnCompletionListener {
                it.reset()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    val maxDiceInRow = 3
    val dicePerRow = min(dice.size, maxDiceInRow)
    val diceSize = (300.dp / dicePerRow)

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.role_the_dice,
                showNavigationIcons = true,
                showActions = true,
                titleAlignment = TextAlign.Center,
                navController = navController
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .then(Modifier.padding(16.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                mainAxisAlignment = FlowMainAxisAlignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isRolling && areAnimationsEnabled) {
                    repeat(dice.size) {
                        GifAnimation(drawable = R.drawable.rolling_dice, size = diceSize)
                    }
                } else {
                    dice.forEach { value ->
                        GifAnimation(
                            drawable = when (value) {
                                1 -> R.drawable.dice1
                                2 -> R.drawable.dice2
                                3 -> R.drawable.dice3
                                4 -> R.drawable.dice4
                                5 -> R.drawable.dice5
                                6 -> R.drawable.dice6
                                else -> R.drawable.dice_default
                            },
                            size = diceSize
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            DropDownButton(
                options = dice_amounts,
                onOptionSelected = { newCount -> diceViewModel.setDiceCount(newCount) }
            )

            CustomButton(
                text = R.string.throw_str,
                onClick = {
                    if (isSoundEnabled && mediaPlayer != null) {
                        try {
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.stop()
                                mediaPlayer.reset()
                                mediaPlayer.setDataSource(context, android.net.Uri.parse("android.resource://${context.packageName}/${R.raw.dice_rolling_sound}"))
                                mediaPlayer.prepare()
                            }
                            mediaPlayer.start()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    diceViewModel.viewModelScope.launch {
                        diceViewModel.rollDice()
                    }
                }
            )
        }
    }
}
private val dice_amounts = listOf(1, 2, 3, 4, 5, 6)

