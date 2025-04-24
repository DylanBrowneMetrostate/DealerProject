package javafiles

import javafiles.customexceptions.MissingCriticalInfoException
import javafiles.customexceptions.ReadWriteException
import kotlin.jvm.Throws

//TODO: Figure out how to limit coupling w/ Dealership and Vehicle
import javafiles.domainfiles.Dealership
import javafiles.domainfiles.Vehicle

import kotlin.reflect.KClass
import kotlin.reflect.cast

enum class Key (// Getters and Setters:
    val key: String, // should be the same as JSONIO key
    val clazz: KClass<*>, // ^would love to make it a generic instead, but can't seem to do that with enum
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

    /**
     * Used to get the [clazz] value for [java] files that don't work with [KClass].
     */
    fun getClazzJava() : Class<*> { return clazz.java }

    /**
     * Casts the value in the [Map] at [Key] to the given [KClass] [T]. If [T] is not the same
     * as  the [KClass] held in [clazz] or the [Object] in map at [Key] is not an instance
     * of [T], throws a [ClassCastException] instead.
     *
     * @param [T] The [KClass] that is expected to be returned.
     * @param map The [Map] object that is being searched.
     * @param type The [KClass] of the object to return.
     * @return the value in map at [Key] cast to [T].
     * @throws [ClassCastException] when [Class] given does not match [clazz].
     */
    @Throws(ClassCastException::class)
    fun <T : Any> getVal(map: Map<Key, Any>, type: KClass<T>): T {
        val obj = map[this]
        if (type.isInstance(obj) && type == clazz) {
            return type.cast(obj)
        }
        throw ClassCastException(obj.toString() + " is not an instance of " + type.java.name)
    }

    /**
     * Casts the value in the [Map] at [Key] to the given [Class] [T]. If [T] is not the same
     * as the [java] equivalent of the [KClass] held in [clazz] or the [Object] in map at [Key]
     * is not an instance of [T], throws a [ClassCastException] instead.
     *
     * @param [T] The [Class] that is expected to be returned.
     * @param map The [Map] object that is being searched.
     * @param type The [Class] of the object to return.
     * @return the value in map at [Key] cast to [T].
     * @throws [ClassCastException] when [Class] given does not match [clazz].
     */
    @Throws(ClassCastException::class)
    fun <T : Any> getVal(map: Map<Key, Any>, type: Class<T>): T {
        return getVal(map, type.kotlin)
    }

    /**
     * Confirms that the [Object] is an instance of the correct type.
     * (As given by [clazz])
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
    fun putValid(map: MutableMap<Key, Any>, obj: Any?): Boolean {
        if (obj != null && validObjectType(obj)) {
            map[this] = obj
            return true
        }
        return false
    }

    /**
     * Takes a [Map] and appends it with the key value of this [Key] and the value that is extracted
     * from the given [extractFrom] value. The class of [extractFrom] needs to be an instance of
     * [passClazz] or the lambda used to get the value to add to the [Map] will throw a [ClassCastException].
     * If the value extracted is null, nothing is appended and an exception is thrown if the [Key] is
     * [needed].
     *
     * @param map The [Map] that will be appended with the new value.
     * @param extractFrom The [Object] that will have its value added to [map] ath this [Key].
     * @throws MissingCriticalInfoException when the [extractFrom] is an instance of [passClazz],
     *   but the value extracted is null and the [Key] is marked as [needed].
     */
    @Throws(MissingCriticalInfoException::class)
    fun fillData(map: MutableMap<Key, Any>, extractFrom: Any) {
        // TODO: Unit test this method
        if (func == null || passClazz == null) {return;}
        if (passClazz.isInstance(extractFrom)) {
            val value = func.invoke(extractFrom)
            if (value != null) { putValid(map, value) }
            else if (needed) {
                val cause = "This object of class $passClazz is missing [$key]."
                throw MissingCriticalInfoException(cause)
            }
        }
    }

    companion object {
        /**
         * Wraps the given [cause] as the cause of a [ReadWriteException] that is appended to the
         * given [Map].
         *
         * @param map The [Map] that has had an error occur and needs [REASON_FOR_ERROR] appended to it.
         * @param cause An exception that represents the cause of the error.
         */
        fun addErrorReason(map: MutableMap<Key, Any>, cause: Exception): MutableMap<Key, Any> {
            REASON_FOR_ERROR.putValid(map, ReadWriteException(cause))
            return map
        }
    }
}