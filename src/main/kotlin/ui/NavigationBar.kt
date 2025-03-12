package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import states.MutableStates
import states.Pages

@Composable
fun NavigationBar(mutableStates: MutableStates) {
    Card(
        modifier = Modifier.fillMaxWidth(0.2f).fillMaxHeight().padding(10.dp),
        backgroundColor = Color(13, 19, 33)
    ) {
        Column {
            Pages.entries.forEach {
                TextButton(
                    onClick = {
                        mutableStates.currentPage.value = it
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color(240, 235, 216),
                        backgroundColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Text(it.name, textAlign = TextAlign.Left)
                }
            }
        }
    }
}