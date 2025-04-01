package ui.tinda_page

import states.MutableStates
import java.awt.Color
import java.awt.Font
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.print.PageFormat
import java.awt.print.PrinterJob
import java.io.File
import javax.imageio.ImageIO

private val FONT = Font("Arial", Font.PLAIN, 15)
private val TOTAL_FONT = Font("Arial", Font.BOLD, 20)
private val EXTRA_PADDING = 10
private val EXTRA_PADDING_FROM_LOGO_TO_ITEMS = 100

private fun getHeightPerLine(font: Font): Int {
    val tempImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    val tempG = tempImage.graphics
    tempG.font = font

    return tempG.fontMetrics.height + 10
}

private fun createInitialImage(width: Int, height: Int, type: Int): BufferedImage {
    val brandImageFile = File("images/ruales_icon.png")
    val brandImage = ImageIO.read(brandImageFile).getScaledInstance(getHeightPerLine(FONT), getHeightPerLine(FONT), Image.SCALE_SMOOTH)

    val initialImage = BufferedImage(width, height, type)
    val graphics = initialImage.createGraphics()

    graphics.color = Color.WHITE
    graphics.fillRect(0, 0, width, height)
    graphics.drawImage(brandImage, (initialImage.width - brandImage.getWidth(null)) / 2, EXTRA_PADDING_FROM_LOGO_TO_ITEMS / 3, null)
    graphics.dispose()

    return initialImage
}

fun addExtraLineToImage(image: BufferedImage, newLineSpaceHeight: Int): BufferedImage {
    val newImage = BufferedImage(image.width, image.height + newLineSpaceHeight, image.type)
    val g2d = newImage.createGraphics()

    g2d.color = Color.WHITE
    g2d.fillRect(0, 0, newImage.width, newImage.height)
    g2d.drawImage(image, 0, 0, null)

    return newImage
}

fun printReceipt(mutableStates: MutableStates) {
    val heightPerLine = getHeightPerLine(FONT)
    val targetWidth = 300
    val initialHeight = heightPerLine + EXTRA_PADDING_FROM_LOGO_TO_ITEMS

    var anchorImage = createInitialImage(targetWidth, initialHeight, BufferedImage.TYPE_INT_RGB)

    var newLineHeight = anchorImage.height - heightPerLine
    var fontMetricHeightAndPadding = 0
    val separatedStrings = mutableListOf<String>()

    mutableStates.itemsToCountMap.forEach { (key, value) ->
        val recursiveImage = addExtraLineToImage(anchorImage, fontMetricHeightAndPadding)
        val g2d = recursiveImage.createGraphics()

        val limitWidth = anchorImage.width / 1.5

        if (g2d.fontMetrics.stringWidth("${key.name} x ${value.value}") >= limitWidth && separatedStrings.isEmpty()) {
            val stringBuilder = StringBuilder()
            stringBuilder.append("${key.name} x ${value.value}")

            var i = 0
            while (i < stringBuilder.length) {
                if (g2d.fontMetrics.stringWidth(stringBuilder.substring(0, i)) >= limitWidth) {
                    separatedStrings.add(stringBuilder.substring(0, i))
                    stringBuilder.delete(0, i)
                    i = 0
                    continue
                }

                i++
            }
            separatedStrings.add(stringBuilder.toString())
        }

        if (separatedStrings.isNotEmpty()) {
            while (separatedStrings.isNotEmpty()) {
                val recursiveImage2 = addExtraLineToImage(anchorImage, fontMetricHeightAndPadding)

                val g2d2 = recursiveImage2.createGraphics()

                g2d2.color = Color.BLACK
                g2d2.font = FONT
                g2d2.drawString(separatedStrings.removeFirst(), 0, g2d2.fontMetrics.height + newLineHeight)

                if (separatedStrings.isEmpty()) {
                    g2d2.drawString((key.price * value.value).toString(), anchorImage.width - g2d2.fontMetrics.stringWidth((key.price * value.value).toString()) - 5, g2d2.fontMetrics.height + newLineHeight)
                }

                g2d2.dispose()

                newLineHeight += g2d2.fontMetrics.height + EXTRA_PADDING
                fontMetricHeightAndPadding = g2d2.fontMetrics.height + EXTRA_PADDING

                anchorImage = recursiveImage2
            }
        } else {
            g2d.color = Color.BLACK
            g2d.font = FONT
            g2d.drawString("${key.name} x ${value.value}", 0, g2d.fontMetrics.height + newLineHeight)
            g2d.drawString((key.price * value.value).toString(), anchorImage.width - g2d.fontMetrics.stringWidth((key.price * value.value).toString()) - 5, g2d.fontMetrics.height + newLineHeight)

            newLineHeight += g2d.fontMetrics.height + EXTRA_PADDING
            fontMetricHeightAndPadding = g2d.fontMetrics.height + EXTRA_PADDING
            g2d.dispose()

            anchorImage = recursiveImage
        }
    }

    val addedPaddingForTotal = 10

    var finishingTouchesImage = addExtraLineToImage(anchorImage, getHeightPerLine(TOTAL_FONT) + addedPaddingForTotal)
    val finalImageGraphics = finishingTouchesImage.createGraphics()
    finalImageGraphics.color = Color.BLACK
    finalImageGraphics.font = TOTAL_FONT

    finalImageGraphics.drawString("Tibuok: ₱ ${mutableStates.itemsToCountMap.toMap().entries.map { (key, value) -> key.price * value.value }.reduce { acc, price -> acc + price }}", 0, finalImageGraphics.fontMetrics.height + newLineHeight + addedPaddingForTotal)
    finalImageGraphics.dispose()

    finishingTouchesImage = addExtraLineToImage(finishingTouchesImage, 50)
    val g2d3 = finishingTouchesImage.createGraphics()
    g2d3.color = Color.BLACK
    g2d3.font = FONT
    val thankYouMessage = "Salamat sa inyong pag palit!"
    g2d3.drawString(thankYouMessage, (finishingTouchesImage.width - g2d3.fontMetrics.stringWidth(thankYouMessage)) / 2, (finishingTouchesImage.height - g2d3.fontMetrics.height))
    g2d3.dispose()

    finishingTouchesImage = addExtraLineToImage(finishingTouchesImage, 50)
    val g2d4 = finishingTouchesImage.createGraphics()
    g2d4.color = Color.BLACK
    g2d4.font = FONT
    val cutLine = "-------------------"
    g2d4.drawString(cutLine, (finishingTouchesImage.width - g2d4.fontMetrics.stringWidth(cutLine)) / 2, (finishingTouchesImage.height - g2d4.fontMetrics.height))
    g2d4.dispose()

    // sample preview
    ImageIO.write(finishingTouchesImage,"JPEG", File("./sample.jpg"))

    val printerJob = PrinterJob.getPrinterJob()
    val pageFormat = printerJob.defaultPage()
    pageFormat.orientation = PageFormat.PORTRAIT

    pageFormat.paper.setImageableArea(0.0, 0.0, pageFormat.paper.width, pageFormat.paper.height)

    printerJob.setPrintable(Receipt(finishingTouchesImage), pageFormat)
    printerJob.print()
}