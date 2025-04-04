package ui.models

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Item(
    val price: Double = 0.00,
    val name: String = "",
    val id: Long = 0L
)

@Composable
fun ItemWrapper(
    modifier: Modifier = Modifier,
    item: Item,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    count: MutableState<Int>,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
) {
    Row(modifier = modifier, horizontalArrangement = horizontalArrangement, verticalAlignment = verticalAlignment) {
        Row(modifier = Modifier.width(250.dp)) {
            Text(text = item.name, modifier = Modifier.padding(horizontal = 10.dp).width(200.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "x ${count.value}")
        }
        Row(verticalAlignment = verticalAlignment) {
            Text(text = "${item.price * count.value} ₱", modifier = Modifier.padding(horizontal = 10.dp))
            Button(
                onClick = {
                    count.value += 1
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(116, 140, 171)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("+", color = Color.White)
            }

            Button(
                onClick = {
                    if (count.value - 1 == 0) {
                        itemsToCountMap.remove(item)
                    } else {
                        count.value -= 1
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(171, 78, 104)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("-", color = Color.White)
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