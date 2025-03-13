package ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.gson.GsonBuilder
import ui.models.Item

@Composable
fun SuccessDialog(openSuccessDialog: MutableState<Boolean>, dialogText: MutableState<String>, item: MutableState<Item?>) {
    fun onDismiss() {
        openSuccessDialog.value = false
        dialogText.value = ""
        item.value = null
    }

    Card {
        AlertDialog(
            backgroundColor = Color(174, 243, 231),
            onDismissRequest = {
                onDismiss()
            },
            buttons = {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        onDismiss()
                    }) {
                        Text("Ok")
                    }
                }
            },
            title = { Text("Success") },
            text = {
                Column {
                    Text(dialogText.value)
                    item.value?.let {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        Text(gson.toJson(item.value), style = TextStyle(fontFamily = FontFamily.Monospace), modifier = Modifier.padding(top = 10.dp))
                    }
                }
            }
        )
    }
}