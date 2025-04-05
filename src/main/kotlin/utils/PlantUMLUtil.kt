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
    private var time = 0L

    suspend fun generateImage(source: String): BufferedImage {
        if (PRINT_TIMERS) time = System.currentTimeMillis()

        // Check cache first
        imageCache[source]?.let { return it }

        // Cancel previous job if it's still running
        currentJob?.cancelAndJoin()

        val formattedSource = "@startuml\n$source\n@enduml"
        val reader = SourceStringReader(formattedSource)

        val os = PipedOutputStream()
        val pis = withContext(Dispatchers.IO) { PipedInputStream(os) }

        // Start generating the image in a separate coroutine
        currentJob = scope.launch {
            if (!isActive) return@launch

            try {
                reader.outputImage(os)
            } catch (e: Exception) {
                if (isActive) println("Error generating image: ${e.message}")
            } finally {
                os.close()  // Ensure the stream is closed in case of an error or cancellation
            }
        }

        // Read the generated image
        val image = withContext(Dispatchers.IO) {
            if (!isActive) return@withContext null

            ImageIO.read(pis)
        }

        if (image == null) {
            // Return null if the image was not generated
            throw CancellationException("Image loading cancelled")
        }

        // Ensure that the PipedInputStream is closed after reading
        withContext(Dispatchers.IO) {
            pis.close()
        }

        // Cache the generated image if it's valid
        if (CACHE_LIMIT > 0 && imageCache.size >= CACHE_LIMIT) {
            imageCache.remove(imageCache.keys.first())
        }
        imageCache[source] = image

        // Print timer if needed
        if (PRINT_TIMERS) {
            val elapsed = System.currentTimeMillis() - time
            println("Image generated in $elapsed ms")
        }

        return image
    }
}

