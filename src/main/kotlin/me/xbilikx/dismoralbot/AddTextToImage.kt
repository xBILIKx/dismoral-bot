package me.xbilikx.dismoralbot

import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class AddTextImage(path: String, color: Color,
                   private val mode: Constants.TextMode, size: Int,
                   private val fileName: String) {

    private var bufferedImage: BufferedImage
    private var g: Graphics2D

    init {
        this.bufferedImage = ImageIO.read(File(path))
        g = bufferedImage.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.font = Font("Arial", Font.BOLD, size)
        g.color = color
        g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER)
    }

    private fun uniteImage(img1: BufferedImage, img2: BufferedImage): BufferedImage {
        val bgWidth = img1.width
        val bgHeight = img1.height
        val offset = 4
        val posX = 94
        val posY = 62
        val newImage = BufferedImage(
            bgWidth, bgHeight,
            BufferedImage.TYPE_INT_ARGB
        )
        val g2 = newImage.createGraphics()
        val oldColor = g2.color
        g2.paint = Color.WHITE
        g2.fillRect(0, 0, bgWidth, bgHeight)
        g2.color = oldColor
        g2.drawImage(img1, 0, 0, null)
        g2.drawImage(img2, posX, posY, 754 - offset, 505 - offset, null)
        g2.dispose()
        return newImage
    }

    fun save(): String {
        return saveImage(File(fileName))
    }

    private fun saveImage(file: File): String {
        try {
            ImageIO.write(bufferedImage, "png", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileName
    }

    fun addTextToImage(
        topX: Int, _topY: Int,
        zoneW: Int, zoneH: Int?, text: String
    ) {
        var topY = _topY
        val fontMetrics = g.fontMetrics
        val lineHeight = fontMetrics.height
        val words = text.split(" ").toTypedArray()
        val line = StringBuilder()
        val lines: MutableList<String> = ArrayList()
        for (i in words.indices) {
            if (fontMetrics.stringWidth(line.toString() + words[i]) > zoneW) {
                lines.add(line.toString())
                line.setLength(0)
            }
            line.append(words[i]).append(" ")
        }
        lines.add(line.toString())
        if (zoneH != null) topY += (zoneH / 2) - ((lineHeight * lines.size) / 2)
        for (i in lines.indices) {
            addTextLineToImage(
                fontMetrics, lines[i],
                topX, lineHeight + topY + i * lineHeight,
                zoneW
            )
        }
        g.dispose()
    }

    private fun addTextLineToImage(
        fontMetrics: FontMetrics, text: String,
        topX: Int, topY: Int,
        zoneW: Int
    ) = when(mode){
            Constants.TextMode.LEFT -> g.drawString(text, topX, topY)
            Constants.TextMode.CENTER -> g.drawString(text, topX + (zoneW - fontMetrics.stringWidth(text)) / 2, topY)
            Constants.TextMode.RIGHT -> g.drawString(text, topX + (zoneW - fontMetrics.stringWidth(text)), topY)
        }
}