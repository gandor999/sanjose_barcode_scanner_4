package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import states.MutableStates

@Composable
fun SuccessDialog(openSuccessDialog: MutableState<Boolean>, dialogText: MutableState<String>) {
    Card {
        AlertDialog(
            backgroundColor = Color(174, 243, 231),
            onDismissRequest = {
                openSuccessDialog.value = false
            },
            buttons = {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        openSuccessDialog.value = false
                    }) {
                        Text("Ok")
                    }
                }
            },
            title = { Text("Success") },
            text = {
                Column {
                    Text(dialogText.value)
                }
            }
        )
    }

}