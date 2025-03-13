package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import error_handling.dialogs.ErrorDialog
import global_util.safeRun
import states.MutableStates
import states.Pages
import ui.crud_page.CRUDPage
import ui.tinda_page.Cashier

@Composable
fun ContentSection(mutableStates: MutableStates) {
    Box(modifier = Modifier.padding(top = 10.dp, end = 10.dp, bottom = 10.dp)) {
        when (mutableStates.currentPage.value) {
            Pages.Tinda -> Cashier(mutableStates)
            Pages.Datos -> CRUDPage(mutableStates)
            Pages.Utangan -> {
                safeRun(mutableStates) {
                    mutableStates.currentPage.value = Pages.Tinda
                    error("Incoming pa ni")
                }
            }

            Pages.Imbentaryo -> {
                safeRun(mutableStates) {
                    mutableStates.currentPage.value = Pages.Tinda
                    error("Incoming pa ni")
                }
            }

            Pages.Halin -> {
                safeRun(mutableStates) {
                    mutableStates.currentPage.value = Pages.Tinda
                    error("Incoming pa ni")
                }
            }

            Pages.BarcodeGen -> {
                safeRun(mutableStates) {
                    mutableStates.currentPage.value = Pages.Tinda
                    error("Incoming pa ni")
                }
            }
        }

        if (mutableStates.errorDialogConfig.value.openErrorDialog) {
            ErrorDialog(mutableStates)
        }
    }
}