package ui.tinda_page

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import states.MutableStates
import ui.dialogs.KwentaDialog

@Composable
fun TotalsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    mutableStates: MutableStates
) {
    val itemsToCountMap = mutableStates.itemsToCountMap

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        var totalPrice = 0.00;
        val openKwentaDialog = remember { mutableStateOf(false) }

        itemsToCountMap.forEach { entry ->
            totalPrice += (entry.key.price * entry.value.value)
        }

        if (openKwentaDialog.value) {
            KwentaDialog(openKwentaDialog, totalPrice)
        }

        Row(
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tibuok: $totalPrice ₱", fontSize = TextUnit(35f, TextUnitType.Sp))
            Row {
                Button(
                    onClick = {
                        openKwentaDialog.value = true
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(116, 140, 171)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Sukli")
                }

                Button(
                    onClick = {
                        // TODO
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(116, 140, 171)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Resibo")
                }
                Button(
                    onClick = {
                        itemsToCountMap.clear()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(171, 78, 104)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Hawan")
                }
            }
        }
    }
}