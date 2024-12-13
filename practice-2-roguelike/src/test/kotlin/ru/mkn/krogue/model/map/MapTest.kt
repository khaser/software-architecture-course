package ru.mkn.krogue.model.map

import org.junit.After
import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.Position
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest {
    private val map = Map(mapOf(Position(0, 0) to Tile.FLOOR), mapOf(Position(0, 0) to listOf(Armor(1))))
    private val testFile = kotlin.io.path.createTempFile()
    private val storedMapFile = Path("src/test/resources/map.json")

    @After
    fun afterAll() {
        testFile.deleteIfExists()
    }

    @Test
    fun saveMapTest() {
        map.saveToFile(testFile)
        assertEquals(storedMapFile.readText(), testFile.readText())
    }

    @Test
    fun loadMapTest() {
        val storedMap = Map.loadFromFile(storedMapFile)
        assertEquals(map, storedMap)
    }

    @Test
    fun storeLoadTest() {
        map.saveToFile(testFile)
        val sm = Map.loadFromFile(testFile)
        assertEquals(map, sm)
    }
}
