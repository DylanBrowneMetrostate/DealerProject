package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.BadCharException
import javafiles.customexceptions.PathNotFoundException
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO
import javafiles.dataaccessfiles.FileIOFactoryTest
import org.junit.jupiter.api.AfterAll

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class JSONIOReadTest {
    @AfterEach
    fun updatePathsRun() {
        FileIOFactoryTest.updatePathsRun()
    }

    // Expected: Creation of JSONIO throws an exception.
    @Test
    fun fileDNERead() {
        try {
            val jsonIO = JSONIOTestHelper.getJSONIORead("DNE_R", "DNE", true)
            Assertions.fail<Any>(jsonIO.toString())
        } catch (e: ReadWriteException) {
            val cause = PathNotFoundException("Path not found.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
        }
    }

    // Expected: Creation of JSONIO does not throw an exception.
    @Test
    fun fileDNEWrite() {
        try {
            JSONIOTestHelper.getJSONIOWrite("DNE_W", "DNE", false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }
    }

    // Expected: Creation of JSONIO throws an exception.
    @Test
    fun fileDNEBadChar() {
        try {
            val jsonIO = FileIOFactoryTest.getFileIOForTest("DNE_X", "DNE", ".json", 'x', true) as JSONIORead
            Assertions.fail<Any>(jsonIO.toString())
        } catch (e: ReadWriteException) {
            val cause = BadCharException("Bad character.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
        }
    }

    /**
     * Creates the [JSONIORead] object from the partialPath provided. Then reads the
     * inventory and returns the results. If the number of [Map]s read is not
     * mapNum or the [JSONIORead] fails to be created or read, the test fails.
     *
     * @param partialPath The path of the .json file after "test_inventory_" and before ".json".
     * @param mapNum The number of maps that are expected to be read.
     * @return the [List] of read [Map]s.
     */
    private fun readInventory(partialPath: String, mapNum: Int): List<Map<Key, Any>> {
        var jsonIO: JSONIORead? = null
        try {
            jsonIO = JSONIOTestHelper.getJSONIORead(partialPath, "read", false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }

        Assertions.assertNotNull(jsonIO)

        val maps = FileIOFactoryTest.getMaps(jsonIO, mapNum)
        Assertions.assertNotNull(maps)

        return maps!!
    }

    /**
     * Tests that the file read at the given path is equal to the [Map]s calculated
     * from mapKeys.
     *
     * @param partialPath The path of the .json file after "test_inventory_" and before ".json".
     * @param mapNum The number of maps that are expected to be read.
     * @param mapKeys The composition and order of the [Map]s in the expected output.
     */
    private fun testGetInventory(partialPath: String, mapNum: Int, mapKeys: Array<MapKey>) {
        Assertions.assertEquals(mapNum, mapKeys.size)

        val mapsToTest = readInventory(partialPath, mapNum)

        val expectedMaps: MutableList<Map<Key, Any>> = ArrayList()
        for (mapKey in mapKeys) {
            Assertions.assertNotNull(mapKey)
            expectedMaps.add(JSONIOTestHelper.getTarget(mapKey))
        }

        FileIOFactoryTest.testMaps(expectedMaps, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryPartialMap() {
        testGetInventory("min_car", 1, arrayOf(MapKey.PARTIAL_MAP))
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryFullMap() {
        testGetInventory("full_car", 1, arrayOf(MapKey.FULL_MAP))
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryMultipleMaps() {
        val mapKeys = arrayOf(MapKey.PARTIAL_MAP, MapKey.FULL_MAP, MapKey.EXTRA_MAP)
        testGetInventory("multi_map", 3, mapKeys)
    }

    // Expected: Will get a ClassCastException when trying to cast XMLIO to JSONIO.
    @Test
    fun createJSONIOWithXMLFile() {
        var fileIO: FileIO? = null
        try {
            fileIO = FileIOFactoryTest.getFileIOForTest("json", "jsonIOTests", ".xml", 'r', false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }

        try {
            val jsonIO = fileIO as JSONIORead?
            Assertions.assertNotNull(jsonIO)
            Assertions.fail<Any>(jsonIO.toString())
        } catch (`_`: ClassCastException) {
        }
    }



    /* TODO: Move to a Java to Kotlin Issue test file.

    // Expected: All Vehicles written, null key not written.
    @Test
    void writeInventoryNullKey() {
        Map<Key, Object> badKey = new HashMap<>();
        badKey.put(null, "Null Key Val");

        writeInventoryWithBadKeys("null_key", new MapKey[]{MapKey.PARTIAL_MAP}, badKey);
    }

    // Expected: All Vehicles written, null value not written.
    @Test
    void writeInventoryNullVal() {
        Map<Key, Object> badKey = new HashMap<>();
        badKey.put(Key.VEHICLE_ACQUISITION_DATE, null);

        writeInventoryWithBadKeys("null_val", new MapKey[]{MapKey.PARTIAL_MAP}, badKey);
    }
     */
    companion object {
        @JvmStatic
        @AfterAll
        fun checkAllTestsRun() {
            FileIOFactoryTest.checkAllTestsRun()
        }
    }
}