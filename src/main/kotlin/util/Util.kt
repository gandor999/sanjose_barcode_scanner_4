package util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.*
import database.Database
import ui.Item

fun handleKeyEvents(
    keyEvent: KeyEvent,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    stringBuilder: MutableState<StringBuilder>,
): Boolean {
    val stringBuild = stringBuilder.value

    if (keyEvent.type == KeyEventType.KeyDown) {
        when(keyEvent.key) {
            Key.Enter -> {
                val scanId = stringBuild.toString().toLong()

                if (itemsToCountMap.entries.map { item -> item.key.id }
                        .contains(scanId) && Database.isItemInDatabaseById(scanId)) {
                    Database.getItemById(scanId)?.let {
                        itemsToCountMap[it]!!.value += 1
                    }
                } else if(Database.isItemInDatabaseById(scanId)) {
                    Database.getItemById(scanId)?.let { item ->
                        itemsToCountMap[Item(
                            price = item.price,
                            name = item.name,
                            id = item.id
                        )] = mutableStateOf(1)
                    }
                }

                stringBuild.clear()

                return true
            }
            Key.Delete -> {
                itemsToCountMap.clear()
            }
            else -> {
                stringBuild.append(java.awt.event.KeyEvent.getKeyText(keyEvent.key.nativeKeyCode))
            }
        }

        return false
    }

    return false
}