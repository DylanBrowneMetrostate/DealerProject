package javafiles.domainfiles

import javafiles.Key
import javafiles.customexceptions.*

/**
 * Represents a central manager for all dealerships within a vehicle dealership system.
 *
 *
 * This class maintains a list of [Dealership] objects and provides functionality to add and retrieve dealerships by ID or name.
 * It supports operations for toggling rental status for vehicles, enabling or disabling vehicle receiving
 * at dealerships, and transferring vehicles between rental and sales inventories. The class also handles
 * data import by mapping structured input into dealership inventories.
 *
 */
class Company {
    val listDealerships: ArrayList<Dealership> = ArrayList()

    /**
     * Adds a [Dealership] object to the list of dealerships in the company.
     *
     * @param dealership dealership object to be added to company
     */
    fun addDealership(dealership: Dealership) {
        listDealerships.add(dealership)
    }

    /**
     * Takes a String representing a Dealership ID and returns the index of that
     * Dealership in [Company.listDealerships].
     *
     * @param dealerId A String equal to the dealerId of the Dealership we are searching for.
     * @return The index of the searched for Dealership in listDealerships (-1 if absent).
     */
    fun getDealershipIndex(dealerId: String): Int {
        return listDealerships.indexOfFirst { it.dealerId == dealerId }
    }

    /**
     * Takes a String representing a Dealership ID and returns the given
     * [Dealership] from [Company.listDealerships]
     *
     * @param dealerId A String equal to the dealerID of the target Dealership.
     * @return The Dealership target dealership (null if absent).
     */
    fun findDealership(dealerId: String): Dealership? {
        return listDealerships.find { it.dealerId == dealerId }
    }

    /**
     * Checks if a [Dealership] with the given ID has renting services enabled.
     *
     * @param dealershipId The ID of the dealership to check.
     * @return true if the dealership has renting enabled, false otherwise.
     */
    fun isDealershipRentingEnabled(dealershipId: String): Boolean {
        return findDealership(dealershipId)?.rentingVehicles ?: false
    }

    /**
     * Adds a new [Vehicle] to the [Dealership] inventory based on the provided vehicle details.
     * This method creates a new vehicle based on the vehicle type and sets its attributes
     * using the provided parameters in map.
     *
     * [VehicleFactory.createVehicle] is used to create and validate the vehicle type.
     * If the vehicle type is unsupported, the method will throw an exception and return without
     * making any changes to the inventory. If the vehicle is created successfully, it will be added
     * to the dealership's inventory.
     *
     * @param map A [Map] containing all attributes of the Vehicle to be created.
     * @param dealer The [Dealership] that will receive the Vehicle.
     * @throws DealershipNotAcceptingVehiclesException If the dealership is not currently accepting new vehicles.
     * @throws VehicleAlreadyExistsException If the vehicle is already present in either the sales or rental inventory.
     * @throws InvalidVehicleTypeException If the vehicle type is not supported.
     * @throws InvalidPriceException if the vehicle price is not a positive value
     */
    @Throws(
        InvalidVehicleTypeException::class,
        VehicleAlreadyExistsException::class,
        DealershipNotAcceptingVehiclesException::class,
        InvalidPriceException::class,
        MissingCriticalInfoException::class
    )
    fun manualVehicleAdd(map: Map<Key, Any>, dealer: Dealership) {
        val id: String = map[Key.VEHICLE_ID] as? String ?: throw MissingCriticalInfoException("Vehicle ID is missing.")
        val make: String? = map[Key.VEHICLE_MANUFACTURER] as? String
        val model: String = map[Key.VEHICLE_MODEL] as? String ?: throw MissingCriticalInfoException("Vehicle model is missing.")
        val price: Long = (map[Key.VEHICLE_PRICE] as? Number)?.toLong() ?: throw MissingCriticalInfoException("Vehicle price is missing or invalid.")
        val acqDate: Long? = (map[Key.VEHICLE_ACQUISITION_DATE] as? Number)?.toLong()
        val type: String = map[Key.VEHICLE_TYPE] as? String ?: throw MissingCriticalInfoException("Vehicle type is missing.")
        val unit: String? = map[Key.VEHICLE_PRICE_UNIT] as? String

        if (isVehicleInInventoryById(id)) {
            throw VehicleAlreadyExistsException(
                "This vehicle is already located in the inventory. " +
                        "Vehicle ID: $id was not added to dealership ${dealer.dealerId}."
            )
        }
        dealer.manualVehicleAdd(id, make, model, price, acqDate, type, unit)
    }

    /**
     * Returns whether a given Vehicle ID is in any Dealership of the Company.
     *
     * @param id The id of the Vehicle searched for.
     * @return weather the Vehicle is in the company.
     */
    private fun isVehicleInInventoryById(id: String?): Boolean {
        return listDealerships.any { it.isVehicleInInventoryById(id ?: "") }
    }

