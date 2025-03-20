package mateusz

import ui.MainFrame
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        MainFrame().isVisible = true
    }
}