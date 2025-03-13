package states

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.focus.FocusRequester
import ui.models.Item

data class MutableStates(
    val itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    val requester: FocusRequester,
    val currentPage: MutableState<Pages>,
    val stringBuilder: MutableState<StringBuilder /* = StringBuilder */>,
    val errorDialogConfig: MutableState<ErrorDialogConfig>
)

data class ErrorDialogConfig(
    var openErrorDialog: Boolean = false,
    var errorMessage: String = "",
    var exception: Exception? = null,
    var error: Error? = null
)

// do we really need to follow a viewmodel pattern? this seems to work real good already
object States {
    lateinit var mutableStates: MutableStates

    @Composable
    fun init(): MutableStates {
        mutableStates = MutableStates(
            itemsToCountMap = remember { mutableStateMapOf() },
            currentPage = remember { mutableStateOf(Pages.Tinda) },
            requester = remember { FocusRequester() },
            stringBuilder = remember { mutableStateOf(StringBuilder()) },
            errorDialogConfig = remember { mutableStateOf(ErrorDialogConfig()) }
        )

        return mutableStates
    }
}