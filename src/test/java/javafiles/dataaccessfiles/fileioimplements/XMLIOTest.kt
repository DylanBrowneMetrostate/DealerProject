package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.*
import javafiles.dataaccessfiles.FileIOFactoryTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import javax.xml.parsers.DocumentBuilder
import kotlin.collections.ArrayList

internal class XMLIOTest {
    @AfterEach
    fun updatePathsRun() {
        FileIOFactoryTest.updatePathsRun()
    }

    /**
     * Returns a [XMLIO] for testing. If retrieving a [XMLIO] is expected to fail, it
     * throws a [ReadWriteException] instead. If the [XMLIO] is created when it is not
     * expected to be or vise versa, the test fails.
     *
     * @param partialPath The ending part of the file before the extension
     * @param mode The mode of the [XMLIO] being created.
     * @param failExpected If the creation of the [XMLIO] is expected to fail.
     * @return the newly created [XMLIO].
     * @throws ReadWriteException If the creation of the [XMLIO] was expected to fail and did.
     */
    @Throws(ReadWriteException::class)
    private fun getXMLIO(partialPath: String, mode: Char, failExpected: Boolean): XMLIO {
        return (FileIOFactoryTest.getFileIOForTest(partialPath, "xmlIOTests", ".xml", mode, failExpected) as XMLIO)
    }

