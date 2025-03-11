import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.Item
import ui.ItemsSection
import ui.TotalsSection
import util.handleKeyEvents
import java.io.File
import java.lang.StringBuilder
import javax.imageio.ImageIO

@Composable
@Preview
fun App() {
    val itemsToCountMap = remember { mutableStateMapOf<Item, MutableState<Int>>() }
    val requester = remember { FocusRequester() }
    val stringBuilder = remember { mutableStateOf(StringBuilder()) }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

    MaterialTheme {
        Column(modifier = Modifier.focusRequester(requester).focusable().onKeyEvent {
            return@onKeyEvent handleKeyEvents(it, itemsToCountMap, stringBuilder)
        }) {
            ItemsSection(
                modifier = Modifier.padding(10.dp).fillMaxWidth()
                    .fillMaxHeight(0.8f),
                itemsToCountMap = itemsToCountMap
            )
            TotalsSection(
                modifier = Modifier.padding(10.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                itemsToCountMap = itemsToCountMap
            )
        }
    }
}

fun main() = application {
    val icon = ImageIO.read(File("src/main/kotlin/images/ruales_icon.png"))

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ruales POS",
        icon = BitmapPainter(image = icon.toComposeImageBitmap())
    ) {
        App()
    }
}
