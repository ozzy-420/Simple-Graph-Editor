package ui

import mateusz.GraphManager
import java.awt.event.ActionListener
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JPanel

object VerticesPanel : JPanel() {
    private fun readResolve(): Any = VerticesPanel
    private val vertexToCheckBox: MutableMap<String, JCheckBox> = HashMap()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }

    private fun tryAddVertex(vertex: String) {
        if (!vertexToCheckBox.containsKey(vertex)) {
            addVertex(vertex)
        }
    }

    private fun addVertex(vertex: String) {
        val checkBox = JCheckBox(vertex)
        checkBox.isSelected = true
        checkBox.addActionListener { ActionListener { GraphManager.changeVertexState(vertex, checkBox.isSelected) } }

        add(checkBox)
        vertexToCheckBox[vertex] = checkBox

        revalidate()
        repaint()
    }

    private fun removeVertex(vertex: String) {
        remove(vertexToCheckBox[vertex])

        revalidate()
        repaint()
    }

    fun updateVertices(vertices: Collection<String>) {
        vertices.forEach {
            tryAddVertex(it)
        }

        for ((vertex, _) in vertexToCheckBox) {
            if (vertex !in vertices) {
                removeVertex(vertex)
            }
        }
    }
}