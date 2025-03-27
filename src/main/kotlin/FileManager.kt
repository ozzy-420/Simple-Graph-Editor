package mateusz

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.DisplayArea
import ui.InputArea
import ui.VerticesPanel
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JPopupMenu

object FileManager {
    fun open() {
        val fileChooser = JFileChooser(File("data"))
        val result = fileChooser.showOpenDialog(null)

        if (result != JFileChooser.APPROVE_OPTION) return

        val file: File = fileChooser.selectedFile


        // Create a new coroutine to read the file
        GlobalScope.launch(Dispatchers.IO) {
            file.bufferedReader().use { reader ->
                var line: String

                // Reset and disable the input panel
                InputArea.removeDocumentListener()
                InputArea.resetInput()

                // Read the file line by line
                while (reader.readLine().also { line = it } != null) {
                    val content = line
                    withContext(Dispatchers.Main) {
                        InputArea.appendInput(content)
                    }
                }
            }

            // Update the graph input panel
            InputArea.syncUpdate()
            VerticesPanel.syncUpdate()
            DisplayArea.syncUpdate()

            InputArea.addDocumentListener()
        }
    }

    fun new() {
        val fileChooser = JFileChooser("data")
        fileChooser.dialogTitle = "Create New File"
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY

        val userSelection = fileChooser.showSaveDialog(null)
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            val fileToSave: File = fileChooser.selectedFile

            if (fileToSave.extension != "txt") {
                fileToSave.renameTo(File("${fileToSave.absolutePath}.txt"))
            }

            if (fileToSave.createNewFile()) {
                println("File created: ${fileToSave.absolutePath}")
            } else {
                val popup = JPopupMenu("File already exists.")
                popup.show(fileChooser, 0, 0)
            }
        }
    }

    fun generateGraph(vertices: Int, file: File) {
        val random = Random()
        val edges = mutableListOf<String>()

        for (i in 1..vertices) {
            val vertex1 = "V${random.nextInt(vertices) + 1}"
            val vertex2 = "V${random.nextInt(vertices) + 1}"
            if (vertex1 != vertex2) {
                edges.add("$vertex1->$vertex2")
            }
        }
        file.writeText(edges.joinToString("\n"))
    }
}