package mateusz.utils

import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object PlantUMLUtil {
    fun generateImage(source: String): BufferedImage {
        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)
        val os = ByteArrayOutputStream()
        reader.outputImage(os)
        return ImageIO.read(os.toByteArray().inputStream())
    }
}