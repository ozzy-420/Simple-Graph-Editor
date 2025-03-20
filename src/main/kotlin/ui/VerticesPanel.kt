package ui

import mateusz.Graph
import mateusz.GraphManager
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
        checkBox.addActionListener {
            GraphManager.changeVertexState(vertex, checkBox.isSelected)
            println("Changed $vertex state to: ${checkBox.isSelected}")
        }

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

    fun update() {
        val vertices = Graph.getVertices()

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