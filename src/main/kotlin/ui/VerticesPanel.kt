package ui

import kotlinx.coroutines.*
import mateusz.graph.Graph
import java.awt.BorderLayout
import javax.swing.*
import kotlinx.coroutines.swing.Swing
import mateusz.utils.INPUT_DELAY
import mateusz.utils.VERTICES_CHUNK

object VerticesPanel : JPanel() {
    private fun readResolve(): Any = VerticesPanel
    private val vertexToCheckBox: MutableMap<String, JCheckBox> = HashMap()
    private val verticesPanel = JPanel()
    private val scrollPane = JScrollPane(verticesPanel)
    private val searchBar = JTextField()
    private val documentListener = object : javax.swing.event.DocumentListener {
        override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
        override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
        override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = filterVertices(searchBar.text.trim().lowercase())
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        layout = BorderLayout()
        verticesPanel.layout = BoxLayout(verticesPanel, BoxLayout.Y_AXIS)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

        searchBar.document.addDocumentListener(documentListener)

        add(searchBar, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)
    }

    private var filterJob: Job? = null
    private fun filterVertices(filterText: String) {
        filterJob?.cancel()
        filterJob = scope.launch {
            delay(INPUT_DELAY)
            withContext(Dispatchers.Swing) {
                updateVisibility(filterText)
            }
        }
    }

    private fun updateVisibility(filterText: String) {
        vertexToCheckBox.forEach { (vertex, checkBox) ->
            checkBox.isVisible = vertex.lowercase().contains(filterText)
        }
        verticesPanel.revalidate()
        verticesPanel.repaint()
    }


    private fun addVertex(vertex: String, isSelected: Boolean = true) {
        if (vertexToCheckBox.containsKey(vertex)) return

        val checkBox = JCheckBox(vertex)
        checkBox.isSelected = true
        checkBox.addActionListener {
            Graph.setVertexState(vertex, checkBox.isSelected)
        }

        verticesPanel.add(checkBox)
        vertexToCheckBox[vertex] = checkBox

        checkBox.isSelected = isSelected
        checkBox.isVisible = vertex.lowercase().contains(searchBar.text.trim().lowercase())

        revalidate()
        repaint()
    }

    private var updateJob: Job? = null
    fun update() {
        verticesPanel.removeAll()
        vertexToCheckBox.clear()

        revalidate()
        repaint()

        updateJob?.cancel()
        updateJob = scope.launch(Dispatchers.IO) {
            val vertices = Graph.getVertices().toSet()
            val unselectedVertices = Graph.getUnselectedVertices().toSet()

            vertices.chunked(VERTICES_CHUNK).forEach { chunk ->
                withContext(Dispatchers.Swing) {
                    chunk.forEach { vertex ->
                        addVertex(vertex, vertex !in unselectedVertices)
                    }
                }
            }
        }
    }
    
    fun resetSearchBar() {
        searchBar.document.removeDocumentListener(documentListener)
        searchBar.text = ""
        searchBar.document.addDocumentListener(documentListener)


        vertexToCheckBox.forEach { (_, checkBox) ->
            checkBox.isVisible = true
        }
    }
}