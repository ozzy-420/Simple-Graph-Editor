package mateusz.graph

import ui.GraphDisplayPanel

object Graph {
    private val vertices = mutableMapOf<String, Vertex>()

    fun getVertices(): Collection<String> {
        return vertices.keys
    }

    fun setGraph(edges: Collection<Pair<String, String>>) {
        val activeStates = vertices.mapValues { it.value.isActive }

        vertices.clear()

        edges.forEach { addEdge(it) }

        activeStates.forEach { (name, isActive) ->
            vertices[name]?.isActive = isActive
        }
    }

    fun addEdge(edge: Pair<String, String>) {
        if (edge.first !in vertices.keys) vertices[edge.first] = Vertex(edge.first)
        if (edge.second !in vertices.keys) vertices[edge.second] = Vertex(edge.second)

        vertices[edge.second]?.let { vertices[edge.first]!!.addNeighbour(it) }
    }

    fun changeVertexState(neighbour: String, state: Boolean) {
        vertices[neighbour]!!.isActive = state
        GraphDisplayPanel.update()
    }

    override fun toString(): String {
        return vertices.values.joinToString("")
    }

    fun reset() {
        vertices.clear()
    }
}