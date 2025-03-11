import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.Item
import ui.ItemsSection
import ui.TotalsSection
import java.awt.event.KeyEvent
import java.io.File
import javax.imageio.ImageIO

@Composable
@Preview
fun App() {
    val itemsToCountMap = remember { mutableStateMapOf<Item, MutableState<Int>>() } // lift this up

    MaterialTheme {
        Column {
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

    val stringBuilder = StringBuilder()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ruales POS",
        icon = BitmapPainter(image = icon.toComposeImageBitmap()),
        onKeyEvent = {
            if (it.type == KeyEventType.KeyDown) {

                // well need a price to barcode map
                if (it.key == Key.Enter) {
                    println(stringBuilder.toString())
                    stringBuilder.clear()
                    return@Window true
                }

                stringBuilder.append(KeyEvent.getKeyText(it.key.nativeKeyCode))

                return@Window false
            }

            return@Window false
        }
    ) {
        App()
    }
}
