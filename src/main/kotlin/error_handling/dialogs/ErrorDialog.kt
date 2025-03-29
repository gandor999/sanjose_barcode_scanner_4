package error_handling.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import states.ErrorDialogConfig
import states.MutableStates

@Composable
fun ErrorDialog(mutableStates: MutableStates) {
    AlertDialog(
        onDismissRequest = {
            mutableStates.errorDialogConfig.value =
                ErrorDialogConfig()
        },
        buttons = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = {
                    mutableStates.errorDialogConfig.value =
                        ErrorDialogConfig()
                }) {
                    Text("Ok")
                }
            }
        },
        title = { Text("Error") },
        text = {
            Column {
                Text("Error Message: ${mutableStates.errorDialogConfig.value.errorMessage}")
                mutableStates.errorDialogConfig.value.throwable?.let {
                    Text("Error Type: " + it::class.simpleName)
                }
            }
        }
    )
}