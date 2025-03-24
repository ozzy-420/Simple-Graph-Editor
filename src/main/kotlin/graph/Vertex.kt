package mateusz.graph

class Vertex(val name: String) {
    private val neighbours = mutableSetOf<Vertex>()
    var isActive: Boolean = true

    fun addNeighbour(neighbour: Vertex) {
        neighbours.add(neighbour)
    }

    fun removeNeighbour(neighbour: Vertex) {
        neighbours.remove(neighbour)
    }

    override fun toString(): String {
        if (!isActive) return ""
        val activeNeighbours = neighbours.filter { it.isActive }
        if (activeNeighbours.isEmpty()) return ""

        return "\n$name->" + activeNeighbours.joinToString("\n$name->") { vertex: Vertex -> vertex.name }
    }
}