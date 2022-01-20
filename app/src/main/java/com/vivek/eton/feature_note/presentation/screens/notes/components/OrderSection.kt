package com.vivek.eton.feature_note.presentation.screens.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vivek.eton.feature_note.domain.util.NoteOrder
import com.vivek.eton.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onSelect = { onOrderChange(NoteOrder.Title(orderType = noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onSelect = { onOrderChange(NoteOrder.Date(orderType = noteOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            DefaultRadioButton(
                text = "Color",
                selected = noteOrder is NoteOrder.Color,
                onSelect = { onOrderChange(NoteOrder.Color(orderType = noteOrder.orderType)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelect = {
                    // keep NoteOrder but change orderType
                    onOrderChange(noteOrder.copy(orderType = OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelect = {
                    // keep NoteOrder but change orderType
                    onOrderChange(noteOrder.copy(orderType = OrderType.Descending))
                }
            )
        }
    }
}
























