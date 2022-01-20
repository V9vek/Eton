package com.vivek.eton.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vivek.eton.ui.theme.*
import java.lang.Exception

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(YellowOrange, RedOrange, Violet, BabyBlue, LightGreen)
    }
}


class InvalidNoteException(message: String) : Exception(message)





















