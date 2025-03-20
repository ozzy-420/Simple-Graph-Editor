package ui

import mateusz.GraphManager
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

object GraphInputPanel : JPanel(BorderLayout()) {
    private fun readResolve(): Any = GraphInputPanel
    private val graphInputArea = JTextArea()

    init {
        graphInputArea.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = updateGraph(e)
        })

        // graphInputArea.lineWrap = true
        add(JScrollPane(graphInputArea), BorderLayout.CENTER)
    }

    private fun updateGraph(e: javax.swing.event.DocumentEvent?) {
        GraphManager.inform(graphInputArea.text.trim(), e)
    }
}