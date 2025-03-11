package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Item(
    val price: Double,
    val name: String,
    val id: Long = 0L
)

@Composable
fun ItemWrapper(
    modifier: Modifier = Modifier,
    item: Item,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    count: MutableState<Int>,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>> ,
) {
    Row(modifier = modifier, horizontalArrangement = horizontalArrangement, verticalAlignment = verticalAlignment) {
        Text(text = "${item.name} x${count.value}", modifier = Modifier.padding(horizontal = 10.dp))
        Row(verticalAlignment = verticalAlignment) {
            Text(text = "${item.price * count.value} ₱", modifier = Modifier.padding(horizontal = 10.dp))
            Button(
                onClick = {
                    count.value += 1
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("+")
            }

            Button(
                onClick = {
                    if (count.value - 1 == 0) {
                        itemsToCountMap.remove(item)
                    } else {
                        count.value -= 1
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("-")
            }

            Button(
                onClick = {
                    itemsToCountMap.remove(item)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(0.dp)
            ) {
                Text("⛔\uFE0F")
            }
        }
    }
}