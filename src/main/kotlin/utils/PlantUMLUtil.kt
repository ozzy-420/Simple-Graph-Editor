package mateusz.utils

import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object PlantUMLUtil {

    private const val CACHE_LIMIT = 0
    private val imageCache = mutableMapOf<String, BufferedImage>()

    fun generateImage(source: String): BufferedImage {
        // Check cache
        if (source in imageCache) {
            return imageCache[source]!!
        }

        // Generate image
        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)

        val os = ByteArrayOutputStream()

        reader.outputImage(os)

        val result = ImageIO.read(os.toByteArray().inputStream())
        os.close()

        // Update cache
        if (CACHE_LIMIT > 0 && imageCache.size >= CACHE_LIMIT) {
            imageCache.remove(imageCache.keys.first())
        }
        imageCache[source] = result

        return result
    }
}