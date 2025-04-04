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
import kotlinx.coroutines.swing.Swing

object DisplayArea : JPanel(BorderLayout()) {
    private fun readResolve(): Any = DisplayArea

    private var image: BufferedImage? = null

    enum class InvalidInput {
        NOT_AN_EDGE,
        EMPTY_STRING
    }

    private val invalidInputLabel = JLabel("Invalid input label")
    private val loadingLabel = JLabel("Loading...")

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        image?.let {
            val g2d = g as Graphics2D
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g2d.drawImage(it, 0, 0, Math.min(width, it.width), Math.min(height, it.height), null)
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var updateJob: Job? = null
    fun syncUpdate() {
        showLoading()
        updateJob?.cancel() // Cancel the previous job if it is active
        val input = Graph.toString()

        updateJob = scope.launch(Dispatchers.IO) {
            if (!isActive) return@launch // Check if the job is canceled
            delay(300) // Delay to avoid too many updates

            image = PlantUMLUtil.generateImage(input)

            withContext(Dispatchers.Swing) {
                if (!isActive) return@withContext // Check if the job is canceled

                remove(loadingLabel)
                revalidate()
                repaint()
            }
        }
    }

    fun dispose() {
        scope.cancel()
    }

    fun showInvalidInput(input: String, index: Int, type: InvalidInput) {
        removeAll()

        invalidInputLabel.text = "Invalid at line $index: $input: ${type.name}"
        add(invalidInputLabel, BorderLayout.CENTER)

        revalidate()
        repaint()
    }

    private fun showLoading() {
        removeAll()

        add(loadingLabel, BorderLayout.CENTER)

        revalidate()
        repaint()
    }

    override fun removeAll() {
        super.removeAll()
        image = null
    }
}