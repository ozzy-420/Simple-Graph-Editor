package mateusz

import ui.GraphDisplayPanel
import ui.VerticesPanel

object GraphManager {
    fun inform(newInput: String, e: javax.swing.event.DocumentEvent?) {
        validate(newInput)
    }

    private fun validate(newInput: String) {
        val pairs = mutableListOf<Pair<String, String>>()

        newInput.split("\n").forEach {
            if (it != "" && !it.contains("->")) return

            pairs.add(getVerticesFromLine(it))
        }
        Graph.setGraph(pairs)

        VerticesPanel.update()
        GraphDisplayPanel.update()
    }
    private fun getVerticesFromLine(line: String): Pair<String, String> {
        val first = line.split("->")[0]
        return first.trim() to line.removePrefix("$first->").trim()
    }

    fun changeVertexState(vertex: String, targetState: Boolean) {
        Graph.changeVertexState(vertex, targetState)
        GraphDisplayPanel.update()
    }
}