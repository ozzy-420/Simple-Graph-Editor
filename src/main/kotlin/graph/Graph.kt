package mateusz.graph

import ui.DisplayArea
import ui.VerticesPanel

object Graph {
    private val vertices = mutableMapOf<String, Vertex>()

    fun getVertices(): Collection<String> {
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
        DisplayArea.syncUpdate()
    }

    fun addEdge(edge: Pair<String, String>) {
        if (edge.first !in vertices.keys) vertices[edge.first] = Vertex(edge.first)
        if (edge.second !in vertices.keys) vertices[edge.second] = Vertex(edge.second)

        vertices[edge.second]?.let { vertices[edge.first]!!.addNeighbour(it) }
    }

    fun setVertexState(neighbour: String, state: Boolean) {
        vertices[neighbour]!!.isActive = state
        DisplayArea.syncUpdate()
    }

    override fun toString(): String {
        return vertices.values.joinToString("")
    }

    fun clear() {
        vertices.clear()
    }
}