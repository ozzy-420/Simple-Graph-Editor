import kotlinx.coroutines.runBlocking
import mateusz.utils.PlantUMLUtil
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PlantUMLUtilTest {

    @Test
    fun `test generate image from valid graph input`(): Unit = runBlocking {
        val graphSource = "A -> B\nB -> C\nC -> A"
        val image: BufferedImage = PlantUMLUtil.generateImage(graphSource)

        assertNotNull(image, "Image should not be null")
    }

    @Test
    fun `test generate image with invalid graph input`(): Unit = runBlocking {
        val invalidGraphSource = "A -> B\nC -> D\nE -> X"
        try {
            val image: BufferedImage = PlantUMLUtil.generateImage(invalidGraphSource)
            assertNotNull(image, "Image should not be null even for invalid graph")
        } catch (e: Exception) {

            assert(false)
        }
    }

    @Test
    fun `test cache for repeated graph input`(): Unit = runBlocking {
        val graphSource = "A -> B\nB -> C\nC -> A"

        val firstImage = PlantUMLUtil.generateImage(graphSource)
        val secondImage = PlantUMLUtil.generateImage(graphSource)

        assertEquals(firstImage, secondImage)
    }
}
