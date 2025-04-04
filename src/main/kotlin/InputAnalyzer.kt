package mateusz

import mateusz.graph.Graph
import ui.DisplayArea
import ui.VerticesPanel

fun parseEdge(line: String): Pair<String, String> {
    val first = line.substringBefore("->").trim()
    val second = line.substringAfter("->").trim()
    return first to second
}

object InputAnalyzer {
    fun analyze(newInput: String) {
        val edges = mutableListOf<Pair<String, String>>()
        var index = 0
        var errorless = true

        newInput.split("\n").forEach {
            index++

            if (errorless && it != "" && !it.contains("->")) {
                DisplayArea.showInvalidInput(it, index, DisplayArea.InvalidInput.NOT_AN_EDGE)
                errorless = false
            }

            val edge = parseEdge(it)
            if (edge.first == "" || edge.second == "") {
                if (errorless) {
                    DisplayArea.showInvalidInput(it, index, DisplayArea.InvalidInput.EMPTY_STRING)
                    errorless = false
                }
            } else {
                edges.add(parseEdge(it))
            }
        }

        Graph.setEdges(edges)
        VerticesPanel.update()
    }
}