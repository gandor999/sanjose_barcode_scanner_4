import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import io.github.cdimascio.dotenv.Dotenv
import ui.CRUDPage
import ui.Cashier
import ui.Item
import java.io.File
import java.nio.file.Paths
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
    val icon = ImageIO.read(File(Paths.get("images/ruales_icon.png").toString()))
    val dotEnv = Dotenv.configure().directory(Paths.get(".env").toUri().toString()).load()

    try {
        Database.connection =
            DriverManager.getConnection(dotEnv["DATABASE_URL"], dotEnv["DATABASE_USER"], dotEnv["DATABASE_PASSWORD"])
    } catch (e: SQLException) {
        println("Connection to database failed!")
        e.printStackTrace()
    }

    Database.connection?.let {
        println("Connected to database")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ruales POS",
        icon = BitmapPainter(image = icon.toComposeImageBitmap())
    ) {
        App()
    }
}
