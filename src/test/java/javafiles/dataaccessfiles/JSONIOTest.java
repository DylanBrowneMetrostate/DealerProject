package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.BadCharException;
import javafiles.customexceptions.PathNotFoundException;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafiles.dataaccessfiles.FileIOBuilderTest.getMaps;
import static javafiles.dataaccessfiles.FileIOBuilderTest.testMaps;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

enum MapKey {
    PARTIAL_MAP,
    FULL_MAP,
    EXTRA_MAP;
}

class JSONIOTest {
//    @AfterEach
//    void updatePathsRun() {
//        FileIOBuilderTest.updatePathsRun();
//    }
//
//    /**
//     * Returns a {@link JSONIO} for testing. If retrieving a {@link JSONIO} is expected to fail, it
//     * throws a {@link ReadWriteException} instead. If the {@link JSONIO} is created when it is not
//     * expected to be or vise versa, the test fails.
//     *
//     * @param partialPath The ending part of the file before the extension.
//     * @param folder the folder of the file is in inside jsonIOTests
//     * @param mode The mode of the {@link JSONIO} being created.
//     * @param failExpected If the creation of the {@link JSONIO} is expected to fail.
//     * @return the newly created {@link JSONIO}.
//     * @throws ReadWriteException If the creation of the {@link JSONIO} was expected to fail and did.
//     */
//    private JSONIO getJSONIO(String partialPath, String folder, char mode, boolean failExpected)
//            throws ReadWriteException {
//        folder = "jsonIOTests\\" + folder;
//        return (JSONIO) FileIOBuilderTest.getFileIOForTest(partialPath, folder,".json", mode, failExpected);
//    }
//
//    // Expected: Creation of JSONIO throws an exception.
//    @Test
//    void fileDNERead() {
//        try {
//            JSONIO jsonIO = getJSONIO("DNE_R", "",'r', true);
//            fail(jsonIO.toString());
//        } catch (ReadWriteException e) {
//            PathNotFoundException cause = new PathNotFoundException("Path not found.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    // Expected: Creation of JSONIO does not throw an exception.
//    @Test
//    void fileDNEWrite() {
//        try {
//            getJSONIO("DNE_W","", 'w', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    // Expected: Creation of JSONIO throws an exception.
//    @Test
//    void fileDNEBadChar() {
//        try {
//            JSONIO jsonIO = getJSONIO("DNE_X", "",'x', true);
//            fail(jsonIO.toString());
//        } catch (ReadWriteException e) {
//            BadCharException cause = new BadCharException("Bad character.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    /**
//     * Returns a new instance of the partial map.
//     * </>
//     * Partial map:
//     *      Dealership: ID [12513]
//     *      Vehicle: ID [48934j], Type [suv], Make [Ford], Model [Explorer]
//     *               Price [20123], Acquisition Date [1515354694451]
//     * @return a new instance of the partial map.
//     */
//    private Map<Key, Object> getPartialMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(Key.DEALERSHIP_ID, "12513");
//        map.put(Key.VEHICLE_ID, "48934j");
//        map.put(Key.VEHICLE_TYPE, "suv");
//        map.put(Key.VEHICLE_MANUFACTURER, "Ford");
//        map.put(Key.VEHICLE_MODEL, "Explorer");
//        map.put(Key.VEHICLE_PRICE, 20123L);
//        map.put(Key.VEHICLE_ACQUISITION_DATE, 1515354694451L);
//
//        return map;
//    }
//
//    /**
//     * Returns a new instance of the full map.
//     * </>
//     * Full map:
//     *      Dealership: ID [d_id], Name [d_name], Renting [false], Receiving [true]
//     *      Vehicle: ID [v_id], Type [suv], Make [manufacture], Model [Model]
//     *               Price [10000], Unit [dollar], Rented [false], Acquisition Date [100]
//     * @return a new instance of the full map.
//     */
//    private Map<Key, Object> getFullMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(Key.DEALERSHIP_ID, "d_id");
//        map.put(Key.DEALERSHIP_NAME, "d_name");
//
//        map.put(Key.VEHICLE_ID, "v_id");
//        map.put(Key.VEHICLE_TYPE, "SUV");
//        map.put(Key.VEHICLE_MANUFACTURER, "manufacture");
//        map.put(Key.VEHICLE_MODEL, "Model");
//        map.put(Key.VEHICLE_PRICE, 10000L);
//
//        map.put(Key.DEALERSHIP_RENTING_STATUS, false);
//        map.put(Key.DEALERSHIP_RECEIVING_STATUS, true);
//        map.put(Key.VEHICLE_RENTAL_STATUS, false);
//
//        map.put(Key.VEHICLE_PRICE_UNIT, "dollar");
//        map.put(Key.VEHICLE_ACQUISITION_DATE, 100L);
//
//        assertEquals(Key.values().length - 1, map.size());
//
//        return map;
//    }
//
//    /**
//     * Returns a new instance of the extra map.
//     * </>
//     * Extra map:
//     *      Dealership: ID [12513], Name [l], Renting [false], Receiving [false]
//     *      Vehicle: ID [83883], Type [Sedan], Make [Tesla], Model [Model 3]
//     *               Price [50444], Unit [dollars], Rented [false], Acquisition Date [1515354694451]
//     * @return a new instance of the extra map.
//     */
//    private Map<Key, Object> getExtra() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(Key.VEHICLE_PRICE_UNIT, "dollars");
//        map.put(Key.VEHICLE_PRICE, 50444L);
//        map.put(Key.VEHICLE_MODEL, "Model 3");
//        map.put(Key.DEALERSHIP_RENTING_STATUS, false);
//        map.put(Key.VEHICLE_TYPE, "Sedan");
//        map.put(Key.DEALERSHIP_NAME, "l");
//        map.put(Key.DEALERSHIP_RECEIVING_STATUS, false);
//        map.put(Key.DEALERSHIP_ID, "12513");
//        map.put(Key.VEHICLE_MANUFACTURER, "Tesla");
//        map.put(Key.VEHICLE_ID, "83883");
//        map.put(Key.VEHICLE_RENTAL_STATUS, false);
//        map.put(Key.VEHICLE_ACQUISITION_DATE, 1515354694451L);
//
//        return map;
//    }
//
//    /**
//     * Returns a new instance of partial map, full map, extra map or null.
//     *
//     * @param mapIndex The index of the {@link Map} to be returned.
//     * @return a new instance of the {@link Map}.
//     */
//    private Map<Key, Object> getTarget(MapKey mapIndex) {
//        return switch (mapIndex) {
//            case PARTIAL_MAP -> getPartialMap();
//            case FULL_MAP -> getFullMap();
//            case EXTRA_MAP -> getExtra();
//        };
//    }
//
//    /**
//     * Creates the {@link JSONIO} object from the partialPath provided. Then reads the
//     * inventory and returns the results. If the number of {@link Map}s read is not
//     * mapNum or the {@link JSONIO} fails to be created or read, the test fails.
//     *
//     * @param partialPath The path of the .json file after "test_inventory_" and before ".json".
//     * @param mapNum The number of maps that are expected to be read.
//     * @return the {@link List} of read {@link Map}s.
//     */
//    private List<Map<Key, Object>> readInventory(String partialPath, int mapNum) {
//        JSONIO jsonIO = null;
//        try {
//            jsonIO = getJSONIO(partialPath, "read",'r', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//
//        assertNotNull(jsonIO);
//
//        List<Map<Key, Object>> maps = getMaps(jsonIO, mapNum);
//        assertNotNull(maps);
//
//        return maps;
//    }
//
//    /**
//     * Tests that the file read at the given path is equal to the {@link Map}s calculated
//     * from mapKeys.
//     *
//     * @param partialPath The path of the .json file after "test_inventory_" and before ".json".
//     * @param mapNum The number of maps that are expected to be read.
//     * @param mapKeys The composition and order of the {@link Map}s in the expected output.
//     */
//    private void testGetInventory(String partialPath, int mapNum, MapKey[] mapKeys) {
//        assertEquals(mapNum, mapKeys.length);
//
//        List<Map<Key, Object>> mapsToTest = readInventory(partialPath, mapNum);
//
//        List<Map<Key, Object>> expectedMaps = new ArrayList<>();
//        for (MapKey mapKey : mapKeys) {
//            assertNotNull(mapKey);
//            expectedMaps.add(getTarget(mapKey));
//        }
//
//        testMaps(expectedMaps, mapsToTest);
//    }
//
//    /**
//     * Tests that the {@link Map}s calculated from readMaps are written to the file correctly.
//     * It tests this by writing it to the file and then comparing the {@link List}<{@link Map}>s
//     * read from the file to the {@link Map} written to the file. Also, it ensures that {@link Map}s
//     * with keys and values that should not be written to the file are not written to the file.
//     *
//     * @param partialPath The ending part of the file path before the extension.
//     * @param readMaps An ordered {@link MapKey}[] corresponding to the {@link Map} that the file consists of.
//     * @param badKeys The {@link Map} of bad key-value pairs that should not be written to the file.
//     *                This consists of only a null key or null value in the map.
//     */
//    private void writeInventoryWithBadKeys(String partialPath, MapKey[] readMaps, Map<Key, Object> badKeys) {
//        JSONIO jsonIO = null;
//        try {
//            jsonIO = getJSONIO(partialPath, "write", 'w', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//
//        assertNotNull(jsonIO);
//
//
//        List<Map<Key, Object>> targetLst = new ArrayList<>();
//        for (MapKey mapKey: readMaps) {
//            Map<Key, Object> target = getTarget(mapKey);
//            assertNotNull(target);
//            target.putAll(badKeys);
//            targetLst.add(target);
//        }
//
//        try {
//            jsonIO.writeInventory(targetLst);
//        } catch (ReadWriteException e) {
//            fail(e.toString());
//        }
//
//        for (Map<Key, Object> map : targetLst) {
//            for (Key key : badKeys.keySet()) {
//                map.remove(key);
//            }
//        }
//
//        JSONIO readFile = null;
//        try {
//            readFile = getJSONIO(partialPath, "write", 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//        assertNotNull(readFile);
//
//        List<Map<Key, Object>> maps = getMaps(readFile, readMaps.length);
//        assertNotNull(maps);
//
//        testMaps(targetLst, maps);
//    }
//
//    /**
//     * Tests that the {@link Map}s calculated from readMaps are written to the file correctly.
//     * It tests this by writing it to the file and then comparing the {@link List}<{@link Map}>s
//     * read from the file to the {@link Map} written to the file.
//     *
//     * @param partialPath The ending part of the file path before the extension.
//     * @param readMaps An ordered int[] corresponding to the {@link Map} that the file consists of.
//     *                 case 1 -> partial map, case 2 -> full map, case 3 -> extra map.
//     */
//    private void writeInventoryGood(String partialPath, MapKey[] readMaps) {
//        writeInventoryWithBadKeys(partialPath, readMaps, new HashMap<>());
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryPartialMap() {
//        testGetInventory("min_car", 1, new MapKey[]{MapKey.PARTIAL_MAP});
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryFullMap() {
//        testGetInventory("full_car", 1, new MapKey[]{MapKey.FULL_MAP});
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryMultipleMaps() {
//        MapKey[] mapKeys = new MapKey[]{MapKey.PARTIAL_MAP, MapKey.FULL_MAP, MapKey.EXTRA_MAP};
//        testGetInventory("multi_map", 3, mapKeys);
//    }
//
//    // Expected: Will get a ClassCastException when trying to cast XMLIO to JSONIO.
//    @Test
//    void createJSONIOWithXMLFile() {
//        FileIO fileIO = null;
//        try {
//            fileIO = FileIOBuilderTest.getFileIOForTest("json", "jsonIOTests",".xml", 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//
//        try {
//            JSONIO jsonIO = (JSONIO) fileIO;
//            assertNotNull(jsonIO);
//            fail(jsonIO.toString());
//        } catch (ClassCastException _) {
//
//        }
//    }
//
//    // Expected: JSONIO.readInventory() throws a ReadWriteException.
//    @Test
//    void readWithWriteJSONIO() {
//        char mode = 'w';
//
//        JSONIO writeJSONIO = null;
//        try {
//            writeJSONIO = getJSONIO("read_to_write", "read", mode, false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//        assertNotNull(writeJSONIO);
//
//        try {
//            writeJSONIO.readInventory();
//            fail("Read from write file.");
//        } catch (ReadWriteException e) {
//            assertEquals("Must be mode 'r', not mode '" + mode + "'.", e.getMessage());
//        }
//    }
//
//    // Expected: JSONIO.writeInventory() throws a ReadWriteException.
//    @Test
//    void writeWithReadJSONIO() {
//        char mode = 'r';
//
//        JSONIO readJSONIO = null;
//        try {
//            readJSONIO = getJSONIO("write_to_read", "write", mode, false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//        assertNotNull(readJSONIO);
//
//        List<Map<Key, Object>> maps = new ArrayList<>();
//        maps.add(getTarget(MapKey.EXTRA_MAP));
//
//        try {
//            readJSONIO.writeInventory(maps);
//            fail("Wrote to read file.");
//        } catch (ReadWriteException e) {
//            assertEquals("Must be mode 'w', not mode '" + mode + "'.", e.getMessage());
//        }
//    }
//
//    // Expected: No issues, all Vehicles written.
//    @Test
//    void writeInventoryPartialMap() {
//        writeInventoryGood("min_car", new MapKey[]{MapKey.PARTIAL_MAP});
//    }
//
//    // Expected: No issues, all Vehicles written.
//    @Test
//    void writeInventoryFullMap() {
//        writeInventoryGood("full_car", new MapKey[]{MapKey.FULL_MAP});
//    }
//
//    // Expected: No issues, all Vehicles written.
//    @Test
//    void writeInventoryMultipleMaps() {
//        writeInventoryGood("multi_map", new MapKey[]{MapKey.PARTIAL_MAP, MapKey.FULL_MAP, MapKey.EXTRA_MAP});
//    }
//
//    // Expected: All Vehicles written, null key not written.
//    @Test
//    void writeInventoryNullKey() {
//        Map<Key, Object> badKey = new HashMap<>();
//        badKey.put(null, "Null Key Val");
//
//        writeInventoryWithBadKeys("null_key", new MapKey[]{MapKey.PARTIAL_MAP}, badKey);
//    }
//
//    // Expected: All Vehicles written, null value not written.
//    @Test
//    void writeInventoryNullVal() {
//        Map<Key, Object> badKey = new HashMap<>();
//        badKey.put(Key.VEHICLE_ACQUISITION_DATE, null);
//
//        writeInventoryWithBadKeys("null_val", new MapKey[]{MapKey.PARTIAL_MAP}, badKey);
//    }
}