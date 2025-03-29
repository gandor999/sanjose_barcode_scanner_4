package ui.barcode_gen

import database.Database
import ui.models.Item

suspend fun onClickDugang(itemName: String, barcode: Long, price: Double) {
    val item = Item(
        id = barcode,
        name = itemName,
        price = price
    )

    Database.insertItem(item)
}