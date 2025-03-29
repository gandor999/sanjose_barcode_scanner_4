package ui.barcode_gen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.dp
import database.Database
import global_util.BarcodeGenerator
import global_util.safeRun
import global_util.safeRunAsync
import kotlinx.coroutines.*
import net.sourceforge.barbecue.Barcode
import states.MutableStates
import ui.dialogs.SuccessDialog
import ui.models.Item
import java.awt.image.BufferedImage

@Composable
fun BarcodeGenPage(mutableStates: MutableStates) {
    val itemName = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val openBarcodeImage = remember { mutableStateOf(false) }
    val startLoading = remember { mutableStateOf(false) }
    val barcode = remember { mutableStateOf<Barcode?>(null) }
    val barcodeImage = remember { mutableStateOf<BufferedImage?>(null) }
    val openSuccessDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    val item = remember { mutableStateOf<Item?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().border(width = 1.dp, color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (openBarcodeImage.value) {
            barcodeImage.value?.let {
                Image(painter = it.toPainter(), contentDescription = "")
                startLoading.value = false
            }
        } else if (startLoading.value && !openBarcodeImage.value) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Color.Gray
            )
        }

        if (openSuccessDialog.value) {
            SuccessDialog(openSuccessDialog, dialogText, item)
        }

        OutlinedTextField(
            value = itemName.value,
            onValueChange = { itemName.value = it },
            label = { Text("itemName") },
            singleLine = true,
            placeholder = { Text("Sample: Orange") }
        )

        OutlinedTextField(
            value = price.value,
            onValueChange = { price.value = it },
            label = { Text("price") },
            singleLine = true,
            placeholder = { Text("Sample: 2.00") }
        )

        Row(
            modifier = Modifier.width(300.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                safeRun(mutableStates) {
                    listOf("itemName" to itemName.value, "price" to price.value).forEach { (name, value) -> require(value.isNotEmpty()) { "Dapat naay naka sulat sa $name" } }

                    startLoading.value = true

                    safeRunAsync(mutableStates, Dispatchers.IO) {
                        // not very noticable but it does do some checking on the database
                        val barcodeGenerator = BarcodeGenerator()
                        barcode.value = barcodeGenerator.getNewBarcode()
                        barcode.value?.let{
                            barcodeImage.value = barcodeGenerator.getBarcodeImage(it, "${itemName.value} --- ₱ ${price.value}")
                            openBarcodeImage.value = true
                        }
                    }
                }
            }) {
                Text("Himo")
            }

            Button(onClick = {
                safeRun(mutableStates) {
                    require(openBarcodeImage.value) {"Barcode image must first be made"}
                }
            }) {
                Text("Print")
            }

            if (openBarcodeImage.value) {
                Button(onClick = {
                    safeRun(mutableStates) {
                        listOf("barcode" to barcode.value, "itemName" to itemName.value, "price" to price.value)
                            .forEach { (name, value) -> require(value != null) { "Dapat naay unod ang $name" } }
                        barcode.value?.let {
                            safeRunAsync(mutableStates, Dispatchers.IO) {
                                onClickDugang(itemName.value, it.data.toLong(), price.value.toDouble())
                                if (Database.isItemInDatabaseById(it.data.toLong())) {
                                    openSuccessDialog.value = true
                                    dialogText.value = "Na dugang ang item"
                                    item.value = Item(price = price.value.toDouble(), name = itemName.value, id = it.data.toLong())
                                }
                            }
                        }
                    }
                }) {
                    Text("Dugang")
                }
            }
        }

    }
}