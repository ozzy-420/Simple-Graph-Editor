package ui

import kotlinx.coroutines.*
import mateusz.graph.Graph
import mateusz.utils.PlantUMLUtil
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JPanel

object GraphDisplayPanel : JPanel(BorderLayout()) {
    private fun readResolve(): Any = GraphDisplayPanel
    private var updateJob: Job? = null
    private var image: BufferedImage? = null

    init {
        // Initial empty image
        image = PlantUMLUtil.generateImage("")
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        image?.let {

            val g2d = g as Graphics2D
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g2d.drawImage(it, 0, 0, Math.min(width, image!!.width), Math.min(height, image!!.height), null)
        }
    }

    fun update() {
        updateJob?.cancel() // Cancel the previous job if it is active
        val input = Graph.toString()

        updateJob = GlobalScope.launch(Dispatchers.IO) {
            image = PlantUMLUtil.generateImage(input)

            revalidate()
            repaint()
        }
    }
}