package javafiles.domainfiles

import javafiles.Key
import javafiles.customexceptions.*
import javafiles.domainfiles.VehicleCreator.Companion.instance
import java.util.*

/**
 * Represents a dealership that manages vehicle sales and rentals.
 *
 * This class provides functionality to add, remove, and retrieve vehicles from the dealership's
 * sales and rental inventories. It also allows for enabling and disabling vehicle acquisition
 * and rental services.
 *
 * The dealership is identified by a unique dealer ID and maintains separate inventories for
 * vehicles available for sale and rental.
 *
 * Authors: Patrick McLucas, Christopher Engelhart
 */
class Dealership (
    val dealerId: String,
    var dealerName: String
) {
    val inventory: MutableList<Vehicle> = ArrayList()
    var statusAcquiringVehicle: Boolean = true
    var rentingVehicles = false

    fun inventoryContainsById(newId: String): Boolean {
        val cleanId = newId.trim().replace("\\s+".toRegex(), "")
        for (vehicle in inventory) {
            val existingVehicleId = vehicle.vehicleId.trim().replace("\\s+".toRegex(), "")
            if (existingVehicleId.equals(cleanId, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    @Throws(DealershipNotAcceptingVehiclesException::class, VehicleAlreadyExistsException::class)
    fun addIncomingVehicle(newVehicle: Vehicle) {
        if (!statusAcquiringVehicle) {
            throw DealershipNotAcceptingVehiclesException(
                "Dealership $dealerId is not accepting new vehicles at this time. Vehicle ID: ${newVehicle.vehicleId} was not added."
            )
        }

        if (inventoryContainsById(newVehicle.vehicleId)) {
            throw VehicleAlreadyExistsException("Vehicle ID: ${newVehicle.vehicleId} already exists in inventory of dealership $dealerId.")
        }

        inventory.add(newVehicle)
    }

    fun removeFromInventory(targetVehicle: Vehicle) {
        inventory.remove(targetVehicle)
    }

    /**
     * Takes a Map with information about a Vehicle, creates that Vehicle and adds to inventory.
     */
    fun dataToInventory(map: MutableMap<Key, Any>): Boolean {
        return try {
            val vehicle = vehicleFactory.createFullVehicle(map)
            addIncomingVehicle(vehicle)
            true
        } catch (e: Exception) {
            //TODO: Less generic Exceptions?
            val wrapped = ReadWriteException(e)
            Key.REASON_FOR_ERROR.putValid(map, wrapped)
            false
        }
    }

    @Throws(
        InvalidVehicleTypeException::class,
        VehicleAlreadyExistsException::class,
        DealershipNotAcceptingVehiclesException::class,
        InvalidPriceException::class,
        MissingCriticalInfoException::class
    )
    fun manualVehicleAdd(map: Map<Key, Any>) {
        addIncomingVehicle(vehicleFactory.createFullVehicle(map))
    }

    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     */
    @Throws(RentalException::class)
    fun updateVehicleRental(vehicle: Vehicle) {
        // Will throw RentalException if vehicle is a sports car
        vehicle.rentalStatus = !vehicle.rentalStatus
    }

    fun calcDealerMapData(): Map<Key, Any> {
        val map = EnumMap<Key, Any>(Key::class.java)
        Key.entries.forEach { key-> key.fillData(map, this) }
        return map
    }

    fun calcDataMap(): List<Map<Key, Any>>{
        val list: MutableList<Map<Key, Any>> = ArrayList()

        for (vehicle in inventory) { list.add( vehicle.getDataMap() ) }
        return list
    }

    /**
     * Transfers a vehicle from one dealership's inventory to another.
     */
    @Throws(
        DuplicateSenderException::class,
        VehicleAlreadyExistsException::class,
        DealershipNotAcceptingVehiclesException::class
    )
    fun dealershipVehicleTransfer(receivingDealer: Dealership, transferVehicle: Vehicle) {
        if (this == receivingDealer) {
            throw DuplicateSenderException("Sender and receiver dealership can not be the same")
        }

        receivingDealer.addIncomingVehicle(transferVehicle)
        inventory.remove(transferVehicle)
    }
    
    override fun toString(): String {
        var rentedVehicleNum = 0
        for (vehicle in inventory) {
            if (vehicle.rentalStatus) {
                rentedVehicleNum++
            }
        }

        var str = "Dealership ID: $dealerId\n"
        str += "Dealership Name: ${Objects.requireNonNullElse(dealerName, "No name on file.")}\n"
        str += "Sales Inventory Num: ${inventory.size - rentedVehicleNum}\n"
        str += "Rental Inventory Num: $rentedVehicleNum"
        return str
    }

    companion object {
        private val vehicleFactory: VehicleFactory = instance // Singleton
    }
}