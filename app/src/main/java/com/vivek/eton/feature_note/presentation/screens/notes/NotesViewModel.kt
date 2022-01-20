package com.vivek.eton.feature_note.presentation.screens.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivek.eton.feature_note.domain.model.Note
import com.vivek.eton.feature_note.domain.use_case.NoteUseCases
import com.vivek.eton.feature_note.domain.util.NoteOrder
import com.vivek.eton.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(noteOrder = NoteOrder.Date(orderType = OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                // check if note order has changed like if clicked same order multiple times
                // NoteOrder.Type (in state) == NoteOrder.Type(in event)

                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }

                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote.invoke(note = event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is NotesEvent.RestoreDeletedNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote.invoke(note = recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        // everytime getNotes() is called, a new instance of flow will be returned,
        // so the previous flow/coroutines that is observing our DATABASE, should be cancelled to save resources
        getNotesJob?.cancel()

        getNotesJob = noteUseCases.getNotes.invoke(noteOrder = noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }
}






















