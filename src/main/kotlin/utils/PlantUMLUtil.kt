package mateusz.utils

import kotlinx.coroutines.*
import net.sourceforge.plantuml.SourceStringReader
import java.awt.image.BufferedImage
import java.io.PipedInputStream
import java.io.PipedOutputStream
import javax.imageio.ImageIO

object PlantUMLUtil {
    private val imageCache = mutableMapOf<String, BufferedImage>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentJob: Job? = null

    suspend fun generateImage(source: String): BufferedImage {
        // Check cache
        if (source in imageCache) {
            return imageCache[source]!!
        }

        currentJob?.cancelAndJoin()

        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)

        val os = PipedOutputStream()
        val pis = withContext(Dispatchers.IO) {
            PipedInputStream(os)
        }

        currentJob = scope.launch {
            if (!isActive) return@launch
            try {
                reader.outputImage(os)
            } catch (e: Exception) {
                println("Error while loading image: ${e.message}")
            } finally {
                os.close()
            }
        }

        val image = withContext(Dispatchers.IO) {
            if (!isActive) return@withContext null
            ImageIO.read(pis)
        }

        if (image == null) {
            throw CancellationException("Image loading cancelled")
        }

        withContext(Dispatchers.IO) {
            if (!isActive) return@withContext
            pis.close()
        }

        // Update cache
        if (CACHE_LIMIT > 0 && imageCache.size >= CACHE_LIMIT) {
            imageCache.remove(imageCache.keys.first())
        }
        imageCache[source] = image

        return image
    }
}
