import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import error_handling.ErrorDialog
import io.github.cdimascio.dotenv.Dotenv
import states.MutableStates
import states.Pages
import states.States
import ui.CRUDPage
import ui.Cashier
import ui.ContentSection
import ui.NavigationBar
import util.safeRun
import java.io.File
import java.nio.file.Paths
import java.sql.DriverManager
import javax.imageio.ImageIO


@Composable
@Preview
fun App(mutableStates: MutableStates) {
    MaterialTheme {
        Card(backgroundColor = Color(240, 235, 216)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                NavigationBar(mutableStates)
                ContentSection(mutableStates)
            }
        }
    }
}

fun main() {
    application {
        val icon = ImageIO.read(File(Paths.get("images/ruales_icon.png").toString()))
        val dotEnv = Dotenv.configure().directory(Paths.get(".env").toUri().toString()).load()
        val mutableStates = States.init()

        safeRun(mutableStates) {
            Database.connection =
                DriverManager.getConnection(
                    dotEnv["DATABASE_URL"],
                    dotEnv["DATABASE_USER"],
                    dotEnv["DATABASE_PASSWORD"]
                )
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "Ruales POS",
            icon = BitmapPainter(image = icon.toComposeImageBitmap())
        ) {
            App(mutableStates)
        }
    }
}
