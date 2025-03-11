package ui

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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun TotalsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        var totalPrice = 0.00;

        itemsToCountMap.forEach { entry ->
            totalPrice += (entry.key.price * entry.value.value)
        }

        Row(
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total: $totalPrice ₱", fontSize = TextUnit(50f, TextUnitType.Sp))
            Row {
                Button(
                    onClick = {
                        itemsToCountMap.clear()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(173, 216, 230)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Print")
                }
                Button(
                    onClick = {
                        itemsToCountMap.clear()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}