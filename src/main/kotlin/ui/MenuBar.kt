package ui

import javax.swing.*
import java.io.File
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class MenuBar : JMenuBar() {
    init {
        // Create a menu
        val fileMenu = JMenu("File")
        val editMenu = JMenu("Edit")

        // Create menu items
        val newItem = JMenuItem("New")
        val openItem = JMenuItem("Open")
        val saveItem = JMenuItem("Save")
        val exitItem = JMenuItem("Exit")

        // Add menu items to the menu
        fileMenu.add(newItem)
        fileMenu.add(openItem)
        fileMenu.add(saveItem)
        fileMenu.addSeparator() // Add a separator line
        fileMenu.add(exitItem)

        openItem.addActionListener {
            val fileChooser = JFileChooser(File("data"))
            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile: File = fileChooser.selectedFile

                GlobalScope.launch(Dispatchers.IO) {
                    selectedFile.bufferedReader().use { reader ->
                        var line: String
                        GraphInputPanel.removeDocumentListener()
                        GraphInputPanel.resetInput()

                        while (reader.readLine().also { line = it } != null) {
                            val content = line
                            withContext(Dispatchers.Main) {
                                GraphInputPanel.addInput(content)
                            }
                        }
                    }
                    GraphInputPanel.syncUpdate()
                    GraphInputPanel.addDocumentListener()
                }
            }
        }

        // Add menus to the menu bar
        add(fileMenu)
        add(editMenu)
    }
}