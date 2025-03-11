package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.ref.WeakReference

data class Item(
    val price: Double,
    val name: String
)

@Composable
fun ItemWrapper(
    modifier: Modifier = Modifier,
    item: Item,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemStack: WeakReference<SnapshotStateList<Item>>,
    count: MutableState<Int>
) {
    val statePrice = remember { mutableStateOf(item.price) }

    Row(modifier = modifier, horizontalArrangement = horizontalArrangement, verticalAlignment = verticalAlignment) {
        Text(text = "${item.name} x${count.value}", modifier = Modifier.padding(horizontal = 10.dp))
        Row(verticalAlignment = verticalAlignment) {
            Text(text = "${statePrice.value * count.value} ₱", modifier = Modifier.padding(horizontal = 10.dp))
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
                        itemStack.get()?.remove(item)
                    } else {
                        count.value -= 1
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("-")
            }
        }
    }
}