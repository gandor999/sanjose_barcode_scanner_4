package ui.crud_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import global_util.safeRun
import states.MutableStates
import ui.dialogs.SuccessDialog
import ui.models.Item

data class CRUDPageStates(
    val itemName: MutableState<String>,
    val barcode: MutableState<String>,
    val price: MutableState<String>,
    val openSuccessDialog: MutableState<Boolean>,
    val dialogText: MutableState<String>,
    val queriedItem: MutableState<Item?>
)

@Composable
fun CRUDPage(mutableStates: MutableStates) {
    val requester = mutableStates.requester

    val crudPageStates = CRUDPageStates(
        itemName = remember { mutableStateOf("") },
        barcode = remember { mutableStateOf("") },
        price = remember { mutableStateOf("") },
        openSuccessDialog = remember { mutableStateOf(false) },
        dialogText = remember { mutableStateOf("") },
        queriedItem = remember { mutableStateOf<Item?>(null) }
    )

    Column(modifier = Modifier.focusRequester(requester)) {
        val (itemName, barcode, price, openSuccessDialog, dialogText, queriedItem) = crudPageStates

        OutlinedTextField(
            value = barcode.value,
            onValueChange = { barcode.value = it },
            label = { Text("bardcode") },
            singleLine = true,
            placeholder = { Text("Sample: 748485100418") }
        )

        OutlinedTextField(
            value = itemName.value,
            onValueChange = { itemName.value = it },
            label = { Text("itemName") },
            singleLine = true,
            placeholder = { Text("Sample: Tuna") }
        )

        OutlinedTextField(
            value = price.value,
            onValueChange = { price.value = it },
            label = { Text("price") },
            singleLine = true,
            placeholder = { Text("Sample: 10.00") }
        )

        Row(modifier = Modifier.padding(top = 10.dp)) {
            Button(
                onClick = {
                    safeRun(mutableStates) {
                        onClickDugang(crudPageStates)
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
                        onClickIlis(crudPageStates)
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
                        onClickLantaw(crudPageStates)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(187, 190, 100)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Lantaw")
            }

            Button(
                onClick = {
                    safeRun(mutableStates) {
                        onClickTangTang(crudPageStates)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(171, 78, 104)),
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text("Tangtang")
            }
        }

        if (openSuccessDialog.value) {
            SuccessDialog(openSuccessDialog, dialogText, queriedItem)
        }
    }
}