package ui.tinda_page

import global_util.SupportedPrinterDPI
import global_util.SupportedPrinterWidth
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.print.PageFormat
import java.awt.print.Printable
import kotlin.math.roundToInt

class Receipt(private val receiptImage: BufferedImage): Printable {

    override fun print(graphics: Graphics?, pageFormat: PageFormat?, pageIndex: Int): Int {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        val originalWidth: Int = receiptImage.width
        val originalHeight: Int = receiptImage.height

        val targetWidthMM = SupportedPrinterWidth
        val printerDPI = SupportedPrinterDPI

        val targetWidth = (targetWidthMM * printerDPI / 90).toInt() // Convert mm to pixels
        val scaleFactor = targetWidth.toDouble() / originalWidth
        val targetHeight = (originalHeight * scaleFactor).toInt()

        val posX = (pageFormat?.paper?.width?.toInt()?.minus(targetWidth))?.div(2)?.minus(2.5)?.roundToInt() // the printer seems to have some extra padding to the left, even when using the FEED button, it doesn't center things completely

        graphics?.drawImage(receiptImage, posX!!, 0, targetWidth, targetHeight, null);

        return Printable.PAGE_EXISTS
    }
}