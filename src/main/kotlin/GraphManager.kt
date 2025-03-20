package mateusz

import ui.GraphDisplayPanel
import ui.VerticesPanel
import java.io.File

object GraphManager {
    private val file = File("src/data/special/opened-graph.txt")

    fun inform(newInput: String, e: javax.swing.event.DocumentEvent?) {
        GraphDisplayPanel.updateGraph(newInput)

        if (isValid(newInput)) file.writeText(newInput)
    }

    private fun isValid(newInput: String): Boolean {
        if (newInput == "") return false

        val vertices = mutableSetOf<String>()

        newInput.split("\n").forEach {
            if (!it.contains("->")) return false

            vertices.add(getVertices(it).first)
            vertices.add(getVertices(it).second)
        }
        VerticesPanel.updateVertices(vertices)

        return true
    }
    fun getVertices(line: String): Pair<String, String> {
        val first = line.split("->")[0]
        return first.trim() to line.removePrefix("$first->").trim()

    }

    fun changeVertexState(vertex: String, isShowing: Boolean) {

    }
}