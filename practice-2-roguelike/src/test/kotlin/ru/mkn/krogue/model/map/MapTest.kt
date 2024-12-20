package ru.mkn.krogue.model.map

import org.hexworks.zircon.api.data.Size
import org.junit.After
import ru.mkn.krogue.model.Armor
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest {
    private val map =
        Map(
            Size.create(1, 1),
            mutableMapOf(Position(0, 0) to Tile.FLOOR),
            mutableMapOf(
                Position(0, 0) to
                    mutableListOf(
                        Armor.Jacket,
                    ),
            ),
        )
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
