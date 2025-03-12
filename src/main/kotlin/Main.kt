import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import io.github.cdimascio.dotenv.Dotenv
import states.Pages
import states.States
import ui.CRUDPage
import ui.Cashier
import java.io.File
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.SQLException
import javax.imageio.ImageIO


@Composable
@Preview
fun App() {
    val mutableStates = States.getMutableStates()

    MaterialTheme {
        Row {
            // NavigationBar()
            when(mutableStates.currentPage.value) {
                Pages.Cashier -> Cashier(mutableStates)
                Pages.Database -> CRUDPage(mutableStates)
                Pages.Debt -> TODO()
                Pages.Inventory -> TODO()
                Pages.SalesRecord -> TODO()
            }
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
