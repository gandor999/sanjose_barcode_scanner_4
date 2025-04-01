package ui.barcode_gen

import database.Database
import global_util.SupportedPrinterDPI
import global_util.SupportedPrinterWidth
import net.sourceforge.barbecue.Barcode
import net.sourceforge.barbecue.BarcodeFactory
import net.sourceforge.barbecue.BarcodeImageHandler
import ui.tinda_page.addExtraLineToImage
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.print.PageFormat
import java.awt.print.Printable
import java.util.*
import kotlin.math.roundToInt


class BarcodeGenerator: Printable {
    private val limitRandomStringLength = 9
    private var barcodeImageGenerated: BufferedImage? = null;

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

        var newPaddedImage = BufferedImage(paddingHorizontal, paddingVertical, rawBarcodeImage.type);

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

        newPaddedImage = addExtraLineToImage(newPaddedImage, 50)
        val g2 = newPaddedImage.createGraphics()
        g2.font = Font("Arial", Font.PLAIN, 15)
        g2.color = Color.BLACK
        val cutLine = "---------------"
        g2.drawString(cutLine, (newPaddedImage.width - g2.fontMetrics.stringWidth(cutLine)) / 2, newPaddedImage.height - g2.fontMetrics.height)
        g2.dispose()

        barcodeImageGenerated = newPaddedImage

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

    override fun print(graphics: Graphics?, pageFormat: PageFormat?, pageIndex: Int): Int {
        if (this.barcodeImageGenerated == null) return Printable.NO_SUCH_PAGE

        this.barcodeImageGenerated?.let {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            val originalWidth: Int = it.width
            val originalHeight: Int = it.height

            val targetWidthMM = SupportedPrinterWidth
            val printerDPI = SupportedPrinterDPI

            val targetWidth = (targetWidthMM * printerDPI / 80).toInt() // Convert mm to pixels
            val scaleFactor = targetWidth.toDouble() / originalWidth
            val targetHeight = (originalHeight * scaleFactor).toInt()

            val posX = (pageFormat?.paper?.width?.toInt()?.minus(targetWidth))?.div(2)?.minus(2.5)?.roundToInt() // the printer seems to have some extra padding to the left, even when using the FEED button, it doesn't center things completely

            graphics?.drawImage(it, posX!!, 0, targetWidth, targetHeight, null);
        }

        return Printable.PAGE_EXISTS
    }
}