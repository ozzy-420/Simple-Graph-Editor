package mateusz

import mateusz.graph.Graph
import ui.DisplayArea

fun parseEdge(line: String): Pair<String, String> {
    val first = line.split("->")[0]
    return first.trim() to line.removePrefix("$first->").trim()
}

object TextAnalyzer {

    fun updateGraph(newInput: String) {
        val edges = mutableListOf<Pair<String, String>>()
        var index = 0

        newInput.split("\n").forEach {
            index++

            if (it != "" && !it.contains("->")) {
                DisplayArea.showInvalidInput(it, index)
                return
            }
            edges.add(parseEdge(it))
        }

        Graph.setEdges(edges)
    }
}