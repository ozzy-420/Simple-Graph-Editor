package mateusz

import mateusz.graph.Graph
import ui.GraphDisplayPanel
import ui.VerticesPanel

fun parseEdge(line: String): Pair<String, String> {
    val first = line.split("->")[0]
    return first.trim() to line.removePrefix("$first->").trim()
}

object TextAnalyzer {
    private var oldInput: String = ""

    fun analyzeChanges(newInput: String, e: javax.swing.event.DocumentEvent?) {
        updateGraph(newInput)
    }

    private fun updateGraph(newInput: String) {
        val edges = mutableListOf<Pair<String, String>>()

        newInput.split("\n").forEach {
            if (it != "" && !it.contains("->")) return
            edges.add(parseEdge(it))
        }
        oldInput = newInput

        Graph.setGraph(edges)
        VerticesPanel.update()
        GraphDisplayPanel.update()
    }
}