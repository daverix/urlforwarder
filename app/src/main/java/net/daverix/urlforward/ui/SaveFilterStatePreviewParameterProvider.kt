package net.daverix.urlforward.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import net.daverix.urlforward.EditingState
import net.daverix.urlforward.SaveFilterState
import net.daverix.urlforward.createInitialAddFilter

class SaveFilterStatePreviewParameterProvider : PreviewParameterProvider<SaveFilterState> {
    override val values: Sequence<SaveFilterState> = sequenceOf(
        SaveFilterState.Editing(
            createInitialAddFilter(name = "Editing"),
            editingState = EditingState.EDITING
        ),
        SaveFilterState.Editing(
            createInitialAddFilter(name = "Saving"),
            editingState = EditingState.SAVING
        ),
        SaveFilterState.Editing(
            createInitialAddFilter(name = "Saved"),
            editingState = EditingState.SAVED
        ),
        SaveFilterState.Editing(
            createInitialAddFilter(name = "Deleting"),
            editingState = EditingState.DELETING
        ),
        SaveFilterState.Editing(
            createInitialAddFilter(name = "Deleted"),
            editingState = EditingState.DELETED
        ),
        SaveFilterState.Loading
    )
}
