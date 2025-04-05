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
import mateusz.utils.INPUT_DELAY
import mateusz.utils.MAX_LOADING_TIME
import mateusz.utils.SHOW_LOADING_ON_FREEZE

object DisplayArea : JPanel(BorderLayout()) {
    private fun readResolve(): Any = DisplayArea

    private var image: BufferedImage? = null

    enum class InvalidInput {
        NOT_AN_EDGE,
        EMPTY_STRING,
        TOO_LARGE_INPUT
    }

    private val invalidInputLabel = JLabel("Invalid input label")
    private val loadingLabel = JLabel("Loading...")

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        image?.let {
            val g2d = g as Graphics2D
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g2d.drawImage(it, 0, 0, width.coerceAtMost(it.width), height.coerceAtMost(it.height), null)
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("DisplayArea"))
    private var updateJob: Job? = null
    private var currentRequestId = 0
    fun update() {
        currentRequestId++
        val requestId = currentRequestId

        if (SHOW_LOADING_ON_FREEZE) showLoading()
        else removeAll()
        updateJob?.cancel()
        val input = Graph.toString()

        updateJob = scope.launch(Dispatchers.IO) {
            if (!isActive || requestId != currentRequestId) return@launch
            delay(INPUT_DELAY)

            val resultImage = withTimeoutOrNull(MAX_LOADING_TIME) {
                PlantUMLUtil.generateImage(input)
            }

            if (!isActive || requestId != currentRequestId) return@launch

            if (resultImage == null) {
                withContext(Dispatchers.Swing) {
                    if (!isActive || requestId != currentRequestId) return@withContext
                    showInvalidInput(input, -1, InvalidInput.TOO_LARGE_INPUT)
                }
            } else {
                image = resultImage
                withContext(Dispatchers.Swing) {
                    if (!isActive || requestId != currentRequestId) return@withContext
                    if (SHOW_LOADING_ON_FREEZE) remove(loadingLabel)

                    revalidate()
                    repaint()
                }
            }
        }
    }

    fun showInvalidInput(input: String, index: Int, type: InvalidInput) {
        removeAll()
        image = null

        if (index == -1) {
            invalidInputLabel.text = "Invalid input: ${type.name}"
        } else {
            invalidInputLabel.text = "Invalid input at line $index: $input: ${type.name}"
        }
        add(invalidInputLabel, BorderLayout.CENTER)

        revalidate()
        repaint()
    }

    private fun showLoading() {
        removeAll()
        image = null

        add(loadingLabel, BorderLayout.CENTER)

        revalidate()
        repaint()
    }
}