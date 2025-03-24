package ui

import mateusz.TextAnalyzer
import mateusz.graph.Graph
import mateusz.parseEdge
import java.awt.BorderLayout
import java.sql.Time
import java.time.LocalTime
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

object GraphInputPanel : JPanel(BorderLayout()) {
    private fun readResolve(): Any = GraphInputPanel
    private var start = LocalTime.now()

    private val graphInputArea = JTextArea()
    private val stringBuilder = StringBuilder()
    private val documentListener = object : javax.swing.event.DocumentListener {
        override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
        override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
        override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
    }

    init {
        addDocumentListener()

        // graphInputArea.lineWrap = true
        add(JScrollPane(graphInputArea), BorderLayout.CENTER)
    }

    private fun updateGraph(e: javax.swing.event.DocumentEvent?) {
        TextAnalyzer.analyzeChanges(graphInputArea.text.trim(), e)
    }
    fun setInput(input: String) {
        graphInputArea.text = input
    }
    fun resetInput() {
        start = LocalTime.now()

        // Make it so that u cant write to the input area
        graphInputArea.isEditable = false
        VerticesPanel.reset()
        Graph.reset()
        stringBuilder.setLength(0)
    }
    fun addInput(input: String) {
        stringBuilder.append(input)
        stringBuilder.append("\n")

        Graph.addEdge(parseEdge(input))
    }
    fun syncUpdate() {
        graphInputArea.isEditable = true
        graphInputArea.text = stringBuilder.toString()
        VerticesPanel.updateSync()

        GraphDisplayPanel.update()
        //println("Sync update took: ${(LocalTime.now().toSecondOfDay() - start.toSecondOfDay()).toString()}s")
    }

    fun removeDocumentListener() {
        graphInputArea.document.removeDocumentListener(documentListener)
    }
    fun addDocumentListener() {
        graphInputArea.document.addDocumentListener(documentListener)
    }
}