package com.vivek.eton.feature_note.presentation.screens.notes

import com.vivek.eton.feature_note.domain.model.Note
import com.vivek.eton.feature_note.domain.util.NoteOrder
import com.vivek.eton.feature_note.domain.util.OrderType

// UI state: which things to display in notes screen

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)


