package de.geosphere.speechplaning.data

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@Suppress("UnusedPrivateMember", "MaxLineLength")
class EventTest {

    private lateinit var mockContext: Context
    private lateinit var mockResources: Resources

    // Beispielhafte Enum-Konstanten für Tests.
    // Du solltest hier deine tatsächlichen Event-Konstanten verwenden.
    // Wenn Event.CONVENTION nicht existiert, werden diese Tests fehlschlagen.
    // Passe diese an deine Event-Enum an.
    private val testEventInstance = Event.CONVENTION // Annahme: CONVENTION existiert
    private val anotherTestEventInstance = Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER

    // Beispielhafte Ressourcen-IDs, die von deinen Event-Konstanten verwendet werden.
    // Diese sollten den Werten in deiner Event-Enum entsprechen./ Annahme
    private val conventionResId = Event.CONVENTION.resourceId // Annahme
    private val circuitAssemblyResId = Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER.resourceId

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockResources = mockk(relaxed = true)
        every { mockContext.resources } returns mockResources
    }

    @Test
    fun `getString valid context and resource ID`() {
        val expectedString = "Expected String Value"
        every { mockContext.getString(testEventInstance.resourceId) } returns expectedString

        val result = testEventInstance.getString(mockContext)

        Assert.assertEquals(expectedString, result)
        verify { mockContext.getString(testEventInstance.resourceId) }
    }

    @Test
    fun `getString resource ID not found`() {
        every { mockContext.getString(testEventInstance.resourceId) } throws
            Resources.NotFoundException("Resource not found")

        Assert.assertThrows(Resources.NotFoundException::class.java) {
            testEventInstance.getString(mockContext)
        }
        verify { mockContext.getString(testEventInstance.resourceId) }
    }

    @Test
    fun `getString null context`() {
        // Diese Testannahme hängt davon ab, ob Event.getString(Context?) nullable Context akzeptiert
        // oder ob die Implementierung intern einen NPE wirft, wenn der Context nicht verwendbar ist.
        // Wenn Context non-null ist (Standard in Kotlin), wäre ein direkter null-Aufruf ein Compile-Fehler.
        // Wir testen hier, ob ein NPE geworfen wird, wenn die Methode aufgerufen wird und intern ein Problem mit dem
        // Context auftritt.
        // Für diesen Test mocken wir, dass der Aufruf von context.getString einen NPE wirft.
        every { mockContext.getString(any()) } throws NullPointerException("Context.getString threw NPE")

        Assert.assertThrows(NullPointerException::class.java) {
            testEventInstance.getString(mockContext)
        }
    }

    @Test
    fun `values returns all enum constants`() {
        val allEvents = Event.values()
        // Stelle sicher, dass alle deine definierten Events enthalten sind.
        // Dies ist ein Beispiel, passe es an deine Enum an.
        Assert.assertTrue(allEvents.contains(Event.CONVENTION))
        Assert.assertTrue(allEvents.contains(Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER))
        // Überprüfe die Gesamtanzahl, wenn du die genaue Anzahl kennst
        Assert.assertEquals(7, allEvents.size)
    }

    @Test
    fun `values returns non empty array`() {
        val allEvents = Event.values()
        Assert.assertTrue("Event.values() should return a non-empty array.", allEvents.isNotEmpty())
    }

    @Test
    fun `valueOf valid enum constant name`() {
        Assert.assertEquals(Event.CONVENTION, Event.valueOf("CONVENTION"))
        Assert.assertEquals(
            Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER,
            Event.valueOf("CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER")
        )
    }

    @Test
    fun `valueOf invalid enum constant name`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Event.valueOf("NON_EXISTENT_EVENT")
        }
    }

    @Test
    fun `valueOf case sensitivity`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Event.valueOf("convention") // Annahme: Enum ist CONVENTION (Großbuchstaben)
        }
    }

    @Test
    fun `valueOf empty string`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            Event.valueOf("")
        }
    }

    @Test
    fun `valueOf null string`() {
        // Behavior for null in valueOf changed across Kotlin versions.
        // Kotlin 1.9 throws IllegalArgumentException for null in valueOf. Older might throw NPE.
        var caught: Throwable? = null
        try {
            Event.valueOf(null as String)
            Assert.fail("Exception was expected for null string.")
        } catch (e: IllegalArgumentException) {
            caught = e
        } catch (e: NullPointerException) {
            caught = e
        }
        Assert.assertTrue(
            "Expected IllegalArgumentException or NullPointerException for null string.",
            caught is IllegalArgumentException || caught is NullPointerException
        )
    }

    @Test
    fun `getEntries returns all enum constants`() {
        // Erfordert Kotlin 1.9+ für Enum.getEntries()
        // oder die 'entries' Eigenschaft in früheren Versionen (ggf. als experimentell markiert)
        val entries = Event.entries
        Assert.assertNotNull(entries)
        Assert.assertTrue(entries.contains(Event.CONVENTION))
        Assert.assertTrue(entries.contains(Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER))
        Assert.assertEquals(7, entries.size)
    }

    @Test
    fun `getEntries iteration`() {
        val entries = Event.entries
        val iteratedEvents = mutableListOf<Event>()
        for (event in entries) {
            iteratedEvents.add(event)
        }
        Assert.assertEquals(Event.values().toList(), iteratedEvents)
    }

    @Test
    fun `getEntries size matches values size`() {
        Assert.assertEquals(Event.values().size, Event.entries.size)
    }

    @Test
    fun `getResourceId returns correct ID for each constant`() {
        // Annahme: Deine Event Enum hat eine 'resourceId' Eigenschaft.
        // Und du hast die IDs oben korrekt initialisiert (conventionResId, etc.)
        Assert.assertEquals(conventionResId, Event.CONVENTION.resourceId)
        Assert.assertEquals(
            circuitAssemblyResId,
            Event.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER.resourceId
        )
    }

    @Test
    fun `getResourceId consistency`() {
        val event = Event.CONVENTION
        val firstCall = event.resourceId
        val secondCall = event.resourceId
        Assert.assertEquals("getResourceId should return consistent values.", firstCall, secondCall)
    }
}