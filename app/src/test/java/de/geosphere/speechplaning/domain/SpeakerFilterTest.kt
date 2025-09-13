package de.geosphere.speechplaning.domain

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Test class for the SpeakerFilter enum, optimized for JaCoCo code coverage, using JUnit 5.
 *
 * This test is intentionally verbose to ensure all compiler-generated
 * bytecode paths of the enum class are executed. This includes the static
 * initializer block (`<clinit>`), the `values()` method's backing array,
 * and the `valueOf()` method.
 */
class SpeakerFilterTest {

    /**
     * This is the most crucial test for JaCoCo coverage.
     * It ensures that all enum constants are initialized and that the
     * compiler-generated `values()` method and its internal array (`$VALUES`)
     * are fully exercised.
     */
    @Test
    fun `test values() method returns all constants in order`() {
        // 1. Define the expected order and content
        val expectedValues = arrayOf(
            SpeakerFilter.ACTIVE,
            SpeakerFilter.INACTIVE,
            SpeakerFilter.ALL
        )

        // 2. Call the `values()` method to get the actual values
        val actualValues = SpeakerFilter.values()

        // 3. Use `assertArrayEquals` to verify the content and order
        // This forces the evaluation of the entire array and its creation.
        assertArrayEquals(
            expectedValues,
            actualValues,
            "The values() array should contain all enum constants in declaration order"
        )

        // 4. Additionally, assert the size to be explicit
        assertEquals(3, actualValues.size, "There should be exactly 3 filter options")
    }

    /**
     * Verifies that the `valueOf()` method correctly converts a string
     * to its corresponding enum constant. This covers the `valueOf` branch.
     */
    @Test
    fun `test valueOf() returns correct enum for valid strings`() {
        assertEquals(SpeakerFilter.ACTIVE, SpeakerFilter.valueOf("ACTIVE"))
        assertEquals(SpeakerFilter.INACTIVE, SpeakerFilter.valueOf("INACTIVE"))
        assertEquals(SpeakerFilter.ALL, SpeakerFilter.valueOf("ALL"))
    }

    /**
     * Ensures that calling `valueOf()` with an invalid string name
     * throws an IllegalArgumentException, which is the expected behavior
     * for the failure path.
     */
    @Test
    fun `test valueOf() throws IllegalArgumentException for invalid string`() {
        // Junit 5 style for expecting exceptions
        assertThrows<IllegalArgumentException> {
            // This call must throw an exception to pass the test
            SpeakerFilter.valueOf("NON_EXISTENT_VALUE")
        }
    }
}
