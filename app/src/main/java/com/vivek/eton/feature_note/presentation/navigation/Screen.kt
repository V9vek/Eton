package com.vivek.eton.feature_note.presentation.navigation

sealed class Screen(val route: String) {
    object NotesScreen: Screen(route = "notes_screen")
    object AddEditNoteScreen: Screen(route = "add_edit_note_screen")
}
