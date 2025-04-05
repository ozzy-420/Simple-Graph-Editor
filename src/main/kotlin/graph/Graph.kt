package mateusz.graph

import mateusz.utils.InputAnalyzer
import ui.DisplayArea
import ui.VerticesPanel

object Graph {
    private val vertices = mutableMapOf<String, Vertex>()

    fun getVertices(): Set<String> {
        return vertices.keys
    }

    fun setEdges(edges: Collection<Pair<String, String>>) {
        val oldString = toString()
        val activeStates = vertices.mapValues { it.value.isActive }

        vertices.clear()

        edges.forEach { addEdge(it) }

        activeStates.forEach { (name, isActive) ->
            vertices[name]?.isActive = isActive
        }

        if (oldString == toString()) return

        VerticesPanel.update()
        DisplayArea.update()
    }

    fun addEdge(edge: Pair<String, String>) {
        if (edge.first !in vertices.keys) vertices[edge.first] = Vertex(edge.first)
        if (edge.second !in vertices.keys) vertices[edge.second] = Vertex(edge.second)

        vertices[edge.second]?.let { vertices[edge.first]!!.addNeighbour(it) }
    }

    fun setVertexState(neighbour: String, state: Boolean) {
        vertices[neighbour]!!.isActive = state
        if (InputAnalyzer.isInputValid) DisplayArea.update()
    }

    override fun toString(): String {
        return vertices.values.joinToString("\n")
    }

    fun getUnselectedVertices(): Set<String> {
        return vertices.filter { !it.value.isActive }.keys.toSet()
    }

    fun reset() {
        vertices.values.forEach { it.isActive = true }
    }
}