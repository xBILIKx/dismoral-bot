package me.xbilikx.dismoralbot

import me.xbilikx.dismoralbot.Constants.CENTER_TEXT_MODE
import me.xbilikx.dismoralbot.Constants.LEFT_TEXT_MODE
import me.xbilikx.dismoralbot.Constants.RIGHT_TEXT_MODE
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class AddTextImage(path: String, _color: Color,
                   _mode: String, _size: Int,
                   _fileName: String) {
    private var fileName: String = _fileName

    private var color: Color = _color
    private var mode: String = _mode
    private val font = Font("Arial", Font.BOLD, _size)

    private var bufferedImage: BufferedImage


    init {
        this.bufferedImage = ImageIO.read(File(path));
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
        val g = bufferedImage.createGraphics()
        g.color = Color.BLACK
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.font = font
        val fontMetrics = g.fontMetrics
        g.dispose()
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
                lines[i],
                topX, lineHeight + topY + i * lineHeight,
                zoneW
            )
        }
    }

    private fun addTextLineToImage(
        text: String,
        topX: Int, topY: Int,
        zoneW: Int
    ) {
        var topX = topX
        val g = bufferedImage.createGraphics()
        g.color = Color.BLACK
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.font = font
        val fontMetrics = g.fontMetrics
        g.dispose()
        if (mode == LEFT_TEXT_MODE) {
            addTextToImage(text, topX, topY, color)
        } else if (mode == CENTER_TEXT_MODE) {
            topX += (zoneW - fontMetrics.stringWidth(text)) / 2
            addTextToImage(text, topX, topY, color)
        } else if (mode == RIGHT_TEXT_MODE) {
            topX += zoneW - fontMetrics.stringWidth(text)
            addTextToImage(text, topX, topY, color)
        }
    }

    private fun addTextToImage(
        text: String,
        topX: Int, topY: Int,
        color: Color
    ) {
        val g = bufferedImage.createGraphics()
        g.color = color
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER)
        g.font = font
        g.drawString(text, topX, topY)
        g.dispose()
    }
}