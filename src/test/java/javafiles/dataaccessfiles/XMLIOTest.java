package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafiles.Key.*;
import static javafiles.dataaccessfiles.FileIOBuilderTest.*;
import static org.junit.jupiter.api.Assertions.*;

class XMLIOTest {
//    @AfterEach
//    void updatePathsRun() {
//        FileIOBuilderTest.updatePathsRun();
//    }
//
//    /**
//     * Returns a {@link XMLIO} for testing. If retrieving a {@link XMLIO} is expected to fail, it
//     * throws a {@link ReadWriteException} instead. If the {@link XMLIO} is created when it is not
//     * expected to be or vise versa, the test fails.
//     *
//     * @param partialPath The ending part of the file before the extension
//     * @param mode The mode of the {@link XMLIO} being created.
//     * @param failExpected If the creation of the {@link XMLIO} is expected to fail.
//     * @return the newly created {@link XMLIO}.
//     * @throws ReadWriteException If the creation of the {@link XMLIO} was expected to fail and did.
//     */
//    private XMLIO getXMLIO(String partialPath, char mode, boolean failExpected) throws ReadWriteException {
//        return (XMLIO) FileIOBuilderTest.getFileIOForTest(partialPath, "xmlIOTests", ".xml", mode, failExpected);
//    }
//
//    // Expected: Creation of XMLIO throws an exception.
//    @Test
//    void fileDNERead() {
//        try {
//            XMLIO xmlIO = getXMLIO("DNE_R", 'r', true);
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            PathNotFoundException cause = new PathNotFoundException("Path not found.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    // Expected: Creation of XMLIO throws an exception.
//    @Test
//    void fileDNEWrite() {
//        try {
//            XMLIO xmlIO = getXMLIO("DNE_W", 'w', true);
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            BadExtensionException cause = new BadExtensionException("Bad extension.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    // Expected: Creation of XMLIO throws an exception.
//    @Test
//    void fileDNEBadChar() {
//        try {
//            XMLIO xmlIO = getXMLIO("DNE_X", 'x', true);
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            BadCharException cause = new BadCharException("Bad character.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    /**
//     * Creates and returns a new {@link Map} of an example Vehicle with the minimum possible
//     * requirements to be created.
//     *
//     * @return The created {@link Map}.
//     */
//    private Map<Key, Object> getMinMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(DEALERSHIP_ID, "d_id");
//        map.put(VEHICLE_ID, "v_id0");
//        map.put(VEHICLE_TYPE, "pickup");
//        map.put(VEHICLE_MODEL, "model");
//        map.put(VEHICLE_PRICE, 17000L);
//
//        int neededKeys = 0;
//        for (Key key: values()) {
//            if (key.getNeeded()) {
//                neededKeys++;
//            }
//        }
//        assertEquals(neededKeys, map.size());
//
//        return map;
//    }
//
//    /**
//     * Creates and returns a new {@link Map} of an example Vehicle with all the possible tags
//     * to be created.
//     *
//     * @return The created {@link Map}.
//     */
//    private Map<Key, Object> getFullMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(DEALERSHIP_ID, "d_id");
//        map.put(DEALERSHIP_NAME, "name");
//        map.put(VEHICLE_ID, "v_id1");
//        map.put(VEHICLE_TYPE, "sports car");
//        map.put(VEHICLE_MANUFACTURER, "make");
//        map.put(VEHICLE_MODEL, "model");
//        map.put(VEHICLE_PRICE, 17000L);
//        map.put(VEHICLE_PRICE_UNIT, "pounds");
//
//        return map;
//    }
//
//    /**
//     * Creates and returns a new {@link Map} of an example Vehicle with some tags
//     * to be created.
//     *
//     * @return The created {@link Map}.
//     */
//    private Map<Key, Object> getExtraMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(DEALERSHIP_ID, "d_id");
//        map.put(DEALERSHIP_NAME, "name");
//        map.put(VEHICLE_ID, "v_id2");
//        map.put(VEHICLE_TYPE, "suv");
//        map.put(VEHICLE_MODEL, "model");
//        map.put(VEHICLE_PRICE, 16500L);
//        map.put(VEHICLE_PRICE_UNIT, "dollars");
//
//        return map;
//    }
//
//    /**
//     * Creates and returns a new {@link Map} of an example Vehicle with the minimum possible requirements
//     * and that belongs to a different Dealership than the other Vehicles.
//     *
//     * @return The created {@link Map}.
//     */
//    private Map<Key, Object> getMinMapNewDealer() {
//        Map<Key, Object> map = new HashMap<>();
//
//        map.put(DEALERSHIP_ID, "d_id2");
//        map.put(VEHICLE_ID, "v_id3");
//        map.put(VEHICLE_TYPE, "sports car");
//        map.put(VEHICLE_MODEL, "model2");
//        map.put(VEHICLE_PRICE, 18000L);
//
//        int neededKeys = 0;
//        for (Key key: values()) {
//            if (key.getNeeded()) {
//                neededKeys++;
//            }
//        }
//        assertEquals(neededKeys, map.size());
//
//        return map;
//    }
//
//    /**
//     * Creates and returns a new {@link Map} of an example Vehicle with an issue in the price
//     * tag in the .xml file to be created.
//     *
//     * @return The created {@link Map}.
//     */
//    private Map<Key, Object> getBadPriceMap() {
//        Map<Key, Object> map = new HashMap<>();
//
//        String reason = "Invalid number format on " + XMLKey.PRICE.getName() + ". [" + 45.2 + "]";
//        NumberFormatException cause = new NumberFormatException(reason);
//
//        map.put(DEALERSHIP_ID, "d_id2");
//        map.put(VEHICLE_ID, "v_id4");
//        map.put(VEHICLE_TYPE, "SUV");
//        map.put(VEHICLE_MODEL, "model3");
//        map.put(REASON_FOR_ERROR, new ReadWriteException(cause));
//
//        return map;
//    }
//
//    /**
//     * Creates the {@link XMLIO} object from the partialPath provided. Then reads the
//     * inventory and returns the results. If the number of {@link Map}s read is not
//     * mapNum or the {@link XMLIO} fails to be created or read, the test fails.
//     *
//     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
//     * @param mapNum The number of maps that are expected to be read.
//     * @return the {@link List} of read {@link Map}s.
//     */
//    private List<Map<Key, Object>> readInventory(String partialPath, int mapNum) {
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = getXMLIO(partialPath, 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e.getMessage());
//        }
//
//        assertNotNull(xmlIO);
//
//        List<Map<Key, Object>> maps = getMaps(xmlIO, mapNum);
//        assertNotNull(maps);
//
//        return maps;
//    }
//
//    /**
//     * Tests that a .xml can be interpreted as the state:
//     * <AnyTagName>
//     *     <Dealer>
//     *         <Min Map Vehicle Info>
//     *     </Dealer>
//     * </AnyTagName>
//     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
//     */
//    private void runDealershipStateMin(String partialPath) {
//        List<Map<Key, Object>> mapsToTest = readInventory(partialPath, 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getMinMap());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    /**
//     * Tests that a .xml can be interpreted as the state:
//     * <AnyTagName>
//     *     <Dealer>
//     *         <Min Map Vehicle Info>
//     *         <Full Map Vehicle Info>
//     *         <Extra Map Vehicle Info>
//     *     </Dealer>
//     * </AnyTagName>
//     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
//     */
//    private void runDealershipStateFullDealership(String partialPath) {
//        List<Map<Key, Object>> mapsToTest = readInventory(partialPath, 3);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//
//        Map<Key, Object> minMap = getMinMap();
//        minMap.put(DEALERSHIP_NAME, "name");
//        mapsToTestAgainst.add(minMap);
//        mapsToTestAgainst.add(getFullMap());
//        mapsToTestAgainst.add(getExtraMap());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    /**
//     * Tests that a .xml can be interpreted as the state:
//     * <AnyTagName>
//     *     <Dealer>
//     *         <Min Map Vehicle Info>
//     *         <Full Map Vehicle Info>
//     *         <Extra Map Vehicle Info>
//     *     </Dealer>
//     *     <Dealer2>
//     *         <Min Map New Dealer Vehicle Info>
//     *     </Dealer2>
//     * </AnyTagName>
//     * @param partialPath The path of the .xml file after "test_inventory_" and before ".xml".
//     */
//    private void runDealershipStateAllVehicles(String partialPath) {
//        List<Map<Key, Object>> mapsToTest = readInventory(partialPath, 4);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getMinMap());
//        mapsToTestAgainst.add(getFullMap());
//        mapsToTestAgainst.add(getExtraMap());
//        mapsToTestAgainst.add(getMinMapNewDealer());
//
//        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
//        mapsToTestAgainst.getLast().put(DEALERSHIP_NAME, "name2");
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    /**
//     * Puts a {@link ReadWriteException} for the {@link Key}.REASON_FOR_ERROR key in the {@link Map}.
//     * The rest of the arguments are to try and format the error message the same.
//     *
//     * @param xmlKey The duplicate {@link  XMLKey} in the message.
//     * @param firstFound The name of the first found value with the given {@link XMLKey}.
//     * @param lastVal The value of the last found value with the given {@link XMLKey}.
//     * @param map The {@link Map} that is being added to.
//     */
//    private void putReasonDuplicate(XMLKey xmlKey, String firstFound, String lastVal, Map<Key, Object> map) {
//        String reason = "Key " + xmlKey.getName() + " already has a value and [";
//        reason += firstFound  + "] != [" + lastVal + "].";
//        DuplicateKeyException cause = new DuplicateKeyException(reason);
//        map.put(REASON_FOR_ERROR, new ReadWriteException(cause));
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryMinMap() {
//        runDealershipStateMin("min_car");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryFullMap() {
//        List<Map<Key, Object>> mapsToTest = readInventory("full_car", 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getFullMap());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryMultipleMaps() {
//        List<Map<Key, Object>> mapsToTest = readInventory("multi_car", 3);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//
//        Map<Key, Object> minMap = getMinMap();
//        minMap.put(DEALERSHIP_NAME, "name"); // dealership has name this time
//        mapsToTestAgainst.add(minMap);
//        mapsToTestAgainst.add(getFullMap());
//        mapsToTestAgainst.add(getExtraMap());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryMultipleDealers() {
//        List<Map<Key, Object>> mapsToTest = readInventory("multi_dealer", 2);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getFullMap());
//        mapsToTestAgainst.add(getMinMapNewDealer());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryDealerInDealerSameID() {
//        runDealershipStateMin("equal_dealer_in_dealer");
//    }
//
//    // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
//    @Test
//    void readInventoryDealerInDealerDifferentID() {
//        List<Map<Key, Object>> mapsToTest = readInventory("not_equal_dealer_in_dealer", 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        Map<Key, Object> fullMap = getFullMap();
//
//        XMLKey key = XMLKey.D_ID;
//        String firstFound = "d_id2"; // Child nodes are fully evaluated before attributes of node,
//                                     // thus d_id2 is found first.
//
//        putReasonDuplicate(key, firstFound, (String) fullMap.get(DEALERSHIP_ID), fullMap);
//        fullMap.put(XMLKey.D_ID.getKey(), firstFound);
//
//        mapsToTestAgainst.add(fullMap);
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: Key.REASON_FOR_ERROR is added to maps, marking last duplicate for each.
//    @Test
//    void readInventoryMultipleDealerInsideDealer() {
//        List<Map<Key, Object>> mapsToTest = readInventory("not_equal_two_dealer_in_dealer", 4);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getMinMap());
//        mapsToTestAgainst.add(getFullMap());
//        mapsToTestAgainst.add(getExtraMap());
//        mapsToTestAgainst.add(getMinMapNewDealer());
//
//        mapsToTestAgainst.getFirst().put(DEALERSHIP_NAME, "name");
//        for (int i = 0; i < 4; i++) {
//            Map<Key, Object> map = mapsToTestAgainst.get(i);
//            map.put(DEALERSHIP_NAME, "name2");
//            putReasonDuplicate(XMLKey.D_ID, "d_id", "d_id2", map);
//            map.put(DEALERSHIP_ID, "d_id");
//        }
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: Key.REASON_FOR_ERROR is added to maps, marking last duplicate.
//    @Test
//    void readInventoryDealerInDealerErrorAfterVehicle() {
//        List<Map<Key, Object>> mapsToTest = readInventory("after_car_error", 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        Map<Key, Object> map = getFullMap();
//        putReasonDuplicate(XMLKey.D_NAME, "name", "name2", map);
//        mapsToTestAgainst.add(map);
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: Key.REASON_FOR_ERROR is added to map, marking last invalid Long.
//    @Test
//    void readInventoryBadNumber() {
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getBadPriceMap());
//
//        List<Map<Key, Object>> mapsToTest = readInventory("bad_price", 1);
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: Creation of XMLIO throws an exception.
//    @Test
//    void readPomFile() {
//        String reason = "Can't read or write to Maven's pom.xml file.\n" +
//                        "If this is not Maven's pom.xml file, rename it and try again.";
//        try {
//            XMLIO xmlIO = getXMLIO("pom", 'r', true);
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            assertInstanceOf(ReadWriteException.class, e);
//            assertEquals(reason, e.getMessage());
//        }
//    }
//
//    // Expected: XMLIO.readInventory() throws a ReadWriteException.
//    @Test
//    void readInventoryInvalidXML() {
//        String partialPath = "missing_data";
//        System.out.println("Expected [fatal error] (from DocumentBuilder) for file ending in " + partialPath + ".xml");
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = getXMLIO(partialPath, 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//        try {
//            xmlIO.readInventory();
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            System.out.println();
//            assertInstanceOf(Exception.class, e.getCause());
//        }
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryNoOverallDealersRoot() {
//        runDealershipStateFullDealership("dealer_as_root");
//    }
//
//    // Expected: XMLIO.readInventory() throws a ReadWriteException.
//    @Test
//    void readInventoryTwoDealersRoots() {
//        String partialPath = "no_root_tag";
//        System.out.println("Expected [fatal error] (from DocumentBuilder) for file ending in " + partialPath + ".xml");
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = getXMLIO(partialPath, 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//        try {
//            xmlIO.readInventory();
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            System.out.println();
//            assertInstanceOf(Exception.class, e.getCause());
//        }
//    }
//
//    // Expected: No data found.
//    @Test
//    void readInventoryNoDealerTags() {
//        List<Map<Key, Object>> mapsToTest = readInventory("no_dealers", 0);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: No data found.
//    @Test
//    void readInventoryNoVehicleTags() {
//        List<Map<Key, Object>> mapsToTest = readInventory("no_cars", 0);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryLowerCaseTags() {
//        runDealershipStateFullDealership("lowercase_tags");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryAllAttributes() {
//        runDealershipStateAllVehicles("all_vals_in_atr");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryAllTags() {
//        runDealershipStateAllVehicles("all_vals_in_tags");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryExtraWhitespace() {
//        runDealershipStateAllVehicles("extra_whitespace");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryAllTagsAndAttributes() {
//        runDealershipStateAllVehicles("equal_dupe_atr_tag");
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryTagsWithinTags() {
//        runDealershipStateAllVehicles("tags_in_tags");
//    }
//
//    // Expected: Key.REASON_FOR_ERROR is added to map, marking last duplicate.
//    @Test
//    void readInventoryVehicleWithinVehicle() {
//        List<Map<Key, Object>> mapsToTest = readInventory("car_in_car", 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        Map<Key, Object> fullMap = getFullMap();
//        mapsToTestAgainst.add(fullMap);
//        VEHICLE_ID.putValid(fullMap, "v_id3");
//
//        putReasonDuplicate(XMLKey.V_ID, "v_id3", "v_id1", fullMap);
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: Vehicle outside of Dealership is discarded. Other Vehicle is read fine.
//    @Test
//    void readInventoryVehicleOutSideDealership() {
//        List<Map<Key, Object>> mapsToTest = readInventory("car_outside_dealer", 1);
//
//        List<Map<Key, Object>> mapsToTestAgainst = new ArrayList<>();
//        mapsToTestAgainst.add(getExtraMap());
//
//        testMaps(mapsToTestAgainst, mapsToTest);
//    }
//
//    // Expected: No issues, all Vehicles read.
//    @Test
//    void readInventoryWithSomeGibberishTagsAndAttributes() {
//        runDealershipStateFullDealership("nonsense_tags");
//    }
//
//    // Expected: FileIOBuilder.buildNewFileIO() throws exception.
//    @Test
//    void writeInventoryThroughFileIOBuilder() {
//        XMLIO xmlIO;
//        try {
//            xmlIO = getXMLIO("w2", 'w', true);
//            fail(xmlIO.toString());
//        } catch (ReadWriteException e) {
//            BadExtensionException cause = new BadExtensionException("Bad extension.");
//            FileIOBuilderTest.assertSameCauseType(new ReadWriteException(cause), e);
//        }
//    }
//
//    // Expected: writeInventory() throws exception.
//    @Test
//    void writeInventoryManually() {
//        String path = FileIOBuilderTest.getPath("w3", "xmlIOTests", ".xml");
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = new XMLIO(path, 'w');
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//        assertNotNull(xmlIO);
//        List<Map<Key, Object>> maps = new ArrayList<>();
//        maps.add(getMinMap());
//        try {
//            xmlIO.writeInventory(maps);
//        } catch (ReadWriteException e) {
//            String reason = "Can not write to XML Files.";
//            assertInstanceOf(ReadWriteException.class, e);
//            assertEquals(reason, e.getMessage());
//        }
//    }
//
//    // Expected: readInventory() throws exception.
//    @Test
//    void writeInventoryReadFromWriteXML() {
//        String path = FileIOBuilderTest.getPath("w1", "xmlIOTests", ".xml");
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = new XMLIO(path, 'w');
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//        try {
//            xmlIO.readInventory();
//        } catch (ReadWriteException e) {
//            String reason = "Must be mode 'r', not mode 'w'.";
//            assertInstanceOf(ReadWriteException.class, e);
//            assertEquals(reason, e.getMessage());
//        }
//    }
//
//    // Expected: writeInventory() throws exception.
//    @Test
//    void writeInventoryWriteFromReadXML() {
//        XMLIO xmlIO = null;
//        try {
//            xmlIO = getXMLIO("w4", 'r', false);
//        } catch (ReadWriteException e) {
//            fail(e);
//        }
//
//        assertNotNull(xmlIO);
//        List<Map<Key, Object>> maps = new ArrayList<>();
//        maps.add(getMinMap());
//        try {
//            xmlIO.writeInventory(maps);
//        } catch (ReadWriteException e) {
//            String reason = "Can not write to XML Files.";
//            assertInstanceOf(ReadWriteException.class, e);
//            assertEquals(reason, e.getMessage());
//        }
//    }
}