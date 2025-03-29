package error_handling

import error_handling.exceptions_wrapper.DatabaseException
import states.ErrorDialogConfig
import states.MutableStates

class ErrorHandler {
    companion object {
        fun handleThrowable(e: Throwable, mutableState: MutableStates) {
            val errorDialogConfig = ErrorDialogConfig(openErrorDialog = true, errorMessage = e.message.toString(), throwable = e)

            when(e) {
                is DatabaseException -> {
                    errorDialogConfig.errorMessage = "Could not connect to database"
                }

                is  NumberFormatException -> {
                    errorDialogConfig.errorMessage = "Dapat number ang e butang"
                }

                else -> {
                }
            }

            if (errorDialogConfig.errorMessage != e.message) errorDialogConfig.errorMessage += "; ${e.message}"

            mutableState.errorDialogConfig.value = errorDialogConfig
        }
    }
}