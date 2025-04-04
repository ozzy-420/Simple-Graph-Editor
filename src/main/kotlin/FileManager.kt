package mateusz

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mateusz.graph.Graph
import ui.DisplayArea
import ui.InputArea
import ui.VerticesPanel
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JPopupMenu

object FileManager {
    private var currentFile: File? = null

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
            currentFile = file

            // Update the graph input panel
            InputArea.syncUpdate()
            VerticesPanel.syncUpdate()
            DisplayArea.syncUpdate()

            InputArea.addDocumentListener()
        }
    }

    fun new() {
        InputArea.clear()
        currentFile = null
    }

    fun save() {
        if (currentFile == null) {
            saveAs()
        } else {
            currentFile?.writeText(InputArea.getInput())
        }
    }

    fun saveAs() {
        val fileChooser = JFileChooser("data")
        fileChooser.dialogTitle = "Save File"
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY

        val userSelection = fileChooser.showSaveDialog(null)
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            val fileToSave: File = fileChooser.selectedFile

            if (fileToSave.extension != "txt") {
                fileToSave.renameTo(File("${fileToSave.absolutePath}.txt"))
            }

            if (fileToSave.createNewFile()) {
                println("File created: ${fileToSave.absolutePath}")
            }

            fileToSave.writeText(InputArea.getInput())
            currentFile = fileToSave
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