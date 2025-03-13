package ui.tinda_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import states.MutableStates
import ui.models.ItemWrapper

@Composable
fun ItemsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    mutableStates: MutableStates
) {
    val itemsToCountMap = mutableStates.itemsToCountMap

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