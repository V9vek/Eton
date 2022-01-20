package com.vivek.eton.feature_note.presentation.screens.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vivek.eton.feature_note.domain.model.Note
import com.vivek.eton.feature_note.presentation.screens.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    noteColor: Int,
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            initialValue = Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(event = AddEditNoteEvent.SaveNote)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = noteBackgroundAnimatable.value)
                .padding(16.dp),
        ) {

            // Row of colors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { noteColor ->
                    val colorInt = noteColor.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(elevation = 15.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(color = noteColor)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = noteColor,
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(event = AddEditNoteEvent.ChangeColor(color = colorInt))
                            },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text Fields
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.EnteredTitle(value = it))
                },
                onFocusChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.ChangeTitleFocus(focusState = it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.EnteredContent(value = it))
                },
                onFocusChange = {
                    viewModel.onEvent(event = AddEditNoteEvent.ChangeContentFocus(focusState = it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

























