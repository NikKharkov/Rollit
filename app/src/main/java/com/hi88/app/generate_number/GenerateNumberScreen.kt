package com.hi88.app.generate_number

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hi88.app.generate_number.components.ChooseRangeRow
import com.hi88.app.generate_number.components.NumberOfNumbersRow
import com.hi88.app.util.CustomButton
import com.hi88.app.util.CustomRowWithCheckbox
import com.hi88.app.util.RollitTopAppBar
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.hi88.app.game.R
import com.hi88.app.settings.data.SettingsRepository
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun GenerateNumberScreen(
    navController: NavController,
    generateNumberViewModel: GenerateNumberViewModel = koinViewModel(),
    settingsRepository: SettingsRepository = koinInject()
) {
    val numberCount by generateNumberViewModel.numberCount.collectAsState()
    val rangeFrom by generateNumberViewModel.rangeFrom.collectAsState()
    val rangeTo by generateNumberViewModel.rangeTo.collectAsState()
    val numbers by generateNumberViewModel.numbers.collectAsState()
    val noRepetitions by generateNumberViewModel.noRepetitions.collectAsState()
    val saveRange by generateNumberViewModel.saveRange.collectAsState()

    val isNumberCountError = numberCount.toIntOrNull()?.let { it < 0 || it > 100 } ?: true
    val isRangeFromError = rangeFrom.toIntOrNull()?.let { it < -1_000_000 || it > 1_000_000 } ?: true
    val isRangeToError = rangeTo.toIntOrNull()?.let { it < -1_000_000 || it > 1_000_000 } ?: true
    val isRangeError = isRangeToError || isRangeFromError

    val isDarkTheme by settingsRepository.isDarkThemeFlow.collectAsState(initial = false)

    Scaffold(
        topBar = {
            RollitTopAppBar(
                title = R.string.number,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                mainAxisAlignment = FlowMainAxisAlignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (numbers.isEmpty()) {
                    Spacer(modifier = Modifier.padding(vertical = 100.dp))
                } else {
                    numbers.forEach { number ->
                        Text(
                            text = "$number ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NumberOfNumbersRow(
                value = numberCount,
                onValueChange = generateNumberViewModel::onNumberCountChange,
                isError = isNumberCountError
            )

            ChooseRangeRow(
                valueFrom = rangeFrom,
                valueTo = rangeTo,
                onValueChangeFrom = generateNumberViewModel::onRangeFromChange,
                onValueChangeTo = generateNumberViewModel::onRangeToChange,
                isError = isRangeError
            )

            CustomRowWithCheckbox(
                text = R.string.no_repetitions,
                checked = noRepetitions,
                onCheckedChange = generateNumberViewModel::onNoRepetitionsChange
            )

            CustomRowWithCheckbox(
                text = R.string.save_range,
                checked = saveRange,
                onCheckedChange = generateNumberViewModel::onSaveRangeChange
            )

            CustomButton(
                text = R.string.save_as_preset,
                onClick = {
                    generateNumberViewModel.saveCurrentRangeAsPreset()
                }
            )

            CustomButton(
                text = R.string.throw_str,
                onClick = {
                    if (!isNumberCountError && !isRangeFromError && !isRangeToError) {
                        generateNumberViewModel.generateNumbers()
                    }
                }
            )
        }
    }
}