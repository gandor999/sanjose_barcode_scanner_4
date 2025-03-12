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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import database.Database
import states.MutableStates
import util.safeRun

@Composable
fun CRUDPage(mutableStates: MutableStates) {
    val requester = mutableStates.requester

    Column(modifier = Modifier.focusRequester(requester)) {
        var itemName by remember { mutableStateOf("") }
        var barcode by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }

        val openSuccessDialog = remember { mutableStateOf(false) }
        val dialogText = remember { mutableStateOf("") }

        OutlinedTextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("bardcode") },
            singleLine = true,
            placeholder = { Text("Sample: 748485100418")}
        )

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("itemName") },
            singleLine = true,
            placeholder = { Text("Sample: Tuna")}
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("price") },
            singleLine = true,
            placeholder = { Text("Sample: 10.00")}
        )

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Button(
                onClick ={
                    safeRun(mutableStates) {
                        check(barcode.isNotEmpty() || itemName.isNotEmpty() || price.isNotEmpty()) { "Dapat naay unod ang mga kahon" }
                        check(
                            Database.insertItem(
                                Item(
                                    id = barcode.toLong(),
                                    name = itemName,
                                    price = price.toDouble()
                                )
                            )
                        ) { "Wala na dugang ang item, palihug lantaw usab sa mga kahon" }

                        dialogText.value = "Na dugang ang $barcode | $itemName sa presyo na $price ₱"
                        openSuccessDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(187, 190, 100)),
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Text("Dugang")
            }

            Button(
                onClick = {
                    safeRun(mutableStates) {
                        check(barcode.isNotEmpty() || itemName.isNotEmpty() || price.isNotEmpty()) { "Dapat naay sulod ang mga kahon" } // should these be just
                        check(
                            Database.updateAnItem(
                                Item(
                                    id = barcode.toLong(),
                                    name = itemName,
                                    price = price.toDouble()
                                )
                            )
                        ) { "Wala na ilis ang item sa database, palihug check usab sa mga kahon" }

                        dialogText.value = "Na ilis na ang item na naay barcode: $barcode"
                        openSuccessDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(187, 190, 100)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Ilis")
            }

            Button(
                onClick = {
                    safeRun(mutableStates) {
                        error("Wala pa ni na himo")
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(187, 190, 100)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Kuha")
            }

            Button(
                onClick = {
                    safeRun(mutableStates) {
                        check(barcode.isNotEmpty()) { "Dapat naay unod ang barcode" }

                        check(Database.deleteItemById(barcode.toLong())) { "Wala na delete ang item sa database" }

                        dialogText.value = "Na delete na ang ${barcode.toLong()} sa database"
                        openSuccessDialog.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(171, 78, 104)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Tangtang")
            }
        }

        if (openSuccessDialog.value) {
            SuccessDialog(openSuccessDialog, dialogText)
//            Dialog(onDismissRequest = {
//                openSuccessDialog.value = false
//            }) {
//                Text(text = dialogText.value, fontSize = TextUnit(20f, TextUnitType.Sp), color = Color.White)
//            }
        }
    }
}