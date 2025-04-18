package javafiles.domainfiles

import javafiles.Key
import javafiles.customexceptions.InvalidAcquisitionDateException
import javafiles.customexceptions.RentalException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Vehicle is an abstract class that defines a set of common attributes
 * and behaviors for all vehicle types. This class serves as a blueprint for any
 * specific vehicle types that may extend it.
 *
 * @author Christopher Engelhart
 */
abstract class Vehicle(
    /**
     * The specific type of the vehicle (e.g., SUV, Sedan, Pickup, Sports Car).
     */
    val vehicleType: String,
    /**
     * The unique identifier for the vehicle.
     */
    var vehicleId: String,
    /**
     * The model name of the vehicle.
     */
    var vehicleModel: String,
    /**
     * The price of the vehicle.
     */
    var vehiclePrice: Long,
    /**
     * The rental strategy used for this vehicle. Defaults to [DefaultRentalStrategy].
     */
    var rentalStrategy: RentalStrategy = DefaultRentalStrategy()
) {
    /**
     * The name of the vehicle's manufacturer. Defaults to "Unknown".
     */
    var vehicleManufacturer: String = "Unknown"

    /**
     * The unit of currency for the vehicle's price. Defaults to "dollars".
     */
    var priceUnit: String = "dollars"

    /**
     * The acquisition date of the vehicle as a Unix timestamp (milliseconds since epoch).
     * Can be null if the acquisition date is not yet set.
     */
    var acquisitionDate: Long? = null

    /**
     * Indicates whether the vehicle is currently rented.
     */
    var rentalStatus: Boolean = false
        private set

    /**
     * Constructor method to be used by Vehicle's child classes
     * to specify the children's vehicle type with default rental strategy
     *
     * @param type the specific vehicle type that the extending class is
     * @param id The vehicle ID of the Vehicle
     */
    constructor(type: String, id: String, model: String, price: Long) :
            this(type, id, model, price, DefaultRentalStrategy())


    init {
        require(vehicleId.isNotBlank()) { "Vehicle ID cannot be blank" }
        require(vehicleModel.isNotBlank()) { "Vehicle model cannot be blank" }
        require(vehiclePrice >= 0) { "Vehicle price cannot be negative" }
    }

    /**
     * Sets the rental state of the vehicle object.
     *
     * @param state `true` if the vehicle is currently rented, `false` if the vehicle is no longer rented
     * and is available.
     */
    fun setRental(state: Boolean) {
        rentalStatus = state
    }

    /**
     * Enables rental for this vehicle using the configured [rentalStrategy].
     *
     * @throws RentalException If an error occurs during the rental enabling process as defined by the strategy.
     */
    @Throws(RentalException::class)
    fun enableRental() {
        rentalStrategy.enableRental(this)
    }

    /**
     * Disables rental for this vehicle using the configured [rentalStrategy].
     *
     * @throws RentalException If an error occurs during the rental disabling process as defined by the strategy.
     */
    @Throws(RentalException::class)
    fun disableRental() {
        rentalStrategy.disableRental(this)
    }

    /**
     * Gets the acquisition date formatted as a String in "MM/dd/yyyy" format.
     * If the acquisition date is null, an empty String is returned.
     *
     * @return The formatted acquisition date String, or an empty String if the acquisition date is null.
     * @throws InvalidAcquisitionDateException if the [acquisitionDate] is not null but represents an invalid epoch time.
     */
    val formattedAcquisitionDate: String
        @Throws(InvalidAcquisitionDateException::class)
        get() = acquisitionDate?.let {
            try {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            } catch (e: Exception) {
                throw InvalidAcquisitionDateException("Invalid acquisition date epoch time.")
            }
        } ?: ""

    /**
     * Creates and returns a String representation of the Vehicle.
     *
     * @return A String representation of the Vehicle, including its type, ID, model, manufacturer,
     * price, rental status, and acquisition date.
     */
    override fun toString(): String {
        val dateStr = acquisitionDate?.let { Date(it).toString() } ?: "Unknown"
        return """Vehicle: $vehicleType
            
        ID: $vehicleId
        Model: $vehicleModel
        Manufacturer: $vehicleManufacturer
        Price: $vehiclePrice $priceUnit
        Currently being rented: $rentalStatus
        Acquired: $dateStr"""
    }

    /**
     * Retrieves Vehicle data and returns a [Map] with the given data.
     * Each key-value pair in the map represents an attribute of the vehicle.
     */
    fun getDataMap(): Map<Key, Any> {
        val map: MutableMap<Key, Any> = EnumMap(Key::class.java)
        Key.entries.forEach { key -> key.fillData(map, this) }
        return map
    }
}