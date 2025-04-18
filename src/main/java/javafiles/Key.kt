package javafiles

import javafiles.customexceptions.ReadWriteException
import javafiles.domainfiles.Dealership
import javafiles.domainfiles.Vehicle
import kotlin.reflect.KClass
import kotlin.reflect.cast

enum class Key (// Getters and Setters:
    val key: String, // should be the same as JSONIO key
    private val clazz: KClass<*>, // ^would love to make it a generic instead, but can't seem to do that with enum
    val needed: Boolean,
    private val passClazz: KClass<*>?,
    private val func: ((Any) -> Any?)?
) {
    // java.lang is needed for Long and Boolean or primitive would be used.
    DEALERSHIP_ID("dealership_id", String::class, true,
        Dealership::class, {dealer -> (dealer as Dealership).dealerId }),
    DEALERSHIP_NAME("dealership_name", String::class, false,
        Dealership::class, {dealer -> (dealer as Dealership).dealerName }),
    DEALERSHIP_RECEIVING_STATUS("dealership_receiving_status", java.lang.Boolean::class, false,
        Dealership::class, {dealer -> (dealer as Dealership).statusAcquiringVehicle }),
    DEALERSHIP_RENTING_STATUS("dealership_rental_status", java.lang.Boolean::class, false,
        Dealership::class, {dealer -> (dealer as Dealership).rentingVehicles }),

    VEHICLE_TYPE("vehicle_type", String::class, true,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).vehicleType }),
    VEHICLE_MANUFACTURER("vehicle_manufacturer", String::class, false,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).vehicleManufacturer }),
    VEHICLE_MODEL("vehicle_model", String::class, true,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).vehicleModel }),

    VEHICLE_ID("vehicle_id", String::class, true,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).vehicleId }),

    VEHICLE_RENTAL_STATUS("vehicle_rental_status", java.lang.Boolean::class, false,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).rentalStatus }),

    VEHICLE_PRICE("price", java.lang.Long::class, true,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).vehiclePrice }),
    VEHICLE_PRICE_UNIT("price_unit", String::class, false,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).priceUnit }),

    VEHICLE_ACQUISITION_DATE("acquisition_date", java.lang.Long::class, false,
        Vehicle::class, {vehicle -> (vehicle as Vehicle).acquisitionDate }),

    DUMMY_VEHICLE("dummy_vehicle", java.lang.Boolean::class, false,
        null, null),
    REASON_FOR_ERROR("error_reason", ReadWriteException::class, false,
        null, null);

    val className: String
        get() = clazz.java.name

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
    fun <T : Any> getVal(map: Map<Key, Any>, type: KClass<T>): T {
        val obj = map[this]
        if (type.isInstance(obj) && type == clazz) {
            return type.cast(obj)
        }
        throw ClassCastException(obj.toString() + " is not an instance of " + type.java.name)
    }

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
    fun <T : Any> getVal(map: Map<Key, Any>, type: Class<T>): T {
        return getVal(map, type.kotlin)
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

    fun fillData(map: MutableMap<Key, Any>, extractFrom: Any) {
        if (func == null || passClazz == null) {return;}
        if (passClazz.isInstance(extractFrom)) {
            val value = func.invoke(extractFrom)
            if (value != null) { putValid(map, value) }
        }
    }

    companion object {
        fun addErrorReason(map: MutableMap<Key, Any>, cause: Exception): MutableMap<Key, Any> {
            REASON_FOR_ERROR.putValid(map, ReadWriteException(cause))
            return map
        }
    }
}