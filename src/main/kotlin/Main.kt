import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import database.Database
import error_handling.ErrorDialog
import error_handling.ErrorHandler
import io.github.cdimascio.dotenv.Dotenv
import states.ErrorDialogConfig
import states.MutableStates
import states.Pages
import states.States
import ui.CRUDPage
import ui.Cashier
import util.safeRun
import java.io.File
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.SQLException
import javax.imageio.ImageIO


@Composable
@Preview
fun App(mutableStates: MutableStates) {
    MaterialTheme {
//        Column(modifier = Modifier.fillMaxWidth(0.1f).border(wid)) {
//
//        }
        Row {
            // NavigationBar()
            when (mutableStates.currentPage.value) {
                Pages.Cashier -> Cashier(mutableStates)
                Pages.Database -> CRUDPage(mutableStates)
                Pages.Debt -> TODO()
                Pages.Inventory -> TODO()
                Pages.SalesRecord -> TODO()
            }

            if (mutableStates.errorDialogConfig.value.openErrorDialog) {
                ErrorDialog(mutableStates)
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