    // Expected: Creation of XMLIO throws an exception.
    @Test
    fun fileDNERead() {
        try {
            val xmlIO = getXMLIO("DNE_R", 'r', true)
            Assertions.fail<Any>(xmlIO.toString())
        } catch (e: ReadWriteException) {
            val cause = PathNotFoundException("Path not found.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
        }
    }

    // Expected: Creation of XMLIO throws an exception.
    @Test
    fun fileDNEWrite() {
        try {
            val xmlIO = getXMLIO("DNE_W", 'w', true)
            Assertions.fail<Any>(xmlIO.toString())
        } catch (e: ReadWriteException) {
            val cause = BadExtensionException("Bad extension.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
        }
    }

    // Expected: Creation of XMLIO throws an exception.
    @Test
    fun fileDNEBadChar() {
        try {
            val xmlIO = getXMLIO("DNE_X", 'x', true)
            Assertions.fail<Any>(xmlIO.toString())
        } catch (e: ReadWriteException) {
            val cause = BadCharException("Bad character.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
        }
    }

    private val minMap: MutableMap<Key, Any>
        /**
         * Creates and returns a new [Map] of an example Vehicle with the minimum possible
         * requirements to be created.
         *
         * @return The created [Map].
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "d_id"
            map[Key.VEHICLE_ID] = "v_id0"
            map[Key.VEHICLE_TYPE] = "pickup"
            map[Key.VEHICLE_MODEL] = "model"
            map[Key.VEHICLE_PRICE] = 17000L

            var neededKeys = 0
            for (key in Key.entries) {
                if (key.needed) {
                    neededKeys++
                }
            }
            Assertions.assertEquals(neededKeys, map.size)

            return map
        }

    private val fullMap: MutableMap<Key, Any>
        /**
         * Creates and returns a new [Map] of an example Vehicle with all the possible tags
         * to be created.
         *
         * @return The created [Map].
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "d_id"
            map[Key.DEALERSHIP_NAME] = "name"
            map[Key.VEHICLE_ID] = "v_id1"
            map[Key.VEHICLE_TYPE] = "sports car"
            map[Key.VEHICLE_MANUFACTURER] = "make"
            map[Key.VEHICLE_MODEL] = "model"
            map[Key.VEHICLE_PRICE] = 17000L
            map[Key.VEHICLE_PRICE_UNIT] = "pounds"

            return map
        }

    private val extraMap: MutableMap<Key, Any>
        /**
         * Creates and returns a new [Map] of an example Vehicle with some tags
         * to be created.
         *
         * @return The created [Map].
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "d_id"
            map[Key.DEALERSHIP_NAME] = "name"
            map[Key.VEHICLE_ID] = "v_id2"
            map[Key.VEHICLE_TYPE] = "suv"
            map[Key.VEHICLE_MODEL] = "model"
            map[Key.VEHICLE_PRICE] = 16500L
            map[Key.VEHICLE_PRICE_UNIT] = "dollars"

            return map
        }

    private val minMapNewDealer: MutableMap<Key, Any>
        /**
         * Creates and returns a new [Map] of an example Vehicle with the minimum possible requirements
         * and that belongs to a different Dealership than the other Vehicles.
         *
         * @return The created [Map].
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "d_id2"
            map[Key.VEHICLE_ID] = "v_id3"
            map[Key.VEHICLE_TYPE] = "sports car"
            map[Key.VEHICLE_MODEL] = "model2"
            map[Key.VEHICLE_PRICE] = 18000L

            var neededKeys = 0
            for (key in Key.entries) {
                if (key.needed) {
                    neededKeys++
                }
            }
            Assertions.assertEquals(neededKeys, map.size)

            return map
        }

    private val badPriceMap: Map<Key, Any>
        /**
         * Creates and returns a new [Map] of an example Vehicle with an issue in the price
         * tag in the .xml file to be created.
         *
         * @return The created [Map].
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            val reason =
                "Invalid number format on " + XMLKey.PRICE.xmlName + ". [" + 45.2 + "]"
            val cause = NumberFormatException(reason)

            map[Key.DEALERSHIP_ID] = "d_id2"
            map[Key.VEHICLE_ID] = "v_id4"
            map[Key.VEHICLE_TYPE] = "SUV"
            map[Key.VEHICLE_MODEL] = "model3"
            map[Key.REASON_FOR_ERROR] = ReadWriteException(cause)

            return map
        }

    /**
     * Creates the [XMLIO] object from the partialPath provided. Then reads the
     * inventory and returns the results. If the number of [Map]s read is not
     * mapNum or the [XMLIO] fails to be created or read, the test fails.
     *
     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
     * @param mapNum The number of maps that are expected to be read.
     * @return the [List] of read [Map]s.
     */
    private fun readInventory(partialPath: String, mapNum: Int): List<Map<Key, Any>> {
        var xmlIO: XMLIO? = null
        try {
            xmlIO = getXMLIO(partialPath, 'r', false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e.message)
        }

        Assertions.assertNotNull(xmlIO)

        val maps = FileIOFactoryTest.getMaps(xmlIO, mapNum)
        Assertions.assertNotNull(maps)

        return maps!!
    }

    /**
     * Tests that a .xml can be interpreted as the state:
     * <AnyTagName>
     * <Dealer>
     * <Min Map Vehicle Info>
    </Min></Dealer> *
    </AnyTagName> *
     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
     */
    private fun runDealershipStateMin(partialPath: String) {
        val mapsToTest = readInventory(partialPath, 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(minMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    /**
     * Tests that a .xml can be interpreted as the state:
     * <AnyTagName>
     * <Dealer>
     * <Min Map Vehicle Info>
     * <Full Map Vehicle Info>
     * <Extra Map Vehicle Info>
    </Extra></Full></Min></Dealer> *
    </AnyTagName> *
     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
     */
    private fun runDealershipStateFullDealership(partialPath: String) {
        val mapsToTest = readInventory(partialPath, 3)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()

        val minMap = minMap
        minMap[Key.DEALERSHIP_NAME] = "name"
        mapsToTestAgainst.add(minMap)
        mapsToTestAgainst.add(fullMap)
        mapsToTestAgainst.add(extraMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    /**
     * Tests that a .xml can be interpreted as the state:
     * <AnyTagName>
     * <Dealer>
     * <Min Map Vehicle Info>
     * <Full Map Vehicle Info>
     * <Extra Map Vehicle Info>
    </Extra></Full></Min></Dealer> *
     * <Dealer2>
     * <Min Map New Dealer Vehicle Info>
    </Min></Dealer2> *
    </AnyTagName> *
     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
     */
    private fun runDealershipStateAllVehicles(partialPath: String) {
        val mapsToTest = readInventory(partialPath, 4)

        val mapsToTestAgainst: MutableList<MutableMap<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(minMap)
        mapsToTestAgainst.add(fullMap)
        mapsToTestAgainst.add(extraMap)
        mapsToTestAgainst.add(minMapNewDealer)

        mapsToTestAgainst[0][Key.DEALERSHIP_NAME] = "name"
        mapsToTestAgainst[mapsToTestAgainst.size - 1][Key.DEALERSHIP_NAME] = "name2"

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    /**
     * Puts a [ReadWriteException] for the [Key].REASON_FOR_ERROR key in the [Map].
     * The rest of the arguments are to try and format the error message the same.
     *
     * @param xmlKey The duplicate [XMLKey] in the message.
     * @param firstFound The name of the first found value with the given [XMLKey].
     * @param lastVal The value of the last found value with the given [XMLKey].
     * @param map The [Map] that is being added to.
     */
    private fun putReasonDuplicate(xmlKey: XMLKey, firstFound: String, lastVal: String, map: MutableMap<Key, Any>) {
        var reason = "Key " + xmlKey.xmlName + " already has a value and ["
        reason += "$firstFound] != [$lastVal]."
        val cause = DuplicateKeyException(reason)
        map[XMLKey.REASON.key] = ReadWriteException(cause)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryMinMap() {
        runDealershipStateMin("min_car")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryFullMap() {
        val mapsToTest = readInventory("full_car", 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(fullMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryMultipleMaps() {
        val mapsToTest = readInventory("multi_car", 3)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()

        val minMap = minMap
        minMap[Key.DEALERSHIP_NAME] = "name" // dealership has name this time
        mapsToTestAgainst.add(minMap)
        mapsToTestAgainst.add(fullMap)
        mapsToTestAgainst.add(extraMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryMultipleDealers() {
        val mapsToTest = readInventory("multi_dealer", 2)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(fullMap)
        mapsToTestAgainst.add(minMapNewDealer)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryDealerInDealerSameID() {
        runDealershipStateMin("equal_dealer_in_dealer")
    }

    // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
    @Test
    fun readInventoryDealerInDealerDifferentID() {
        val mapsToTest = readInventory("not_equal_dealer_in_dealer", 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        val fullMap = fullMap

        val key = XMLKey.D_ID
        val firstFound = "d_id2" // Child nodes are fully evaluated before attributes of node,

        // thus d_id2 is found first.
        putReasonDuplicate(key, firstFound, (fullMap[Key.DEALERSHIP_ID] as String?)!!, fullMap)
        fullMap[Key.DEALERSHIP_ID] = firstFound

        mapsToTestAgainst.add(fullMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: Key.REASON_FOR_ERROR is added to maps, marking last duplicate for each.
    @Test
    fun readInventoryMultipleDealerInsideDealer() {
        val mapsToTest = readInventory("not_equal_two_dealer_in_dealer", 4)

        val mapsToTestAgainst: MutableList<MutableMap<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(minMap)
        mapsToTestAgainst.add(fullMap)
        mapsToTestAgainst.add(extraMap)
        mapsToTestAgainst.add(minMapNewDealer)

        mapsToTestAgainst[0][Key.DEALERSHIP_NAME] = "name"
        for (i in 0..3) {
            val map = mapsToTestAgainst[i]
            map[Key.DEALERSHIP_NAME] = "name2"
            putReasonDuplicate(XMLKey.D_ID, "d_id", "d_id2", map)
            map[Key.DEALERSHIP_ID] = "d_id"
        }

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: Key.REASON_FOR_ERROR is added to maps, marking last duplicate.
    @Test
    fun readInventoryDealerInDealerErrorAfterVehicle() {
        val mapsToTest = readInventory("after_car_error", 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        val map = fullMap
        putReasonDuplicate(XMLKey.D_NAME, "name", "name2", map)
        mapsToTestAgainst.add(map)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: Key.REASON_FOR_ERROR is added to map, marking last invalid Long.
    @Test
    fun readInventoryBadNumber() {
        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(badPriceMap)

        val mapsToTest = readInventory("bad_price", 1)
        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    /**
     * Takes a [String] corresponding to a xml file that cannot be parsed by [DocumentBuilder].
     * The [DocumentBuilder] will print a fatal error to the screen. This function is used
     * to show that the fatal error is expected for the (fatally flawed) input file and that
     * the test has not been failed.
     *
     * @param partialPath The name of the file in xmlIOTests after "test_" and before the extension.
     */
    private fun fatalErrorExpected(partialPath: String) {
        var xmlIO: XMLIO? = null
        try {
            xmlIO = getXMLIO(partialPath, 'r', false)
        } catch (e: ReadWriteException) {
            Assertions.fail<Any>(e)
        }

        try {
            println("Expected [fatal error] (from DocumentBuilder) for file ending in $partialPath.xml")
            xmlIO!!.readInventory()
            Assertions.fail<Any>(xmlIO.toString())
        } catch (e: ReadWriteException) {
            println("End expected [fatal error] for file ending in $partialPath.xml.\n")
            Assertions.assertInstanceOf(Exception::class.java, e.cause)
        }
    }

    // Expected: XMLIO.readInventory() throws a ReadWriteException.
    @Test
    fun readInventoryInvalidXML() {
        fatalErrorExpected("missing_data")

    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryNoOverallDealersRoot() {
        runDealershipStateFullDealership("dealer_as_root")
    }

    // Expected: XMLIO.readInventory() throws a ReadWriteException.
    @Test
    fun readInventoryTwoDealersRoots() {
        fatalErrorExpected("no_root_tag")
    }

    // Expected: No data found.
    @Test
    fun readInventoryNoDealerTags() {
        val mapsToTest = readInventory("no_dealers", 0)

        val mapsToTestAgainst: List<Map<Key, Any>> = ArrayList()

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No data found.
    @Test
    fun readInventoryNoVehicleTags() {
        val mapsToTest = readInventory("no_cars", 0)

        val mapsToTestAgainst: List<Map<Key, Any>> = ArrayList()

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryLowerCaseTags() {
        runDealershipStateFullDealership("lowercase_tags")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryAllAttributes() {
        runDealershipStateAllVehicles("all_vals_in_atr")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryAllTags() {
        runDealershipStateAllVehicles("all_vals_in_tags")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryExtraWhitespace() {
        runDealershipStateAllVehicles("extra_whitespace")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryAllTagsAndAttributes() {
        runDealershipStateAllVehicles("equal_dupe_atr_tag")
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryTagsWithinTags() {
        runDealershipStateAllVehicles("tags_in_tags")
    }

    // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
    @Test
    fun readInventoryVehicleWithinVehicle() {
        val mapsToTest = readInventory("car_in_car", 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        val fullMap = fullMap
        mapsToTestAgainst.add(fullMap)
        Key.VEHICLE_ID.putValid(fullMap, "v_id3")

        putReasonDuplicate(XMLKey.V_ID, "v_id3", "v_id1", fullMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: Vehicle outside of Dealership is discarded. Other Vehicle is read fine.
    @Test
    fun readInventoryVehicleOutSideDealership() {
        val mapsToTest = readInventory("car_outside_dealer", 1)

        val mapsToTestAgainst: MutableList<Map<Key, Any>> = ArrayList()
        mapsToTestAgainst.add(extraMap)

        FileIOFactoryTest.testMaps(mapsToTestAgainst, mapsToTest)
    }

    // Expected: No issues, all Vehicles read.
    @Test
    fun readInventoryWithSomeGibberishTagsAndAttributes() {
        runDealershipStateFullDealership("nonsense_tags")
    }

    // Expected: FileIOBuilder.buildNewFileIO() throws exception.
    @Test
    fun writeInventoryThroughFileIOBuilder() {
        val xmlIO: XMLIO
        try {
            xmlIO = getXMLIO("write", 'w', true)
            Assertions.fail<Any>(xmlIO.toString())
        } catch (e: ReadWriteException) {
            val cause = BadExtensionException("Bad extension.")
            FileIOFactoryTest.assertSameCauseType(ReadWriteException(cause), e)
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