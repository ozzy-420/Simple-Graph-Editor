package mateusz

import ui.MainFrame
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        MainFrame().isVisible = true
    }
}

fun generateGraph(vertices: Int, path: String = "data/graph.txt") {
    val random = Random()
    val edges = mutableListOf<String>()

    for (i in 1..vertices) {
        val vertex1 = "V${random.nextInt(vertices) + 1}"
        val vertex2 = "V${random.nextInt(vertices) + 1}"
        if (vertex1 != vertex2) {
            edges.add("$vertex1->$vertex2")
        }
    }
    val filePath = Paths.get(path)
    Files.write(filePath, edges.joinToString("\n").toByteArray())
}

