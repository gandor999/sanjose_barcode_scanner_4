import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import io.github.cdimascio.dotenv.Dotenv
import ui.*
import util.DatabaseMetadata
import util.handleKeyEvents
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import javax.imageio.ImageIO


@Composable
@Preview
fun App() {
    val itemsToCountMap = remember { mutableStateMapOf<Item, MutableState<Int>>() }
    val requester = remember { FocusRequester() }
    val stringBuilder = remember { mutableStateOf(StringBuilder()) }
    val showDatabaseInsertionPage = remember { mutableStateOf(false) } // should this be refactored? For now nahhh

    MaterialTheme {
        if (!showDatabaseInsertionPage.value) {
            Cashier(
                itemsToCountMap = itemsToCountMap,
                requester = requester,
                stringBuilder = stringBuilder,
                showDatabaseInsertionPage = showDatabaseInsertionPage
            )
        } else {
            CRUDPage(requester = requester, showDatabaseInsertionPage = showDatabaseInsertionPage)
        }
    }
}

fun main() = application {
    val icon = ImageIO.read(File("src/main/kotlin/images/ruales_icon.png"))
    val dotEnv = Dotenv.configure().directory(Paths.get(".env").toUri().toString()).load()

    try {
        Database.connection =
            DriverManager.getConnection(dotEnv["DATABASE_URL"], dotEnv["DATABASE_USER"], dotEnv["DATABASE_PASSWORD"])
    } catch (e: SQLException) {
        println("Connection failed!")
        e.printStackTrace()
    }

    Database.connection?.let {
        println("connected to database")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ruales POS",
        icon = BitmapPainter(image = icon.toComposeImageBitmap())
    ) {
        App()
    }
}
