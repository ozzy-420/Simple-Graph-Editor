package ui

import kotlinx.coroutines.*
import mateusz.graph.Graph
import mateusz.utils.PlantUMLUtil
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JLabel
import javax.swing.JPanel

object DisplayArea : JPanel(BorderLayout()) {
    private fun readResolve(): Any = DisplayArea

    public enum class invalidInput {
        NOT_AN_EDGE,
        EMPTY_STRING
    }

    private var updateJob: Job? = null
    private var image: BufferedImage? = null
    private val invalidInputLabel = JLabel("Invalid input label")
    private val loadingLabel = JLabel("Loading...")

    init {
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

    fun syncUpdate() {
        showLoading()
        updateJob?.cancel() // Cancel the previous job if it is active
        val input = Graph.toString()

        updateJob = GlobalScope.launch(Dispatchers.IO) {
            if (!isActive) return@launch // Check if the job is canceled

            image = PlantUMLUtil.generateImage(input)
            remove(loadingLabel)

            withContext(Dispatchers.Main) {
                if (!isActive) return@withContext // Check if the job is canceled
                revalidate()
                repaint()
            }
        }
    }

    fun showInvalidInput(input: String, index: Int, type: invalidInput) {
        removeAll()

        invalidInputLabel.text = "Invalid at line $index: $input: ${type.name}"

        add(invalidInputLabel, BorderLayout.NORTH)

        revalidate()
        repaint()
    }

    private fun showLoading() {
        removeAll()

        add(loadingLabel, BorderLayout.NORTH)

        revalidate()
        repaint()
    }

    override fun removeAll() {
        super.removeAll()
        image = null
    }
}