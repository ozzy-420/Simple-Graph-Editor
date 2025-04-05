package mateusz.utils

import kotlinx.coroutines.*
import mateusz.graph.Graph
import ui.DisplayArea
import ui.InputArea
import ui.VerticesPanel
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane

object FileManager {
    private var currentFile: File? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun open() {
        val fileChooser = JFileChooser(File("data"))
        val result = fileChooser.showOpenDialog(null)

        if (result != JFileChooser.APPROVE_OPTION) return

        val file = fileChooser.selectedFile

        Graph.reset()
        VerticesPanel.resetSearchBar()
        scope.launch {
            try {
                val lines = file.bufferedReader().use { it.readLines() }

                InputArea.freeze()

                lines.forEach { line ->
                    InputArea.appendInput(line)
                }

                DisplayArea.update()
                InputArea.unfreeze()

                currentFile = file
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    JOptionPane.showMessageDialog(null, "Error opening file: ${e.message}")
                }
            }
        }
    }

    fun new() {
        Graph.reset()
        VerticesPanel.resetSearchBar()
        InputArea.clear()
        currentFile = null
    }

    fun save() {
        if (currentFile == null) {
            saveAs()
        } else {
            currentFile!!.writeText(InputArea.getInput())
        }
    }

    fun saveAs() {
        val fileChooser = JFileChooser("data")
        fileChooser.dialogTitle = "Save File"
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY

        val userSelection = fileChooser.showSaveDialog(null)

        if (userSelection != JFileChooser.APPROVE_OPTION) return

        val fileToSave: File = fileChooser.selectedFile

        fileToSave.createNewFile()
        fileToSave.writeText(InputArea.getInput())
        currentFile = fileToSave
    }
}