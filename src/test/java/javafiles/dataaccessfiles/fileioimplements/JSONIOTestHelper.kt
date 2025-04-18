package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIOFactoryTest
import org.junit.jupiter.api.Assertions
import java.util.*

enum class MapKey {
    PARTIAL_MAP,
    FULL_MAP,
    EXTRA_MAP
}

object JSONIOTestHelper {
    private val partialMap: MutableMap<Key, Any>
        /**
         * Returns a new instance of the partial map.
         * >
         * Partial map:
         * Dealership: ID [12513]
         * Vehicle: ID [48934j], Type [suv], Make [Ford], Model [Explorer]
         * Price [20123], Acquisition Date [1515354694451]
         * @return a new instance of the partial map.
         */
        get() {
            val map: MutableMap<Key, Any> =
                EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "12513"
            map[Key.VEHICLE_ID] = "48934j"
            map[Key.VEHICLE_TYPE] = "suv"
            map[Key.VEHICLE_MANUFACTURER] = "Ford"
            map[Key.VEHICLE_MODEL] = "Explorer"
            map[Key.VEHICLE_PRICE] = 20123L
            map[Key.VEHICLE_ACQUISITION_DATE] = 1515354694451L

            return map
        }

    private val fullMap: MutableMap<Key, Any>
        /**
         * Returns a new instance of the full map.
         * >
         * Full map:
         * Dealership: ID [d_id], Name [d_name], Renting [false], Receiving [true]
         * Vehicle: ID [v_id], Type [suv], Make [manufacture], Model [Model]
         * Price [10000], Unit [dollar], Rented [false], Acquisition Date [100]
         * @return a new instance of the full map.
         */
        get() {
            val map: MutableMap<Key, Any> = EnumMap(Key::class.java)

            map[Key.DEALERSHIP_ID] = "d_id"
            map[Key.DEALERSHIP_NAME] = "d_name"

            map[Key.VEHICLE_ID] = "v_id"
            map[Key.VEHICLE_TYPE] = "SUV"
            map[Key.VEHICLE_MANUFACTURER] = "manufacture"
            map[Key.VEHICLE_MODEL] = "Model"
            map[Key.VEHICLE_PRICE] = 10000L

            map[Key.DEALERSHIP_RENTING_STATUS] = false
            map[Key.DEALERSHIP_RECEIVING_STATUS] = true
            map[Key.VEHICLE_RENTAL_STATUS] = false

            map[Key.VEHICLE_PRICE_UNIT] = "dollar"
            map[Key.VEHICLE_ACQUISITION_DATE] = 100L

            Assertions.assertEquals(Key.entries.size - 2, map.size)

            return map
        }

    private val extra: MutableMap<Key, Any>
        /**
         * Returns a new instance of the extra map.
         * >
         * Extra map:
         * Dealership: ID [12513], Name [l], Renting [false], Receiving [false]
         * Vehicle: ID [83883], Type [Sedan], Make [Tesla], Model [Model 3]
         * Price [50444], Unit [dollars], Rented [false], Acquisition Date [1515354694451]
         * @return a new instance of the extra map.
         */
        get() {
            val map: MutableMap<Key, Any> = EnumMap(Key::class.java)

            map[Key.VEHICLE_PRICE_UNIT] = "dollars"
            map[Key.VEHICLE_PRICE] = 50444L
            map[Key.VEHICLE_MODEL] = "Model 3"
            map[Key.DEALERSHIP_RENTING_STATUS] = false
            map[Key.VEHICLE_TYPE] = "Sedan"
            map[Key.DEALERSHIP_NAME] = "l"
            map[Key.DEALERSHIP_RECEIVING_STATUS] = false
            map[Key.DEALERSHIP_ID] = "12513"
            map[Key.VEHICLE_MANUFACTURER] = "Tesla"
            map[Key.VEHICLE_ID] = "83883"
            map[Key.VEHICLE_RENTAL_STATUS] = false
            map[Key.VEHICLE_ACQUISITION_DATE] = 1515354694451L

            return map
        }

    /**
     * Returns a [JSONIORead] for testing. If retrieving a [JSONIORead] is expected to fail, it
     * throws a [ReadWriteException] instead. If the [JSONIORead] is created when it is not
     * expected to be or vise versa, the test fails.
     *
     * @param partialPath The ending part of the file before the extension.
     * @param folder the folder of the file is in inside jsonIOTests
     * @param failExpected If the creation of the [JSONIORead] is expected to fail.
     * @return the newly created [JSONIORead].
     * @throws ReadWriteException If the creation of the [JSONIORead] was expected to fail and did.
     */
    @Throws(ReadWriteException::class)
    internal fun getJSONIORead(partialPath: String, folder: String, failExpected: Boolean): JSONIORead {
        var folder = folder
        folder = "jsonIOTests\\$folder"
        return FileIOFactoryTest.getFileIOForTest(partialPath, folder, ".json", 'r', failExpected) as JSONIORead
    }

    /**
     * Returns a [JSONIOWrite] for testing. If retrieving a [JSONIOWrite] is expected to fail, it
     * throws a [ReadWriteException] instead. If the [JSONIOWrite] is created when it is not
     * expected to be or vise versa, the test fails.
     *
     * @param partialPath The ending part of the file before the extension.
     * @param folder the folder of the file is in inside jsonIOTests
     * @param failExpected If the creation of the [JSONIOWrite] is expected to fail.
     * @return the newly created [JSONIOWrite].
     * @throws ReadWriteException If the creation of the [JSONIOWrite] was expected to fail and did.
     */
    @Throws(ReadWriteException::class)
    internal fun getJSONIOWrite(partialPath: String, folder: String, failExpected: Boolean): JSONIOWrite {
        var folder = folder
        folder = "jsonIOTests\\$folder"
        return FileIOFactoryTest.getFileIOForTest(partialPath, folder, ".json", 'w', failExpected) as JSONIOWrite
    }

    /**
     * Returns a new instance of partial map, full map, extra map or null.
     *
     * @param mapIndex The index of the [Map] to be returned.
     * @return a new instance of the [Map].
     */
    fun getTarget(mapIndex: MapKey): MutableMap<Key, Any> {
        return when (mapIndex) {
            MapKey.PARTIAL_MAP -> partialMap
            MapKey.FULL_MAP -> fullMap
            MapKey.EXTRA_MAP -> extra
        }
    }
}