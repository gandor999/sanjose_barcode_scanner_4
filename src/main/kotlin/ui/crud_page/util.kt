package ui.crud_page

import database.Database
import ui.models.Item

suspend fun onClickDugang(crudPageStates: CRUDPageStates) {
    val (itemName, barcode, price, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    listOf("barcode" to barcode, "itemName" to itemName, "price" to price)
        .forEach { (name, value) -> check(value.value.isNotEmpty()) { "Dapat naay unod ang $name" } }

    val item = Item(
        id = barcode.value.toLong(),
        name = itemName.value,
        price = price.value.toDouble()
    )

    Database.insertItem(item)
    queriedItem.value = item
    dialogText.value = "Mao ni ang na dugang"
    openSuccessDialog.value = true
}

suspend fun onClickIlis(crudPageStates: CRUDPageStates) {
    val (itemName, barcode, price, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    listOf("barcode" to barcode, "itemName" to itemName, "price" to price)
        .forEach { (name, value) -> check(value.value.isNotEmpty()) { "Dapat naay unod ang $name" } }

    val updatedItem = Item(
        id = barcode.value.toLong(),
        name = itemName.value,
        price = price.value.toDouble()
    )

    check(Database.updateAnItem(updatedItem)) { "Wala na ilis ang item sa database, palihug check usab sa mga kahon" }

    queriedItem.value = updatedItem
    dialogText.value = "Pag ilis"
    openSuccessDialog.value = true
}

suspend fun onClickLantaw(crudPageStates: CRUDPageStates) {
    val (_, barcode, _, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    check(barcode.value.isNotEmpty()) { "Dapat naay unod ang barcode" }
    queriedItem.value = Database.getItemById(barcode.value.toLong())
    openSuccessDialog.value = true
    dialogText.value = "Mao ni ang na kuha"
}

suspend fun onClickTangTang(crudPageStates: CRUDPageStates) {
    val (_, barcode, _, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    check(barcode.value.isNotEmpty()) { "Dapat naay unod ang barcode" }

    queriedItem.value = Database.deleteItemById(barcode.value.toLong())
    dialogText.value = "Mao ni ang na delete"
    openSuccessDialog.value = true
}