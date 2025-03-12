package error_handling

import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.application
import error_handling.exceptions_wrapper.DatabaseException
import states.ErrorDialogConfig
import states.MutableStates
import java.lang.NumberFormatException

class ErrorHandler {
    companion object {
        fun handleException(e: Exception, mutableState: MutableStates) {
            val errorDialogConfig = ErrorDialogConfig(openErrorDialog = true, errorMessage = e.message.toString(), exception = e)

            println(e)

            when(e) {
                is DatabaseException -> {
                    errorDialogConfig.errorMessage = "Could not connect ot database"
                }

                else -> {
                }
            }

            mutableState.errorDialogConfig.value = errorDialogConfig
        }

        fun handleError(e: Error, mutableState: MutableStates) {
            val errorDialogConfig = ErrorDialogConfig(openErrorDialog = true, errorMessage = e.message.toString(), error = e)

            mutableState.errorDialogConfig.value = errorDialogConfig
        }

        fun handleThrowable(e: Throwable, mutableState: MutableStates) {
            val errorDialogConfig = ErrorDialogConfig(openErrorDialog = true, errorMessage = e.message.toString())
            mutableState.errorDialogConfig.value = errorDialogConfig
        }
    }
}