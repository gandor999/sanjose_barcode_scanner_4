package ui.crud_page

import database.Database
import ui.models.Item

fun onClickDugang(crudPageStates: CRUDPageStates){
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

fun onClickIlis(crudPageStates: CRUDPageStates){
    val (itemName, barcode, price, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    listOf("barcode" to barcode, "itemName" to itemName, "price" to price)
        .forEach { (name, value) -> check(value.value.isNotEmpty()) { "Dapat naay unod ang $name" } }
    check(
        Database.updateAnItem(
            Item(
                id = barcode.value.toLong(),
                name = itemName.value,
                price = price.value.toDouble()
            )
        )
    ) { "Wala na ilis ang item sa database, palihug check usab sa mga kahon" }

    dialogText.value = "Na ilis na ang item na naay barcode: $barcode"
    openSuccessDialog.value = true
}

fun onClickLantaw(crudPageStates: CRUDPageStates){
    val (_, barcode, _, openSuccessDialog, dialogText, queriedItem) = crudPageStates

    check(barcode.value.isNotEmpty()) { "Dapat naay unod ang barcode" }
    queriedItem.value = Database.getItemById(barcode.value.toLong())
    openSuccessDialog.value = true
    dialogText.value = "Mao ni ang na kuha"
}

fun onClickTangTang(crudPageStates: CRUDPageStates){
    val (_, barcode, _, openSuccessDialog, dialogText, _) = crudPageStates

    check(barcode.value.isNotEmpty()) { "Dapat naay unod ang barcode" }
    check(Database.deleteItemById(barcode.value.toLong())) { "Wala na delete ang item sa database" }

    dialogText.value = "Na delete na ang ${barcode.value.toLong()} sa database"
    openSuccessDialog.value = true
}