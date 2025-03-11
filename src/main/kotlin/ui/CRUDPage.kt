package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import database.Database

@Composable
fun CRUDPage(
    requester: FocusRequester,
    showDatabaseInsertionPage: MutableState<Boolean>
) {
    Column(modifier = Modifier.focusRequester(requester).padding(10.dp)) {
        var itemName by remember { mutableStateOf("") }
        var barcode by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }

        val openAlertDialog = remember { mutableStateOf(false) }
        val dialogText = remember { mutableStateOf("") }

        OutlinedTextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("bardcode") },
            singleLine = true
        )

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("itemName") },
            singleLine = true
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("price") },
            singleLine = true
        )

        Row {
            Button(
                onClick = {
                    if (barcode.isEmpty() || itemName.isEmpty() || price.isEmpty()) {
                        // modal say that these must not be empty
                        dialogText.value = "Dapat naay unod ang mga kahon"
                    } else {
                        if (Database.isItemInDatabaseById(barcode.toLong())) {
                            dialogText.value = "Naa na ni na barcode sa atung database"
                        } else if (Database.insertItem(
                                Item(
                                    id = barcode.toLong(),
                                    name = itemName,
                                    price = price.toDouble()
                                )
                            )
                        ) {
                            dialogText.value = "Na dugang ang $barcode | $itemName sa presyo na $price ₱"
                        } else {
                            dialogText.value = "Wala na dugang ang item, palihug lantaw usab sa mga kahon"
                        }
                    }

                    openAlertDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Dugang")
            }

            Button(
                onClick = {
                    if (barcode.isNotEmpty()) {
                        Database.deleteItemById(barcode.toLong())
                        dialogText.value = "Na delete na ang ${barcode.toLong()} sa database"
                        openAlertDialog.value = true
                    } else {
                        dialogText.value =
                            "Wala na delete ang ${barcode.toLong()}, palihug check ug naa ba gyud ni sa database o wala"
                        openAlertDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(255, 127, 127)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Tangtang")
            }

            Button(
                onClick = {
                    if (barcode.isEmpty() || itemName.isEmpty() || price.isEmpty()) {
                        if (Database.updateAnItem(
                                Item(
                                    id = barcode.toLong(),
                                    name = itemName,
                                    price = price.toDouble()
                                )
                            )
                        ) {
                            dialogText.value =
                                "Na ilis na ang item na naay barcode: $barcode"
                            openAlertDialog.value = true
                        }
                    } else {
                        dialogText.value =
                            "Wala na ilis ang item na naay barcode: $barcode, palihug ko double check sa mga kahon"
                        openAlertDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(173, 216, 230)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Ilis")
            }

            Button(
                onClick = {
                    dialogText.value = "Wala pa ni na himo"
                    openAlertDialog.value = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Kuha")
            }
        }

        Button(onClick = {
            showDatabaseInsertionPage.value = false
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(144, 238, 144))) {
            Text("Back to Cashier")
        }

        if (openAlertDialog.value) {
            Dialog(onDismissRequest = {
                openAlertDialog.value = false
            }) {
                Text(text = dialogText.value, fontSize = TextUnit(20f, TextUnitType.Sp), color = Color.White)
            }
        }
    }
}