package ui

import mateusz.Graph
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
        graphLabel.icon = ImageIcon(PlantUMLUtil.generateImage(""))
    }

    fun update() {
        graphLabel.icon = ImageIcon(PlantUMLUtil.generateImage(Graph.toString()))
    }
}