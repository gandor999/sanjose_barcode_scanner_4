package util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.input.key.*
import ui.Item

fun handleKeyEvents(
    keyEvent: KeyEvent,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    stringBuilder: MutableState<StringBuilder>
): Boolean {
    val stringBuild = stringBuilder.value

    // let 748485100418 be sample id for item, this is canned tuna
    val sampleMapForItemInDatabase = HashMap<Long, Item>()
    sampleMapForItemInDatabase[748485100418L] = Item(
        name = "Tuna",
        price = 100.00,
        id = 748485100418L
    )

    if (keyEvent.type == KeyEventType.KeyDown) {
        // well need a price to barcode map
        if (keyEvent.key == Key.Enter) {
            val scanId = stringBuild.toString().toLong()

            if (itemsToCountMap.entries.map { item -> item.key.id }
                    .contains(scanId)) {
                sampleMapForItemInDatabase[scanId]?.let {
                    itemsToCountMap[sampleMapForItemInDatabase[scanId]]!!.value += 1
                }

            } else {
                sampleMapForItemInDatabase[scanId]?.let { item ->
                    itemsToCountMap[Item(
                        price = item.price,
                        name = item.name,
                        id = scanId
                    )] = mutableStateOf(1)
                }
            }

            stringBuild.clear()

            return true
        }

        stringBuild.append(java.awt.event.KeyEvent.getKeyText(keyEvent.key.nativeKeyCode))

        return false
    }

    return false
}