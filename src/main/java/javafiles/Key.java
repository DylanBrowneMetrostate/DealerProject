package javafiles;

import javafiles.customexceptions.ReadWriteException;

import java.util.Map;

public enum Key {
    DEALERSHIP_ID ("dealership_id", String.class, true),
    DEALERSHIP_NAME ("dealership_name", String.class, false),
    DEALERSHIP_RECEIVING_STATUS ("dealership_receiving_status", Boolean.class, false),
    DEALERSHIP_RENTING_STATUS("dealership_rental_status", Boolean.class, false),

    VEHICLE_TYPE ("vehicle_type", String.class, true),
    VEHICLE_MANUFACTURER ("vehicle_manufacturer", String.class, false),
    VEHICLE_MODEL ("vehicle_model", String.class, true),

    VEHICLE_ID ("vehicle_id", String.class, true),
    VEHICLE_RENTAL_STATUS ("vehicle_rental_status", Boolean.class, false),

    VEHICLE_PRICE ("price", Long.class, true),
    VEHICLE_PRICE_UNIT ("price_unit", String.class, false),

    VEHICLE_ACQUISITION_DATE ("acquisition_date", Long.class, false),

    REASON_FOR_ERROR ("error_reason", ReadWriteException.class, false);

    private final String KEY; // should be the same as JSONIO key
    private final Class<?> CLASS;
    // ^would love to make it a generic instead, but can't seem to do that with enum
    private final boolean needed;

    Key(String key, Class<?> c, boolean needed) {
        KEY = key;
        CLASS = c;
        this.needed = needed;
    }

    // Getters and Setters:
    public String getKey() {return KEY;}
    public String getClassName() {return CLASS.getName();}
    public boolean getNeeded() {return needed;}

    /**
     * Casts the value in the {@link Map} at KEY to the given {@link Class} {@link T}. If {@link T}
     * is not the same as the {@link Class} held in CLASS or the {@link Object} in map at KEY is not an
     * instance of {@link T}, returns null instead.
     *
     * @param map The {@link Map} object that is being searched.
     * @param type The {@link Class} of the object to return.
     * @return the value in map at KEY cast to {@link T}.
     * @param <T> The {@link Class} that is expected to be returned.
     */
    public <T> T getVal(Map<Key, Object> map, Class<T> type) {
        Object object = map.get(this);
        if (type.isInstance(object) && type.equals(CLASS)) {
            try {
                return type.cast(object);
            } catch (ClassCastException e) {
                // Should be caught with if statement, but left as is just in case.
                return null;
            }
        }
        return null;
    }

    /**
     * Confirms that the {@link Object} is an instance of the correct type.
     * (As given by CLASS)
     *
     * @param object The {@link Object} that is being checked.
     * @return Whether object is of the correct type.
     */
    private boolean validObjectType(Object object) {
        return CLASS.isInstance(object);
    }

    /**
     * Puts the {@link Object} into the map iff the item is not null and of the correct type.
     *
     * @param map The {@link Map} that is being appended to.
     * @param object The {@link Object} being added as a value to the map.
     * @return weather object was added to the {@link Map}.
     */
    public boolean putValid(Map<Key, Object> map, Object object) {
        if (validObjectType(object)) {
            map.put(this, object);
            return true;
        }
        return false;
    }
}