import mateusz.utils.InputAnalyzer
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class InputAnalyzerTest {

    @Test
    fun `test valid edge`() {
        val validInput = "A -> B"
        InputAnalyzer.analyze(validInput)
        assertTrue { InputAnalyzer.isInputValid }
    }

    @Test
    fun `test invalid edge with missing arrow`() {
        val invalidInput = "A B"
        InputAnalyzer.analyze(invalidInput)

        assertFalse { InputAnalyzer.isInputValid }
    }

    @Test
    fun `test empty edge`() {
        val invalidInput = "A -> "
        InputAnalyzer.analyze(invalidInput)

        assertFalse { InputAnalyzer.isInputValid }
    }
}