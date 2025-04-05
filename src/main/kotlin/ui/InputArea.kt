package ui

import mateusz.utils.InputAnalyzer
import mateusz.graph.Graph
import mateusz.utils.MAX_INPUT_LENGTH
import mateusz.utils.parseEdge
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

object InputArea : JPanel(BorderLayout()) {
    private fun readResolve(): Any = InputArea

    private val graphInputArea = JTextArea()
    private val stringBuilder = StringBuilder()
    private val documentListener = object : javax.swing.event.DocumentListener {
        override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = onInputChanged()
        override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = onInputChanged()
        override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = onInputChanged()
    }

    init {
        graphInputArea.document.addDocumentListener(documentListener)
        add(JScrollPane(graphInputArea), BorderLayout.CENTER)
    }

    fun onInputChanged() {
        graphInputArea.text.substring(0, minOf(graphInputArea.text.length, MAX_INPUT_LENGTH))
        InputAnalyzer.analyze(graphInputArea.text.trim())
    }

    fun clear() {
        graphInputArea.text = ""
        InputAnalyzer.analyze("")
    }

    fun freeze() {
        graphInputArea.isEditable = false
        graphInputArea.document.removeDocumentListener(documentListener)
        stringBuilder.setLength(0)
    }

    fun unfreeze() {
        graphInputArea.text = stringBuilder.toString()
        stringBuilder.setLength(0)
        graphInputArea.isEditable = true
        graphInputArea.document.addDocumentListener(documentListener)

        InputAnalyzer.analyze(graphInputArea.text.trim())
    }

    fun appendInput(input: String) {
        stringBuilder.append(input)
        stringBuilder.append("\n")
    }

    fun getInput(): String {
        return graphInputArea.text
    }
}