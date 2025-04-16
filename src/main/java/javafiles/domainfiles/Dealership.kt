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
class Dealership(
    val dealerId: String,
    var dealerName: String
) {
    val saleVehicles: ArrayList<Vehicle> = ArrayList()
    val rentalVehicles: ArrayList<Vehicle> = ArrayList()
    var statusAcquiringVehicle: Boolean = true
    var rentingVehicles = false

    //fun getRentingVehicles(): Boolean {
    //    return rentingVehicles
   // }

   // fun setName(name: String) {
   //     this.dealerName = name
   // }

    fun setReceivingVehicle(status: Boolean) {
        statusAcquiringVehicle = Objects.requireNonNullElse(status, true)
    }

    //fun setRentingVehicles(status: Boolean) {
    //    rentingVehicles = Objects.requireNonNullElse(status, false)
  // }

    @Throws(VehicleNotFoundException::class)
    fun getVehicleFromSalesInventory(vehicleID: String): Vehicle {
        for (vehicle in saleVehicles) {
            if (vehicle.vehicleId == vehicleID) {
                return vehicle
            }
        }
        throw VehicleNotFoundException("Vehicle with ID: $vehicleID not found in sales inventory.")
    }

    @Throws(VehicleNotFoundException::class)
    fun getVehicleFromRentalInventory(vehicleID: String): Vehicle {
        for (vehicle in rentalVehicles) {
            if (vehicle.vehicleId == vehicleID) {
                return vehicle
            }
        }
        throw VehicleNotFoundException("Vehicle with ID: $vehicleID not found in rental inventory.")
    }

    private fun isVehicleInInventory(newVehicle: Vehicle, inventory: List<Vehicle>): Boolean {
        for (vehicle in inventory) {
            if (vehicle.vehicleId == newVehicle.vehicleId) {
                return true
            }
        }
        return false
    }

    fun isVehicleInInventoryById(newId: String): Boolean {
        val cleanId = newId.trim().replace("\\s+".toRegex(), "")
        for (vehicle in totalInventory) {
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

        if (isVehicleInInventory(newVehicle, saleVehicles) ||
            isVehicleInInventory(newVehicle, rentalVehicles)) {
            throw VehicleAlreadyExistsException("Vehicle ID: ${newVehicle.vehicleId} already exists in inventory of dealership $dealerId.")
        }

        saleVehicles.add(newVehicle)
    }

    /**
     * Takes a Map with information about a Vehicle, creates that Vehicle and adds to inventory.
     */
    fun dataToInventory(map: MutableMap<Key, Any>): Boolean {
        val vehicle: Vehicle? = try {
            vehicleFactory.createVehicle(map as Map<Key?, Any?>?)
        } catch (e: Exception) {
            val wrapped = ReadWriteException(e)
            Key.REASON_FOR_ERROR.putValid(map, wrapped)
            return false
        }

        if (vehicle == null) {
            val wrapped = ReadWriteException(MissingCriticalInfoException("Vehicle Creation Failed"))
            Key.REASON_FOR_ERROR.putValid(map, wrapped)
            return false
        }

        return try {
            addIncomingVehicle(vehicle)
            true
        } catch (e: Exception) {
            val wrapped = ReadWriteException(e)
            Key.REASON_FOR_ERROR.putValid(map, wrapped)
            false
        }
    }

    val totalInventory: ArrayList<Vehicle>
        get() = ArrayList<Vehicle>().apply {
            addAll(saleVehicles)
            addAll(rentalVehicles)
        }
    @Throws(
        InvalidVehicleTypeException::class,
        VehicleAlreadyExistsException::class,
        DealershipNotAcceptingVehiclesException::class,
        InvalidPriceException::class,
        MissingCriticalInfoException::class
    )
    fun manualVehicleAdd(
        vehicleId: String,
        vehicleManufacturer: String?,
        vehicleModel: String?,
        vehiclePrice: Long,
        acquisitionDate: Long?,
        vehicleType: String?,
        priceUnit: String?
    ) {
        if (vehiclePrice <= 0) {
            throw InvalidPriceException("Error: Vehicle price must be a positive value. Vehicle ID: $vehicleId was not added.")
        }

        val newVehicle = vehicleFactory.createVehicle(vehicleType, vehicleId, vehicleModel, vehiclePrice)
        vehicleFactory.fillVehicle(newVehicle, vehicleManufacturer, acquisitionDate, priceUnit, null)
        if (newVehicle != null) {
            this.addIncomingVehicle(newVehicle)
        }
    }

    /**
     * Updates the rental status of a vehicle within a dealership and moves it between
     * the dealership's sales and rental inventories based on the updated rental status.
     */
    @Throws(RentalException::class)
    fun updateVehicleRental(vehicle: Vehicle) {
        if (!vehicle.vehicleType.equals("Sports car", ignoreCase = true)) {
            if (vehicle.rentalStatus) {
                vehicle.disableRental()
            } else {
                vehicle.enableRental()
            }
        } else {
            throw VehicleNotRentableException("Sports car types are not currently rentable")
        }

        if (saleVehicles.contains(vehicle)) {
            saleVehicles.remove(vehicle)
            rentalVehicles.add(vehicle)
        } else {
            rentalVehicles.remove(vehicle)
            saleVehicles.add(vehicle)
        }
    }
    @Throws(
        IllegalArgumentException::class,
        VehicleAlreadyExistsException::class,
        DealershipNotRentingException::class,
        VehicleNotRentableException::class
    )
    fun addRentalVehicle(rental: Vehicle) {
        requireNotNull(rental) { "Rental vehicle is null." }

        if (!rentingVehicles) {
            throw DealershipNotRentingException("Dealership $dealerId is not currently providing rental services.")
        }

        if (!rental.rentalStatus) {
            throw VehicleNotRentableException("Vehicle ${rental.vehicleId} is not currently rentable.")
        }

        if (isVehicleInInventory(rental, rentalVehicles)) {
            throw VehicleAlreadyExistsException("Vehicle ${rental.vehicleId} is already in the rental inventory.")
        }

        rentalVehicles.add(rental)
    }

    val dataMap: List<Map<Key, Any>>
        get() {
            val list: MutableList<Map<Key, Any>> = ArrayList()
            val fullInventory: List<Vehicle> = this.totalInventory

            for (vehicle in fullInventory) {
                val map: MutableMap<Key, Any> = HashMap()
                Key.DEALERSHIP_ID.putValid(map, dealerId)
                Key.DEALERSHIP_NAME.putValid(map, dealerName)
                Key.DEALERSHIP_RECEIVING_STATUS.putValid(map, statusAcquiringVehicle)
                Key.DEALERSHIP_RENTING_STATUS.putValid(map, rentingVehicles)
                vehicle.getDataMap(map)
                list.add(map)
            }
            return list
        }

    /**
     * Removes a vehicle from the dealership's inventory.
     */
    fun removeVehicleFromInventory(targetVehicle: Vehicle) {
        requireNotNull(targetVehicle) { "Target vehicle is null." }
        saleVehicles.remove(targetVehicle)
        rentalVehicles.remove(targetVehicle)
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

        removeVehicleFromInventory(transferVehicle)
        receivingDealer.addIncomingVehicle(transferVehicle)
    }
    override fun toString(): String {
        var str = "Dealership ID: $dealerId\n"
        str += "Dealership Name: ${Objects.requireNonNullElse(dealerName, "No name on file.")}\n"
        str += "Sales Inventory Num: ${saleVehicles.size}\n"
        str += "Rental Inventory Num: ${rentalVehicles.size}"
        return str
    }

    companion object {
        private val vehicleFactory: VehicleFactory = instance // Singleton
    }
}