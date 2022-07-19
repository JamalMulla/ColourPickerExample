package com.jamal.colourpickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.jamal.colourpickerexample.ui.theme.ColourPickerExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colors = listOf(
            Color(0xFFEF9A9A),
            Color(0xFFF48FB1),
            Color(0xFF80CBC4),
            Color(0xFFA5D6A7),
            Color(0xFFFFCC80),
            Color(0xFFFFAB91),
            Color(0xFF81D4FA),
            Color(0xFFCE93D8),
            Color(0xFFB39DDB)
        )

        setContent {
            ColourPickerExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ColourButton(colors, onColorSelected = {})
                    }
                }
            }
        }
    }
}

@Composable
fun ColourButton(colors: List<Color>, onColorSelected: (Color) -> Unit) {
    var colorPickerOpen by rememberSaveable { mutableStateOf(false) }
    var currentlySelected by rememberSaveable(saver = colourSaver()) { mutableStateOf(colors[0]) }

    Box(
        modifier = Modifier
            .padding(top = 16.dp, start = 5.dp, end = 5.dp, bottom = 5.dp)
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(20))
            .border(
                2.dp,
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                RoundedCornerShape(20)
            )
            .clickable {
                colorPickerOpen = true
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select colour",
            )

            Canvas(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(20))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                        RoundedCornerShape(20)
                    )
                    .background(currentlySelected)
                    .clickable {
                        colorPickerOpen = true
                    }
            ) {}
        }

    }



    if (colorPickerOpen) {
        TwoColorDialog(
            colorList = colors,
            onDismiss = { colorPickerOpen = false },
            currentlySelected = currentlySelected,
            onColorSelected = {
                currentlySelected = it
                onColorSelected(it)
            }
        )
    }
}


@Composable
private fun ColorDialog(
    colorList: List<Color>,
    onDismiss: (() -> Unit),
    currentlySelected: Color,
    onColorSelected: ((Color) -> Unit) // when the save button is clicked
) {
    val gridState = rememberLazyGridState()

    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.outline,
        onDismissRequest = onDismiss,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState
            ) {
                items(colorList) { color ->
                    var borderWidth = 0.dp
                    if (currentlySelected == color) {
                        borderWidth = 2.dp
                    }

                    Canvas(modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            borderWidth,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            RoundedCornerShape(20.dp)
                        )
                        .background(color)
                        .requiredSize(70.dp)
                        .clickable {
                            onColorSelected(color)
                            onDismiss()
                        }
                    ) {
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun TwoColorDialog(
    colorList: List<Color>,
    onDismiss: (() -> Unit),
    currentlySelected: Color,
    onColorSelected: ((Color) -> Unit)
) {
    val gridState = rememberLazyGridState()

    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.outline,
        onDismissRequest = onDismiss,
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState
            ) {
                items(colorList) { color ->
                    var borderWidth = 0.dp
                    if (currentlySelected == color) {
                        borderWidth = 2.dp
                    }

                    Canvas(modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            borderWidth,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                            RoundedCornerShape(20.dp)
                        )
//                        .background(color)
                        .requiredSize(70.dp)
                        .clickable {
                            onColorSelected(color)
                            onDismiss()
                        }
                    ) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        drawPath(Path().apply {
                            moveTo(0f, 0f)
                            lineTo(canvasWidth, 0f)
                            lineTo(0f, canvasHeight)
                            close()
                        }, color = color)

                        drawPath(Path().apply {
                            moveTo(canvasWidth, 0f)
                            lineTo(0f, canvasHeight)
                            lineTo(canvasWidth, canvasHeight)
                            close()
                        }, color = color.copy(alpha = 0.6f))

                    }
                }
            }
        },
        confirmButton = {}
    )
}

fun colourSaver() = Saver<MutableState<Color>, String>(
    save = { state -> state.value.toHexString() },
    restore = { value -> mutableStateOf(value.toColor()) }
)

fun Color.toHexString(): String {
    return String.format(
        "#%02x%02x%02x%02x", (this.alpha * 255).toInt(),
        (this.red * 255).toInt(), (this.green * 255).toInt(), (this.blue * 255).toInt()
    )
}

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}