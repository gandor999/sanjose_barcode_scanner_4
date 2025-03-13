package error_handling

import error_handling.exceptions_wrapper.DatabaseException
import states.ErrorDialogConfig
import states.MutableStates

class ErrorHandler {
    companion object {
        fun handleException(e: Exception, mutableState: MutableStates) {
            val errorDialogConfig = ErrorDialogConfig(openErrorDialog = true, errorMessage = e.message.toString(), exception = e)

            println(e)

            when(e) {
                is DatabaseException -> {
                    errorDialogConfig.errorMessage = "Could not connect ot database"
                }

                is  NumberFormatException -> {
                    errorDialogConfig.errorMessage = "Dapat number ang e butang"
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