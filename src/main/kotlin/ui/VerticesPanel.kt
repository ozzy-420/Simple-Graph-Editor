package ui

import kotlinx.coroutines.*
import mateusz.graph.Graph
import java.awt.BorderLayout
import javax.swing.*

object VerticesPanel : JPanel() {
    private fun readResolve(): Any = VerticesPanel
    private val vertexToCheckBox: MutableMap<String, JCheckBox> = HashMap()
    private val panel = JPanel()
    private val scrollPane = JScrollPane(panel)
    private val searchBar = JTextField()
    private val documentListener = object : javax.swing.event.DocumentListener {
        override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
        override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
        override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
    }


    init {
        layout = BorderLayout()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

        searchBar.document.addDocumentListener(documentListener)

        add(searchBar, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)
    }

    private var filterJob: Job? = null
    private fun filterVertices(filterText: String) {
        // Create coroutine and job to filter the vertices
        filterJob?.cancel() // Cancel the previous job if it is active

        filterJob = GlobalScope.launch(Dispatchers.IO) {
            delay(300) // Delay to avoid too many updates
            if (!isActive) return@launch // Check if the job is canceled

            SwingUtilities.invokeLater {
                vertexToCheckBox.forEach { (vertex, checkBox) ->
                    checkBox.isVisible = vertex.lowercase().contains(filterText)
                }
                panel.revalidate()
                panel.repaint()
            }
        }
    }
    suspend fun filterVerticesSus(filterText: String) {
        SwingUtilities.invokeLater {
            vertexToCheckBox.forEach { (vertex, checkBox) ->
                checkBox.isVisible = vertex.lowercase().contains(filterText)
            }
            panel.revalidate()
            panel.repaint()
        }
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

    private fun addVertexSync(vertex: String) {
        val checkBox = JCheckBox(vertex)
        checkBox.isSelected = true
        checkBox.addActionListener {
            Graph.setVertexState(vertex, checkBox.isSelected)
        }

        panel.add(checkBox)
        vertexToCheckBox[vertex] = checkBox
    }

    fun syncUpdate() {
        val vertices = Graph.getVertices()

        vertices.forEach {
            addVertexSync(it)
        }

        revalidate()
        repaint()
    }
}