    /**
     * Takes a List of Map<Key, Object>s representing a List of [Vehicle] information
     * and writes the data in each map to the corresponding [Dealership].
     *
     * @param data The List of Maps containing Vehicle information to be added to inventory.
     */
    fun dataToInventory(data: List<MutableMap<Key, Any>>?): List<Map<Key, Any>> {
        val badInventoryMaps = mutableListOf<Map<Key, Any>>()
        val newlyCreatedDealerships = mutableMapOf<Dealership, Map<Key, Any>>()

        if (data == null) {
            return badInventoryMaps
        }

        for (map in data) {
            if (map.containsKey(Key.REASON_FOR_ERROR)) {
                badInventoryMaps.add(map)
                continue
            }

            val id = map[Key.DEALERSHIP_ID] as? String
            val name = map[Key.DEALERSHIP_NAME] as? String

            if (id == null) {
                val cause = MissingCriticalInfoException("No dealerID.")
                val exception = ReadWriteException(cause)
                map[Key.REASON_FOR_ERROR] = exception
                badInventoryMaps.add(map)
                continue
            }

            val v_id = map[Key.VEHICLE_ID] as? String
            if (v_id != null && isVehicleInInventoryById(v_id)) {
                val cause = VehicleAlreadyExistsException("Duplicate Vehicle ID in inventory")
                val exception = ReadWriteException(cause)
                map[Key.REASON_FOR_ERROR] = exception
                badInventoryMaps.add(map)
                continue
            }

            var dealership = findDealership(id)
            if (dealership == null) {
                dealership = Dealership(id, name ?: "")
                addDealership(dealership)
                newlyCreatedDealerships[dealership] = map // Mark as newly created
            }

            if (!dealership.dataToInventory(map)) {
                badInventoryMaps.add(map)
            }
        }

        // Apply receiving and renting status only to newly created dealerships
        for ((dealer, statusMap) in newlyCreatedDealerships) {
            dealer.rentingVehicles = statusMap[Key.DEALERSHIP_RENTING_STATUS] as? Boolean ?: false
            dealer.setReceivingVehicle(statusMap[Key.DEALERSHIP_RECEIVING_STATUS] as? Boolean ?: true)
        }

        return badInventoryMaps
    }

    /**
     * Retrieves [Vehicle] data for all Dealerships within the Company.
     *
     * @return A [List] of [Map]s representing all vehicles in the Company.
     */
    val dataMap: List<Map<Key, Any>>
        get() = listDealerships.flatMap { it.dataMap }

    /**
     * Generates a formatted [String] of Dealership IDs.
     *
     * @return A tab-separated string of dealership IDs.
     */
    val dealershipIdList: String
        get() {
            if (listDealerships.isEmpty()) {
                return "No valid Dealerships."
            }
            return listDealerships.joinToString(separator = "\t") { it.dealerId }
                .chunked(6)
                .joinToString(separator = "\n")
        }

    /**
     * Returns a list of all Dealership IDs.
     *
     * @return An [ArrayList] of all dealership ID strings.
     */
    val allDealershipIds: ArrayList<String>
        get() = listDealerships.map { it.dealerId }.toCollection(ArrayList())

    /**
     * Returns a list of maps containing dealership info.
     *
     * @return A [List] of dealership info maps.
     */
    val dealershipInfoList: List<Map<String, Any>>
        get() {
            val dealershipInfoList: MutableList<Map<String, Any>> = ArrayList()
            for (dealership in listDealerships) {
                val dealershipInfo: MutableMap<String, Any> = HashMap()
                dealershipInfo["id"] = dealership.dealerId
                dealershipInfo["name"] = dealership.dealerName
                dealershipInfo["receivingEnabled"] = dealership.statusAcquiringVehicle
                dealershipInfo["rentingEnabled"] = dealership.rentingVehicles
                dealershipInfoList.add(dealershipInfo)
            }
            return dealershipInfoList
        }

    /**
     * Updates the Dealership receiving status for Vehicles.
     *
     * @param dealerIndex index of dealership in list
     * @param userInput string command ("enable"/"disable")
     * @return true if invalid input, false otherwise
     */
    fun changeReceivingStatus(dealerIndex: Int, userInput: String): Boolean {
        val dealer = listDealerships[dealerIndex]
        if (userInput.equals("enable", ignoreCase = true)) {
            if (dealer.statusAcquiringVehicle) {
                println("Dealership ${dealer.dealerId} is already set to receive vehicles.")
            } else {
                dealer.statusAcquiringVehicle = true
                println("Vehicle receiving status for dealership ${dealer.dealerId} has been enabled.")
            }
            return false
        } else if (userInput.equals("disable", ignoreCase = true)) {
            if (!dealer.statusAcquiringVehicle) {
                println("Dealership ${dealer.dealerId} is already set to not receive vehicles.")
            } else {
                dealer.setReceivingVehicle(false)
                println("Vehicle receiving status for dealership ${dealer.dealerId} has been disabled.")
            }
            return false
        }
        println("Invalid input. Please enter 'enable' or 'disable'.")
        return true
    }
}