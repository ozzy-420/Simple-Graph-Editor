package ui

import mateusz.TextAnalyzer
import mateusz.graph.Graph
import mateusz.parseEdge
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

object InputArea : JPanel(BorderLayout()) {
    private fun readResolve(): Any = InputArea

    private val graphInputArea = JTextArea()
    private val stringBuilder = StringBuilder()
    private val documentListener = object : javax.swing.event.DocumentListener {
        override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = TextAnalyzer.analyze(graphInputArea.text.trim())
        override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = TextAnalyzer.analyze(graphInputArea.text.trim())
        override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = TextAnalyzer.analyze(graphInputArea.text.trim())
    }

    init {
        addDocumentListener()

        // graphInputArea.lineWrap = true
        add(JScrollPane(graphInputArea), BorderLayout.CENTER)
    }

    fun clear() {
        graphInputArea.text = ""
        TextAnalyzer.analyze(graphInputArea.text.trim())
    }

    fun resetInput() {

        // Make it so that u cant write to the input area
        graphInputArea.isEditable = false
        VerticesPanel.reset()
        Graph.clear()
        stringBuilder.setLength(0)
    }

    fun appendInput(input: String) {
        stringBuilder.append(input)
        stringBuilder.append("\n")

        Graph.addEdge(parseEdge(input))
    }

    fun getInput(): String {
        return graphInputArea.text
    }

    fun syncUpdate() {
        graphInputArea.text = stringBuilder.toString()
        graphInputArea.isEditable = true
    }

    fun removeDocumentListener() {
        graphInputArea.document.removeDocumentListener(documentListener)
    }

    fun addDocumentListener() {
        graphInputArea.document.addDocumentListener(documentListener)
    }
}