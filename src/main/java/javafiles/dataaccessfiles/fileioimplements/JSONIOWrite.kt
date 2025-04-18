package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException

import org.json.simple.JSONArray
import org.json.simple.JSONObject

import java.io.*
import java.util.EnumMap

/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
internal class JSONIOWrite
/**
 * Creates or opens a JSON file with name filePath in read ('r') or write ('w') mode.
 * Read mode allows the reading, but not writing of files, write mode allows for the
 * writing, but not reading of files.
 *
 * @throws ReadWriteException Thrown if the mode is an invalid char.
 */
    constructor(override val file: File) : FileIOWriter {
    /**
     * Takes a Map<Key></Key>, Object> of data with the same keys as keys
     * and converts it to a JSONObject and returns it.
     *
     * @param data The Map of items to be ordered in a JSONObject with the keys for
     * the data the same as the keys in keys.
     * @return The newly created JSONObject
     */
    private fun makeJSONObject(data: Map<Key, Any>): JSONObject {
        val jObj = JSONObject()
        for (key in Key.entries) {
            val dataPoint = data[key] ?: continue
            jObj[key.key] = dataPoint
        }
        return jObj
    }

    /**
     * Takes a List of Maps to write to the file stored in this object.
     *
     * @param maps List of Maps to write to a file.
     * @throws ReadWriteException Thrown if not in write ('w') mode.
     */
    @Throws(ReadWriteException::class)
    override fun writeInventory(maps: Map<Map<Key, Any>, List<Map<Key, Any>>>) {
        val jArray = JSONArray()

        maps.forEach { dealerCarPair->
            if (dealerCarPair.value.isEmpty()) {
                val dummyCar = EnumMap(dealerCarPair.key)
                Key.DUMMY_VEHICLE.putValid(dummyCar, true)

                jArray.add(makeJSONObject(dummyCar))
            } else {
                dealerCarPair.value.forEach { carMap ->
                    val fullCarMap = EnumMap(carMap)
                    fullCarMap.putAll(dealerCarPair.key)

                    jArray.add(makeJSONObject(fullCarMap))
                }
            }
        }

        /*
        for (dealer in maps.keys) {
            val jObj = makeJSONObject(carData)
            jArray.add(jObj)
        }
         */

        val fileWriter: Writer
        val jFile = JSONObject()
        jFile["car_inventory"] = jArray
        try {
            fileWriter = FileWriter(file)
            jFile.writeJSONString(fileWriter)
            fileWriter.close()
        } catch (e: IOException) {
            throw(ReadWriteException(e))
        }
    }
}