package ui

import javax.swing.*
import mateusz.utils.FileManager


class MenuBar : JMenuBar() {
    init {
        // Create a menu
        val fileMenu = JMenu("File")
        //val optionsMenu = JMenu("Optons")

        // Create menu items
        val newItem = JMenuItem("New")
        val openItem = JMenuItem("Open")
        val saveItem = JMenuItem("Save")
        val saveAsItem = JMenuItem("Save as...")

        //val changeCachingItem = JMenuItem("Change caching limit (10)")

        // Add menu items to the menu
        fileMenu.add(newItem)
        fileMenu.add(openItem)
        fileMenu.add(saveItem)
        fileMenu.add(saveAsItem)

        // Add functionality to the menu items
        openItem.addActionListener { FileManager.open() }
        newItem.addActionListener { FileManager.new() }
        saveItem.addActionListener { FileManager.save() }
        saveAsItem.addActionListener { FileManager.saveAs() }

        // Add menus to the menu bar
        add(fileMenu)

        //add(optionsMenu)
    }
}