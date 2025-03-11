import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.Item
import ui.ItemsSection

@Composable
@Preview
fun App() {
    val itemsToCountMap = remember { mutableStateMapOf<Item, MutableState<Int>>() } // lift this up

    MaterialTheme {
        ItemsSection(
            modifier = Modifier.padding(10.dp).fillMaxWidth()
                .fillMaxHeight(0.8f),
            itemsToCountMap = itemsToCountMap
        )
        // Totals Section
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
