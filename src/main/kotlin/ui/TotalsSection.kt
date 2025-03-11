package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TotalsSection(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemsToCountMap: SnapshotStateMap<Item, MutableState<Int>>,
    showDatabaseInsertionPage: MutableState<Boolean>
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        var totalPrice = 0.00;
        val openAlertDialog = remember { mutableStateOf(false) }
        val sensilyo = remember { mutableStateOf(0.00) }
        val bayad = remember { mutableStateOf("") }

        itemsToCountMap.forEach { entry ->
            totalPrice += (entry.key.price * entry.value.value)
        }

        if (openAlertDialog.value) {
            Dialog(onDismissRequest = {
                openAlertDialog.value = false
            }) {
                Column(modifier = Modifier.background(color = Color.White).padding(10.dp).fillMaxWidth()) {
                    OutlinedTextField(
                        value = bayad.value,
                        onValueChange = { bayad.value = it },
                        label = { Text("Bayad") },
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (bayad.value.isNotEmpty()) sensilyo.value = bayad.value.toDouble() - totalPrice

                        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127))
                    ) {
                        Text("Kwenta")
                    }

                    Text("Sensilyo: ${sensilyo.value} ₱", fontSize = TextUnit(35f, TextUnitType.Sp))
                }
            }
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
                        showDatabaseInsertionPage.value = true
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Datos")
                }

                Button(
                    onClick = {
                        openAlertDialog.value = true
//                        dialogText.value = ""
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Sensilyo")
                }

                Button(
                    onClick = {
//                        itemsToCountMap.clear()
                        // TODO
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(173, 216, 230)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Panghad")
                }
                Button(
                    onClick = {
                        itemsToCountMap.clear()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127)),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Hawan")
                }
            }
        }
    }
}