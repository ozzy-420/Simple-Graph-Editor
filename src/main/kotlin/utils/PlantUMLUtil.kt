package mateusz.utils

import kotlinx.coroutines.*
import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.PipedInputStream
import java.io.PipedOutputStream
import javax.imageio.ImageIO
import java.awt.Image

object PlantUMLUtil {

    private const val CACHE_LIMIT = 1
    private const val WIDTH = 1920
    private const val HEIGHT = 1080

    private val imageCache = mutableMapOf<String, BufferedImage>()
    private var currentJob: Job? = null

    suspend fun generateImage(source: String): BufferedImage {
        // Check cache
        if (source in imageCache) {
            return imageCache[source]!!
        }

        // Cancel the current job if it is active
        currentJob?.cancelAndJoin()

        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)

        val os = PipedOutputStream()
        val pis = PipedInputStream(os)

        currentJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                reader.outputImage(os)
            } catch (e: Exception) {
                println("Przerwano wczytywanie obrazu")
            } finally {
                os.close()
            }
        }

        // Odczyt obrazu też w korutynie
        val originalImage = withContext(Dispatchers.IO) {
            ImageIO.read(pis)
        }
        pis.close()

        // Resize image
        val resizedImage = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)
        val graphics = resizedImage.createGraphics()
        graphics.drawImage(originalImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH), 0, 0, null)
        graphics.dispose()

        // Update cache
        if (CACHE_LIMIT > 0 && imageCache.size >= CACHE_LIMIT) {
            imageCache.remove(imageCache.keys.first())
        }
        imageCache[source] = resizedImage

        return resizedImage
    }
}