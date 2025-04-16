package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIOFactoryTest
import org.junit.jupiter.api.AfterAll

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach

import java.util.*
import kotlin.collections.ArrayList

class JSONIOWriteTest {
    @AfterEach
    fun updatePathsRun() {
        FileIOFactoryTest.updatePathsRun()
    }

    /**
     * Tests that the [Map]s calculated from readMaps are written to the file correctly.
     * It tests this by writing it to the file and then comparing the [List]<[Map]>s
     * read from the file to the [Map] written to the file. Also, it ensures that [Map]s
     * with keys and values that should not be written to the file are not written to the file.
     *
     * @param partialPath The ending part of the file path before the extension.
     * @param readMaps An ordered [MapKey][] corresponding to the [Map] that the file consists of.
     * @param badKeys The [Map] of bad key-value pairs that should not be written to the file.
     * This consists of only a null key or null value in the map.
     */
    private fun writeInventoryWithBadKeys(partialPath: String, readMaps: Array<MapKey>, badKeys: Map<Key, Any>) {
        var jsonIO: JSONIOWrite? = null
        try {
            jsonIO = JSONIOTestHelper.getJSONIOWrite(partialPath, "write", false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }

        Assertions.assertNotNull(jsonIO)

        val targetLst: MutableList<MutableMap<Key, Any>> = ArrayList()
        for (mapKey in readMaps) {
            val target = JSONIOTestHelper.getTarget(mapKey)
            Assertions.assertNotNull(target)
            target.putAll(badKeys)
            targetLst.add(target)
        }

        try {
            jsonIO!!.writeInventory(targetLst)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.toString())
        }

        for (map in targetLst) {
            for (key in badKeys.keys) {
                map.remove(key)
            }
        }

        var readFile: JSONIORead? = null
        try {
            readFile = JSONIOTestHelper.getJSONIORead(partialPath, "write", false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }
        Assertions.assertNotNull(readFile)

        val maps = FileIOFactoryTest.getMaps(readFile, readMaps.size)
        Assertions.assertNotNull(maps)

        FileIOFactoryTest.testMaps(targetLst, maps)
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

    /**
     * Tests that the [Map]s calculated from readMaps are written to the file correctly.
     * It tests this by writing it to the file and then comparing the [List]<[Map]>s
     * read from the file to the [Map] written to the file.
     *
     * @param partialPath The ending part of the file path before the extension.
     * @param readMaps An ordered int[] corresponding to the [Map] that the file consists of.
     * case 1 -> partial map, case 2 -> full map, case 3 -> extra map.
     */
    private fun writeInventoryGood(partialPath: String, readMaps: Array<MapKey>) {
        writeInventoryWithBadKeys(partialPath, readMaps, EnumMap(Key::class.java))
    }

    // Expected: No issues, all Vehicles written.
    @Test
    fun writeInventoryPartialMap() {
        writeInventoryGood("min_car", arrayOf(MapKey.PARTIAL_MAP))
    }

    // Expected: No issues, all Vehicles written.
    @Test
    fun writeInventoryFullMap() {
        writeInventoryGood("full_car", arrayOf(MapKey.FULL_MAP))
    }

    // Expected: No issues, all Vehicles written.
    @Test
    fun writeInventoryMultipleMaps() {
        writeInventoryGood("multi_map", arrayOf(MapKey.PARTIAL_MAP, MapKey.FULL_MAP, MapKey.EXTRA_MAP))
    }

    @Test
    fun writeToFolder() {
        try {
            JSONIOTestHelper.getJSONIOWrite("not_a_file", "write", true)
            Assertions.fail("Expected to not create JSONIOWrite file, but did.")
        } catch (e: ReadWriteException) {
            Assertions.assertNull(e.cause)
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun checkAllTestsRun() {
            FileIOFactoryTest.checkAllTestsRun()
        }
    }
}