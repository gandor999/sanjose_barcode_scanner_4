package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import util.handleKeyEvents

@Composable
fun Cashier(
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    requester: FocusRequester,
    stringBuilder: MutableState<StringBuilder /* = StringBuilder */>,
    showDatabaseInsertionPage: MutableState<Boolean>
) {
    LaunchedEffect(Unit) {
        requester.captureFocus() // this seems like the most likely solution to not have the other buttons be focused
        requester.requestFocus()
    }
    Column(modifier = Modifier.focusRequester(requester).focusable().onKeyEvent {
        return@onKeyEvent handleKeyEvents(it, itemsToCountMap, stringBuilder)
    }.onFocusChanged {
        if (!it.isFocused) {
            requester.requestFocus()
        }
    }) {
        ItemsSection(
            modifier = Modifier.padding(10.dp).fillMaxWidth().border(width = 1.dp, color = Color.Black)
                .fillMaxHeight(0.8f),
            itemsToCountMap = itemsToCountMap
        )
        TotalsSection(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            itemsToCountMap = itemsToCountMap,
            showDatabaseInsertionPage = showDatabaseInsertionPage
        )
    }
}