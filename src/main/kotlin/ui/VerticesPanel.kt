package ui

import mateusz.graph.Graph
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JScrollPane

object VerticesPanel : JScrollPane() {
    private fun readResolve(): Any = VerticesPanel
    private val vertexToCheckBox: MutableMap<String, JCheckBox> = HashMap()
    private val panel = JPanel()

    init {
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        setViewportView(panel)
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_ALWAYS
    }

    private fun tryAddVertex(vertex: String) {
        if (!vertexToCheckBox.containsKey(vertex)) {
            addVertex(vertex)
        }
    }

    private fun addVertex(vertex: String) {
        addVertexSync(vertex)

        revalidate()
        repaint()
    }

    private fun removeVertex(vertex: String) {
        panel.remove(vertexToCheckBox[vertex])
        vertexToCheckBox.remove(vertex)

        revalidate()
        repaint()
    }

    fun update() {
        val vertices = Graph.getVertices()

        vertices.forEach {
            tryAddVertex(it)
        }

        val verticesToRemove = mutableListOf<String>()

        for ((vertex, _) in vertexToCheckBox) {
            if (vertex !in vertices) {
                verticesToRemove.add(vertex)
            }
        }

        verticesToRemove.forEach {
            removeVertex(it)
        }
    }

    fun reset() {
        panel.removeAll()
        vertexToCheckBox.clear()

        revalidate()
        repaint()
    }

    fun addVertexSync(vertex: String) {
        val checkBox = JCheckBox(vertex)
        checkBox.isSelected = true
        checkBox.addActionListener {
            Graph.changeVertexState(vertex, checkBox.isSelected)
        }

        panel.add(checkBox)
        vertexToCheckBox[vertex] = checkBox
    }

    fun updateSync() {
        val vertices = Graph.getVertices()

        vertices.forEach {
            addVertexSync(it)
        }

        revalidate()
        repaint()
    }
}