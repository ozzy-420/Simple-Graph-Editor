package ui

import kotlinx.coroutines.*
import mateusz.graph.Graph
import java.awt.BorderLayout
import javax.swing.*
import kotlinx.coroutines.swing.Swing

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
            delay(300)
            withContext(Dispatchers.Swing) {
                updateVisibility(filterText)
            }
        }
    }

    private fun updateVisibility(filterText: String) {
        vertexToCheckBox.forEach { (vertex, checkBox) ->
            checkBox.isVisible = vertex.lowercase().contains(filterText)
        }
    }


    private fun addVertex(vertex: String) {
        if (vertexToCheckBox.containsKey(vertex)) return

        val checkBox = JCheckBox(vertex)
        checkBox.isSelected = true
        checkBox.addActionListener {
            Graph.setVertexState(vertex, checkBox.isSelected)
        }

        verticesPanel.add(checkBox)
        vertexToCheckBox[vertex] = checkBox

        checkBox.isVisible = vertex.lowercase().contains(searchBar.text.trim().lowercase())

        revalidate()
        repaint()
    }

    fun update() {
        val vertices = Graph.getVertices()

        vertices.forEach {
            addVertex(it)
        }

        val verticesToRemove = vertexToCheckBox.keys - vertices

        verticesToRemove.forEach {
            verticesPanel.remove(vertexToCheckBox[it])
            vertexToCheckBox.remove(it)
        }

        revalidate()
        repaint()
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