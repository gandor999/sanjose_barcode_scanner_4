package global_util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.*
import database.Database
import error_handling.ErrorHandler
import states.MutableStates
import ui.models.Item

fun handleKeyEvents(
    keyEvent: KeyEvent,
    mutableStates: MutableStates
): Boolean {
    val stringBuild = mutableStates.stringBuilder.value
    val itemsToCountMap = mutableStates.itemsToCountMap

    if (keyEvent.type == KeyEventType.KeyDown) {
        when (keyEvent.key) {
            Key.Enter -> {
                safeRun(mutableStates) {
                    val scanId = stringBuild.toString().toLong()

                    if (itemsToCountMap.entries.map { item -> item.key.id }
                            .contains(scanId) && Database.isItemInDatabaseById(scanId)) {
                        Database.getItemById(scanId)?.let {
                            itemsToCountMap[it]!!.value += 1
                        }
                    } else if (Database.isItemInDatabaseById(scanId)) {
                        Database.getItemById(scanId)?.let { item ->
                            itemsToCountMap[Item(
                                price = item.price,
                                name = item.name,
                                id = item.id
                            )] = mutableStateOf(1)
                        }
                    }

                    stringBuild.clear()
                }

                return true
            }

            Key.Delete -> {
                safeRun(mutableStates) {
                    itemsToCountMap.clear()
                }
            }

            else -> {
                safeRun(mutableStates) {
                    stringBuild.append(java.awt.event.KeyEvent.getKeyText(keyEvent.key.nativeKeyCode))
                }
            }
        }

        return false
    }

    return false
}

/**
 * Since we can't catch exceptions directly from the topmost composable, we can organize the flow of exceptions this way by decorating every lambda or function invocation, it's not a pretty sight but it beats having to have try catches on every freaking onClick block!
 * */
fun safeRun(mutableStates: MutableStates, func: () -> Unit) {
    try {
        func()
    } catch (e: Exception) {
        ErrorHandler.handleException(e, mutableStates)
    } catch (e: Error) {
        ErrorHandler.handleError(e, mutableStates)
    }
}