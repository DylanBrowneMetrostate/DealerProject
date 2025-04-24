package javafiles;

import static javafiles.Key.*;

import javafiles.customexceptions.ReadWriteException;
import kotlin.enums.EnumEntries;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {
    private void putValidObjectTest(Object exampleInstance, Key[] notAddedKeys) {
        Map<Key, Object> map = new HashMap<>();

        int prevLen = 0;

        for (Key key: getEntries()) {
            key.putValid(map, exampleInstance);

            boolean added = true;

            for (Key badKey: notAddedKeys) {
                if (key.equals(badKey)) {
                    added = false;
                    break;
                }
            }

            if (added) {assertEquals(prevLen + 1, map.size());} else {assertEquals(prevLen, map.size());}

            prevLen = map.size();
        }
    }

    private Key[] calcNotAddedKeys(Key[] addedKeysArray) {
        List<Key> addedKeys = new ArrayList<>();
        Collections.addAll(addedKeys, addedKeysArray);

        Key[] notAddedKeys = new Key[getEntries().size() - addedKeys.size()];

        int i = 0;
        for (Key key: getEntries()) {
            if (!addedKeys.contains(key)) {
                notAddedKeys[i] = key;
                i++;
            }
        }

        assertEquals(notAddedKeys.length, i);

        return notAddedKeys;
    }

    @Test
    public void putValidStringTest() {
        Key[] addedKeys = {
                DEALERSHIP_ID, DEALERSHIP_NAME,
                VEHICLE_TYPE, VEHICLE_MANUFACTURER,
                VEHICLE_MODEL, VEHICLE_ID,
                VEHICLE_PRICE_UNIT
        };

        Key[] notAddedKeys = calcNotAddedKeys(addedKeys);

        putValidObjectTest("String", notAddedKeys);
    }

    @Test
    public void putValidLongTest() {
        Key[] addedKeys = {VEHICLE_ACQUISITION_DATE, VEHICLE_PRICE};

        Key[] notAddedKeys = calcNotAddedKeys(addedKeys);

        putValidObjectTest(1046L, notAddedKeys);
    }

    @Test
    public void putValidBooleanTest() {
        Key[] addedKeys = {
                DEALERSHIP_RECEIVING_STATUS, DEALERSHIP_RENTING_STATUS,
                VEHICLE_RENTAL_STATUS, DUMMY_VEHICLE
        };

        Key[] notAddedKeys = calcNotAddedKeys(addedKeys);

        putValidObjectTest(true, notAddedKeys);
    }

    private Key[] getEntriesAsArray() {
        EnumEntries<Key> entries = getEntries();
        Key[] keys = new Key[entries.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = entries.get(i);
        }
        return keys;
    }

    @Test
    public void putValidNullTest() {
        Map<Key, Object> map = new HashMap<>();
        for (Key key: getEntriesAsArray()) {
            if (key.putValid(map, null)) {
                fail("Null added");
            }
        }
    }

    @Test
    public void putValidOtherObjectTest() {
        putValidObjectTest(new HashMap<Key, Object>(), getEntriesAsArray());
    }

    @Test
    public void putValidKeyTest() {
        putValidObjectTest(VEHICLE_ID, getEntriesAsArray());
    }

    private Map<Key, Object> getGoodMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.VEHICLE_PRICE_UNIT, "$");
        map.put(Key.VEHICLE_PRICE, 1000L);
        map.put(Key.VEHICLE_MODEL, "model");
        map.put(Key.DEALERSHIP_RENTING_STATUS, true);
        map.put(Key.VEHICLE_TYPE, "suv");
        map.put(Key.DEALERSHIP_NAME, "d_name");
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, true);
        map.put(Key.DEALERSHIP_ID, "d_id");
        map.put(Key.VEHICLE_MANUFACTURER, "make");
        map.put(Key.VEHICLE_ID, "v_id");
        map.put(Key.VEHICLE_RENTAL_STATUS, true);
        map.put(Key.VEHICLE_ACQUISITION_DATE, 123L);

        map.put(DUMMY_VEHICLE, false);
        map.put(REASON_FOR_ERROR, new ReadWriteException("No error, just need all keys."));

        assertEquals(map.size(), Key.getEntries().size(), "Missing keys in Good map, add them.");
        return map;
    }

    @Test
    public void getValGoodMapGoodValStr() {
        Map<Key, Object> map = getGoodMap();
        DEALERSHIP_NAME.getVal(map, String.class);
    }

    @Test
    public void getValGoodMapGoodValLong() {
        Map<Key, Object> map = getGoodMap();
        VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
    }

    @Test
    public void getValGoodMapGoodValBool() {
        Map<Key, Object> map = getGoodMap();
        VEHICLE_RENTAL_STATUS.getVal(map, Boolean.class);
    }

    @Test
    public void getValGoodMapAllGoodVal() {
        Map<Key, Object> map = getGoodMap();
        for (Key key: getEntries()) {
            Class<?> clazz = key.getClazzJava();

            try {
                if (clazz.equals(String.class)) {
                    key.getVal(map, String.class);
                    continue;
                } else if (clazz.equals(Long.class)) {
                    key.getVal(map, Long.class);
                    continue;
                } else if (clazz.equals(Boolean.class)) {
                    key.getVal(map, Boolean.class);
                    continue;
                } else if (clazz.equals(ReadWriteException.class)) {
                    key.getVal(map, ReadWriteException.class);
                    continue;
                }
                fail("Function not called for " + key.getKey());
            } catch (ClassCastException e) {
                fail(e.getMessage() + '\n' + key.getKey() + " not found in map with: " + clazz);
            }
        }
    }

    @Test
    public void getValGoodMapBadValStr() {
        Map<Key, Object> map = getGoodMap();
        try {
            DEALERSHIP_NAME.getVal(map, Long.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValGoodMapBadValLong() {
        Map<Key, Object> map = getGoodMap();
        try {
            VEHICLE_ACQUISITION_DATE.getVal(map, Boolean.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValGoodMapBadValBool() {
        Map<Key, Object> map = getGoodMap();
        try {
            VEHICLE_RENTAL_STATUS.getVal(map, String.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValGoodMapAllBadVal() {
        Map<Key, Object> map = getGoodMap();
        for (Key key: getEntries()) {
            Class<?> clazz = key.getClazzJava();

            try {
                if (clazz.equals(String.class)) {
                    key.getVal(map, Boolean.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Long.class)) {
                    key.getVal(map, String.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Boolean.class)) {
                    key.getVal(map, Long.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(ReadWriteException.class)) {
                    key.getVal(map, Integer.class);
                    fail("No exception thrown.");
                }
            } catch (ClassCastException e) {
                continue;
            }
            fail("Did not call key.getVal().");
        }
    }

    private Map<Key, Object> getBadMap() {
        Map<Key, Object> map = new HashMap<>();

        map.put(Key.VEHICLE_PRICE_UNIT, false);
        map.put(Key.VEHICLE_PRICE, "One Hundred");
        map.put(Key.VEHICLE_MODEL, 123L);
        map.put(Key.DEALERSHIP_RENTING_STATUS, "Accepting");
        map.put(Key.VEHICLE_TYPE, 456);
        map.put(Key.DEALERSHIP_NAME, false);
        map.put(Key.DEALERSHIP_RECEIVING_STATUS, "Receiving");
        map.put(Key.DEALERSHIP_ID, 789L);
        map.put(Key.VEHICLE_MANUFACTURER, false);
        map.put(Key.VEHICLE_ID, 111213);
        map.put(Key.VEHICLE_RENTAL_STATUS, 111116543L);
        map.put(Key.VEHICLE_ACQUISITION_DATE, false);

        return map;
    }

    @Test
    public void getValBadMapGoodValStr() {
        Map<Key, Object> map = getBadMap();
        try {
            DEALERSHIP_NAME.getVal(map, String.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValBadMapGoodValLong() {
        Map<Key, Object> map = getBadMap();
        try {
            VEHICLE_ACQUISITION_DATE.getVal(map, Long.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValBadMapGoodValBool() {
        Map<Key, Object> map = getBadMap();
        try {
            VEHICLE_RENTAL_STATUS.getVal(map, Boolean.class);
            fail("Did not throw ClassCastException.");
        } catch (ClassCastException _) {
            
        }
    }

    @Test
    public void getValBadMapAllGoodVal() {
        Map<Key, Object> map = getBadMap();
        for (Key key: getEntries()) {
            Class<?> clazz = key.getClazzJava();

            try {
                if (clazz.equals(String.class)) {
                    key.getVal(map, String.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Long.class)) {
                    key.getVal(map, Long.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Boolean.class)) {
                    key.getVal(map, Boolean.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(ReadWriteException.class)) {
                    key.getVal(map, ReadWriteException.class);
                    fail("No exception thrown.");
                }
            } catch (ClassCastException e) {
                continue;
            }

            fail("Did not call key.getVal().");
        }
    }

    @Test
    public void getValBadMapBadValStr() {
        Map<Key, Object> map = getBadMap();
        try {
            DEALERSHIP_NAME.getVal(map, Long.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValBadMapBadValLong() {
        Map<Key, Object> map = getBadMap();
        try {
            VEHICLE_ACQUISITION_DATE.getVal(map, Boolean.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValBadMapBadValBool() {
        Map<Key, Object> map = getBadMap();
        try {
            VEHICLE_RENTAL_STATUS.getVal(map, String.class);
            fail("No ClassCastException Thrown.");
        } catch (ClassCastException _) {

        }
    }

    @Test
    public void getValBadMapAllBadVal() {
        Map<Key, Object> map = getBadMap();
        for (Key key: getEntries()) {
            Class<?> clazz = key.getClazzJava();

            try {
                if (clazz.equals(String.class)) {
                    key.getVal(map, Boolean.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Long.class)) {
                    key.getVal(map, String.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(Boolean.class)) {
                    key.getVal(map, Long.class);
                    fail("No exception thrown.");
                } else if (clazz.equals(ReadWriteException.class)) {
                    key.getVal(map, Integer.class);
                    fail("No exception thrown.");
                }
            } catch (ClassCastException _) {
                continue;
            }

            fail("Did not call key.getVal().");
        }
    }
}