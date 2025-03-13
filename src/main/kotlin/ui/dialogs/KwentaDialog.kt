package ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import global_util.safeRun
import states.States

@Composable
fun KwentaDialog(openKwentaDialog: MutableState<Boolean>, totalPrice: Double) {
    val sukli = remember { mutableStateOf(0.00) }
    val bayad = remember { mutableStateOf("") }

    if (openKwentaDialog.value) {
        Dialog(onDismissRequest = {
            openKwentaDialog.value = false
        }) {
            Card(
                backgroundColor = Color(224, 164, 88)
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    OutlinedTextField(
                        value = bayad.value,
                        onValueChange = { bayad.value = it },
                        label = { Text("Kwarta sa customer") },
                        singleLine = true
                    )

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Sukli: ${sukli.value} ₱", fontSize = TextUnit(25f, TextUnitType.Sp))
                        Button(
                            onClick = {
                                safeRun(States.mutableStates) {
                                    if (bayad.value.isNotEmpty()) sukli.value = bayad.value.toDouble() - totalPrice
                                }
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(171, 78, 104))
                        ) {
                            Text("Kwenta")
                        }
                    }
                }
            }
        }
    }
}