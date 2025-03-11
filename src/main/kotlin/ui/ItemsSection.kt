package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.lang.ref.WeakReference

@Composable
fun ItemsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemsToCountMap: WeakReference<SnapshotStateMap<Item, MutableState<Int>>>
) {
    val itemsToCount = itemsToCountMap.get()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        item {
            Button(onClick = {
                if (itemsToCount?.containsKey(
                        Item(
                            price = 100.00,
                            name = "Can Tuna"
                        )
                    ) == true
                ) {
                    itemsToCount[Item(
                        price = 100.00,
                        name = "Can Tuna"
                    )]?.value = itemsToCount[Item(
                        price = 100.00,
                        name = "Can Tuna"
                    )]?.value!! + 1
                } else {
                    itemsToCount?.set(
                        Item(
                            price = 100.00,
                            name = "Can Tuna"
                        ), mutableStateOf(1)
                    )
                }
            }) {
                Text("Simulate bar code read")
            }

            itemsToCount?.forEach { entry ->
                ItemWrapper(
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(), item = entry.key,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    count = entry.value,
                    itemsToCountMap = itemsToCountMap
                )
            }
        }
    }
}