package ui

import java.awt.BorderLayout
import javax.swing.*

class MainFrame : JFrame() {
    init {
        title = "Graph Visualizer"
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        jMenuBar = ui.MenuBar()

        val splitPane1 = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, GraphInputPanel, GraphDisplayPanel)
        splitPane1.dividerLocation = 100

        val splitPane2 = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, VerticesPanel)
        splitPane2.dividerLocation = 650

        add(splitPane2, BorderLayout.CENTER)
    }
}