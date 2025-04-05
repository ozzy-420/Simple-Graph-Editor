package mateusz.utils

import mateusz.graph.Graph
import ui.DisplayArea
import ui.VerticesPanel

fun parseEdge(line: String): Pair<String, String> {
    val first = line.substringBefore("->").trim()
    val second = line.substringAfter("->").trim()
    return first to second
}



object InputAnalyzer {
    var isInputValid = true

    fun analyze(newInput: String) {
        val edges = mutableListOf<Pair<String, String>>()
        var lineNumber = 0
        var isInputValid = true

        newInput.trim().lines().filter { it.isNotEmpty() }.forEach {
            lineNumber++

            if (isInputValid && it != "" && !it.contains("->")) {
                DisplayArea.showInvalidInput(it, lineNumber, DisplayArea.InvalidInput.NOT_AN_EDGE)
                isInputValid = false
            }

            val edge = parseEdge(it)
            if (edge.first == "" || edge.second == "") {
                if (isInputValid) {
                    DisplayArea.showInvalidInput(it, lineNumber, DisplayArea.InvalidInput.EMPTY_STRING)
                    isInputValid = false
                }
            } else {
                edges.add(parseEdge(it))
            }
        }
        this.isInputValid = isInputValid

        if (isInputValid) {
            Graph.setEdges(edges)
            DisplayArea.update()
        }
        VerticesPanel.update()
    }
}