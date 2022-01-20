package com.vivek.eton.feature_note.presentation.screens.notes

import com.vivek.eton.feature_note.domain.model.Note
import com.vivek.eton.feature_note.domain.util.NoteOrder

// Event to be done by user

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreDeletedNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
























