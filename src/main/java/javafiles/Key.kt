package javafiles

import javafiles.customexceptions.ReadWriteException

enum class Key (// Getters and Setters:
    val key: String, // should be the same as JSONIO key
    private val clazz: Class<*>, // ^would love to make it a generic instead, but can't seem to do that with enum
    val needed: Boolean
) {
    // java.lang is needed for Long and Boolean or primitive would be used.
    DEALERSHIP_ID("dealership_id", String::class.java, true),
    DEALERSHIP_NAME("dealership_name", String::class.java, false),
    DEALERSHIP_RECEIVING_STATUS("dealership_receiving_status", java.lang.Boolean::class.java, false),
    DEALERSHIP_RENTING_STATUS("dealership_rental_status", java.lang.Boolean::class.java, false),

    VEHICLE_TYPE("vehicle_type", String::class.java, true),
    VEHICLE_MANUFACTURER("vehicle_manufacturer", String::class.java, false),
    VEHICLE_MODEL("vehicle_model", String::class.java, true),

    VEHICLE_ID("vehicle_id", String::class.java, true),
    VEHICLE_RENTAL_STATUS("vehicle_rental_status", java.lang.Boolean::class.java, false),

    VEHICLE_PRICE("price", java.lang.Long::class.java, true),
    VEHICLE_PRICE_UNIT("price_unit", String::class.java, false),

    VEHICLE_ACQUISITION_DATE("acquisition_date", java.lang.Long::class.java, false),

    REASON_FOR_ERROR("error_reason", ReadWriteException::class.java, false);

    val className: String
        get() = clazz.name

    /**
     * Casts the value in the [Map] at KEY to the given [Class] [T]. If [T]
     * is not the same as the [Class] held in CLASS or the [Object] in map at KEY is not an
     * instance of [T], returns null instead.
     *
     * @param map The [Map] object that is being searched.
     * @param type The [Class] of the object to return.
     * @return the value in map at KEY cast to [T].
     * @param [T] The [Class] that is expected to be returned.
     */
    fun <T> getVal(map: Map<Key, Any>, type: Class<T>): T? {
        val obj = map[this]
        if (type.isInstance(obj) && type == clazz) {
            try {
                return type.cast(obj)
            } catch (e: ClassCastException) {
                // Should be caught with if statement, but left as is just in case.
                return null
            }
        }
        return null
    }

    /**
     * Confirms that the [Object] is an instance of the correct type.
     * (As given by CLASS)
     *
     * @param obj The [Object] that is being checked.
     * @return Whether obj is of the correct type.
     */
    private fun validObjectType(obj: Any): Boolean {
        return clazz.isInstance(obj)
    }

    /**
     * Puts the [Object] into the map iff the item is not null and of the correct type.
     *
     * @param map The [Map] that is being appended to.
     * @param obj The [Object] being added as a value to the map.
     * @return weather object was added to the [Map].
     */
    fun putValid(map: MutableMap<Key, Any>, obj: Any): Boolean {
        if (validObjectType(obj)) {
            map[this] = obj
            return true
        }
        return false
    }
}