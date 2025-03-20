package ui

import mateusz.utils.PlantUMLUtil
import java.awt.BorderLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

object GraphDisplayPanel : JPanel(BorderLayout()) {
    private fun readResolve(): Any = GraphDisplayPanel
    private val graphLabel = JLabel()

    init {
        add(graphLabel, BorderLayout.CENTER)
        updateGraph("")
    }

    fun updateGraph(graph: String) {
        graphLabel.icon = ImageIcon(PlantUMLUtil.generateImage(graph))
    }
}