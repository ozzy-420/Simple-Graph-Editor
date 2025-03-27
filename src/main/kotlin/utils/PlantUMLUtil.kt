package mateusz.utils

import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.LocalTime
import javax.imageio.ImageIO

object PlantUMLUtil {
    var timestamp = LocalTime.now()

    init {
        // Initialize an image because the first image generation takes a long time
        generateImage("")
    }

    fun generateImage(source: String): BufferedImage {
        timestamp = LocalTime.now()

        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)

        val os = ByteArrayOutputStream()



        reader.outputImage(os)



        val result = ImageIO.read(os.toByteArray().inputStream())
        os.close()

        return result
    }
}