package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

import java.io.*

import java.util.EnumMap
import kotlin.collections.ArrayList

/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
internal class JSONIORead
/**
 * Creates or opens a JSON file with name filePath in read ('r') or write ('w') mode.
 * Read mode allows the reading, but not writing of files, write mode allows for the
 * writing, but not reading of files.
 *
 * @param file The full path of the file to be opened or created.
 * @throws ReadWriteException Thrown if the mode is an invalid char.
 */
    constructor(override val file: File) : FileIOReader {
    /**
     * Takes a JSONObject and creates and returns a Map. Fills the Map with the
     * data from the JSONObject with the same keys as keys. If any keys are absent,
     * null is returned.
     *
     * @param jObj The JSONObject that data is being extracted from.
     */
    private fun readJSONObject(jObj: JSONObject): Map<Key, Any> {
        val map: MutableMap<Key, Any> = EnumMap(Key::class.java)

        for (key in Key.entries) {
            val keyStr = key.key

            val dataPoint = jObj[keyStr] ?: continue
            map[key] = dataPoint
        }
        return map
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return A List of Map<Key></Key>, Object>s that correspond to the
     * JSONArray of data stored in the JSON file for this object.
     * The Map has data in the same keys as keys.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    @Throws(ReadWriteException::class)
    override fun readInventory(): List<Map<Key, Any>> {
        val parser = JSONParser()
        val fileReader: Reader
        val jFile: JSONObject
        try {
            fileReader = FileReader(file)
            jFile = parser.parse(fileReader) as JSONObject
            fileReader.close()
        } catch (e: Exception) {
            when(e) {
                is ParseException, is IOException -> {return ArrayList()}
                else -> throw e
            }
        }

        val jArray = jFile["car_inventory"] as JSONArray

        val maps: MutableList<Map<Key, Any>> = ArrayList()

        for (jObj in jArray) {
            val map = readJSONObject(jObj as JSONObject)
            maps.add(map)
        }

        return maps
    }
}