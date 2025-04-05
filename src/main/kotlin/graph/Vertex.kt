package mateusz.graph

class Vertex(private val name: String) {
    private val neighbours = mutableSetOf<Vertex>()
    var isActive: Boolean = true

    fun addNeighbour(neighbour: Vertex) {
        neighbours.add(neighbour)
    }

    override fun toString(): String {
        if (!isActive) return ""
        val activeNeighbours = neighbours.filter { it.isActive }
        if (activeNeighbours.isEmpty()) return ""

        return "$name->" + activeNeighbours.joinToString("\n$name->") { vertex: Vertex -> vertex.name }
    }
}