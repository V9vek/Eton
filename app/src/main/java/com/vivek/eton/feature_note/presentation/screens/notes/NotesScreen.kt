package com.vivek.eton.feature_note.presentation.screens.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vivek.eton.feature_note.domain.model.Note
import com.vivek.eton.feature_note.presentation.navigation.Screen
import com.vivek.eton.feature_note.presentation.screens.notes.components.CustomFab
import com.vivek.eton.feature_note.presentation.screens.notes.components.FabItem
import com.vivek.eton.feature_note.presentation.screens.notes.components.FabState.COLLAPSED
import com.vivek.eton.feature_note.presentation.screens.notes.components.FabState.EXPANDED
import com.vivek.eton.feature_note.presentation.screens.notes.components.NoteItem
import com.vivek.eton.feature_note.presentation.screens.notes.components.OrderSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var fabState by remember { mutableStateOf(COLLAPSED) }

    Scaffold(
        floatingActionButton = {
            CustomFab(
                fabState = fabState,
                onFabStateToggle = {
                    fabState = if (fabState == COLLAPSED) EXPANDED else COLLAPSED
                },
                fabItems = Note.noteColors.map { FabItem(color = it) },
                onFabItemClick = { color ->
                    // adding new note
                    navController.navigate(
                        route = Screen.AddEditNoteScreen.route +
                                "?noteId=${-1}&noteColor=${color.toArgb()}"
                    )
                }
            )
        },
        scaffoldState = scaffoldState,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            viewModel.onEvent(event = NotesEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filter Notes List"
                        )
                    }
                }

                // Filter Section
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        noteOrder = state.noteOrder,
                        onOrderChange = { noteOrder ->
                            viewModel.onEvent(event = NotesEvent.Order(noteOrder = noteOrder))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notes List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                ) {
                    items(state.notes) { note ->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // editing existing note
                                    navController.navigate(
                                        route = Screen.AddEditNoteScreen.route +
                                                "?noteId=${note.id}&noteColor=${note.color}"
                                    )
                                },
                            onDeleteClick = {
                                viewModel.onEvent(event = NotesEvent.DeleteNote(note = note))
                                // snackbar
                                scope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(event = NotesEvent.RestoreDeletedNote)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // overlay layer
            AnimatedVisibility(
                visible = fabState == EXPANDED,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(animateFloatAsState(targetValue = 0.92f).value)
                        .background(MaterialTheme.colors.onSecondary)
                        .clickable(
                            enabled = fabState == EXPANDED,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { fabState = COLLAPSED }
                )
            }
        }
    }
}


























