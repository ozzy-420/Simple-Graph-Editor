package ui

import javax.swing.*

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

        // Add menus to the menu bar
        add(fileMenu)
        add(editMenu)
    }
}