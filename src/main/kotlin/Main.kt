package mateusz

import ui.MainFrame
import java.io.File
import java.util.*
import javax.swing.SwingUtilities

fun main() {
    //generateGraph(100, File("data/test100.txt"))
    //generateGraph(10000, File("data/test10000.txt"))

    SwingUtilities.invokeLater {
        MainFrame().isVisible = true
    }
}

fun generateGraph(vertices: Int, file: File) {
    val random = Random()
    val edges = mutableListOf<String>()

    for (i in 1..vertices+1) {
        val vertex1 = "V${random.nextInt(vertices) + 1}"
        val vertex2 = "V${random.nextInt(vertices) + 1}"
        if (vertex1 != vertex2) {
            edges.add("$vertex1->$vertex2")
        }
    }

    file.writeText(edges.joinToString("\n"))
}