package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
class JSONIO extends FileIO {
    /**
     * Creates or opens a JSON file with name filePath in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files.
     *
     * @param filePath The full path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char.
     */
    public JSONIO(String filePath, char mode) throws ReadWriteException {
        super(filePath, mode);
    }

    /**
     * Takes a {@link JSONObject} and creates and returns a {@link Map}. Fills the {@link Map}
     * with the data from the {@link JSONObject} with the keys equal to {@link Key}.getKey().
     *
     * @param jObj The {@link JSONObject}  that data is being extracted from.
     * @return A {@link Map} of all the key-value pairs found in the object.
     */
    private Map<Key, Object> readJSONObject(JSONObject jObj) {
        Map<Key, Object> map = new HashMap<>();

        for (Key key : Key.values()) {
            Object dataPoint = jObj.get(key.getKey());
            if (dataPoint == null) {continue;}
            if (!key.putValid(map, dataPoint)) {
                ReadWriteException exception = new ReadWriteException("Unknown");
                Key.REASON_FOR_ERROR.putValid(map, exception);
            }

        }
        return map;
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return A {@link List} of {@link Map}<{@link Key}, {@link Object}>s that correspond
     *         to the {@link JSONArray} of data stored in the JSON file for this object.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    public List<Map<Key, Object>> readInventory() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }
        JSONParser parser = new JSONParser();
        Reader fileReader;
        JSONObject jFile;
        JSONArray jArray;
        try {
            fileReader = new FileReader(file);
            jFile = (JSONObject) parser.parse(fileReader);
            fileReader.close();
        } catch (ParseException | IOException e) {
            throw new ReadWriteException(e);
        }

        jArray = (JSONArray)jFile.get("car_inventory");

        List< Map<Key, Object> > maps = new ArrayList<>();

        for (Object jObj : jArray) {
            Map<Key, Object> map = readJSONObject((JSONObject) jObj);
            maps.add(map);
        }

        return maps;
    }

    /**
     * Takes a {@link Map}<{@link Key}, {@link Object}> of data and converts it to a {@link JSONObject}
     * where {@link Key}.getKey() corresponds to the key in the {@link JSONObject}.
     *
     * @param data The {@link Map} of items to be ordered in a {@link JSONObject} where
     *             {@link Key}.getKey() corresponds to the key in the created {@link JSONObject}.
     * @return The newly created {@link JSONObject}.
     */
    private JSONObject makeJSONObject(Map<Key, Object> data) {
        JSONObject jObj = new JSONObject();
        for (Key key : Key.values()) {
            Object dataPoint = data.get(key);
            if (dataPoint == null) {continue;}
            jObj.put(key.getKey(), dataPoint);
        }
        return jObj;
    }

    /**
     * Takes a {@link List} of {@link Map}s to write to the file stored in this object.
     *
     * @param data {@link List} of {@link Map}s to write to a file.
     * @throws ReadWriteException Thrown if not in write ('w') mode.
     */
    public void writeInventory(List<Map<Key, Object>> data) throws ReadWriteException {
        if (mode != 'w') {
            throw new ReadWriteException("Must be mode 'w', not mode '" + mode + "'.");
        }

        JSONArray jArray = new JSONArray();
        for (Map<Key, Object> carData : data) {
            JSONObject jObj = makeJSONObject(carData);
            jArray.add(jObj);
        }

        Writer fileWriter;
        JSONObject jFile = new JSONObject();
        jFile.put("car_inventory", jArray);
        try {
            fileWriter = new FileWriter(file);
            jFile.writeJSONString(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}