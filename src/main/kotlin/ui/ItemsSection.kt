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

@Composable
fun ItemsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        item {
            itemsToCountMap.forEach { entry ->
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