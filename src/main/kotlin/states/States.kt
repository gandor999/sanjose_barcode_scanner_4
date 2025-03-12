package states

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.focus.FocusRequester
import ui.Item

data class MutableStates(
    val itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    val requester: FocusRequester,
    val currentPage: MutableState<Pages>,
    val stringBuilder: MutableState<StringBuilder /* = StringBuilder */>,
    val showDatabaseInsertionPage: MutableState<Boolean>
)

// do we really need to follow a viewmodel pattern? this seems to work real good already
class States() {
    companion object{
        @Composable
        fun getMutableStates(): MutableStates {
            return MutableStates(
                itemsToCountMap = remember { mutableStateMapOf<Item, MutableState<Int>>() },
                currentPage = remember { mutableStateOf(Pages.Cashier) },
                requester = remember { FocusRequester() },
                stringBuilder = remember { mutableStateOf(StringBuilder()) },
                showDatabaseInsertionPage = remember { mutableStateOf(false) }
            )
        }
    }
}