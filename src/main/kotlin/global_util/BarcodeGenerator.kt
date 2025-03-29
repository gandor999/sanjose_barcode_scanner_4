package global_util

import database.Database
import net.sourceforge.barbecue.Barcode
import net.sourceforge.barbecue.BarcodeFactory
import net.sourceforge.barbecue.BarcodeImageHandler
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.util.*


class BarcodeGenerator {
    private val limitRandomStringLength = 15

    private fun getRandomString(): String {
        val leftLimit = 48; // '0'
        val rightLimit = 57; // '9'

        val random = Random()
        val buffer: StringBuilder = StringBuilder(limitRandomStringLength)

        for (i in 0 until limitRandomStringLength) {
            val randomLimitedInt: Int = leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
            buffer.append(randomLimitedInt.toChar())
        }

        return buffer.toString()
    }

    fun getBarcodeImage(barcode: Barcode, additionalText: String?): BufferedImage {
        val rawBarcodeImage = BarcodeImageHandler.getImage(barcode)
        val paddingHorizontal = rawBarcodeImage.width + 50
        val paddingVertical = rawBarcodeImage.height + 50

        val newPaddedImage = BufferedImage(paddingHorizontal, paddingVertical, rawBarcodeImage.type);

        // all these change consecutively like a C++ pointer, think of g as the pen
        val g = newPaddedImage.graphics

        // the padding process
        g.color = Color.white
        g.fillRect(0, 0, paddingHorizontal, paddingVertical)
        g.drawImage(rawBarcodeImage, (paddingHorizontal - rawBarcodeImage.width) / 2, 0, null) // position on center horizontal but stay on top

        // append string to image
        additionalText?.let {
            g.font = Font("Arial", Font.PLAIN, 20)
            g.color = Color.BLACK
            g.drawString(
                it,
                25,
                (newPaddedImage.height - g.fontMetrics.height) + g.fontMetrics.ascent - 10
            )
        }

        g.dispose()

        return newPaddedImage;
    }

    /**
    * Get a new barcode, as of now this doesn't check if the barcode exists in the database.
    * */
    suspend fun getNewBarcode(): Barcode {
        var barcode = BarcodeFactory.createCode39((getRandomString()).uppercase(), false)

        // this function must be in another concurrent process, use coroutines
        while (Database.isItemInDatabaseById(barcode.data.toLong())) {
            barcode = BarcodeFactory.createCode39((getRandomString()).uppercase(), false)
        }

        return barcode;
    }
}