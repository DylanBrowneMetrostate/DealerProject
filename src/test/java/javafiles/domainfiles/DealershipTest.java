package javafiles.domainfiles;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javafiles.Key;
import javafiles.customexceptions.*;

class DealershipTest {

    private Dealership dealership;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Vehicle vehicle3;
    private Vehicle vehicle4;

    @BeforeEach
    public void setUp() throws RentalException {
        dealership = new Dealership("D001", "Test Dealership");

        RentalStrategy rentalStrategy = new DefaultRentalStrategy();

        //TODO: Why (and how) are these Vehicle abstract class instead of the child classes?
        vehicle1 = new Vehicle("suv", "V001", "Model X", 50000L, rentalStrategy) {};
        vehicle2 = new Vehicle("pickup", "V002", "Model Y", 60000L, rentalStrategy) {};
        vehicle3 = new Vehicle("sedan", "V003", "Model Z", 40000L, rentalStrategy) {};
        vehicle4 = new Vehicle("sports car", "V004", "Model A", 80000L, rentalStrategy) {};
        // Note: Pseudo-sports car got Default rental status.

        vehicle3.setRentalStatus(true);
        vehicle4.setRentalStatus(true);

    }

    @Test
    public void testConstructor() {
        assertEquals("D001", dealership.getDealerId());
        assertEquals("Test Dealership", dealership.getDealerName());
        assertTrue(dealership.getStatusAcquiringVehicle());
        assertFalse(dealership.getRentingVehicles());
        assertTrue(dealership.getInventory().isEmpty());
    }

    @Test
    public void testSetName() {
        dealership.setDealerName("New Name");
        assertEquals("New Name", dealership.getDealerName());
    }

    @Test
    public void testsetStatusAcquiringVehicle() {
        dealership.setStatusAcquiringVehicle(false);
        assertFalse(dealership.getStatusAcquiringVehicle());
    }

    @Test
    public void testSetRentingVehicles() {
        dealership.setRentingVehicles(true);
        assertTrue(dealership.getRentingVehicles());
    }

    @Test
    public void testAddVehicle() throws Exception {
        
        assertDoesNotThrow(() -> dealership.addIncomingVehicle(vehicle1));

        List<Vehicle> inventory = dealership.getInventory();
        
        assertEquals(1, inventory.size());
        assertEquals(vehicle1, inventory.getFirst());
    }

    private void assertAdded(Map<Key, Object> map, Key key, Object value) {
        assertTrue(key.putValid(map, value), "Key: [" + key.getKey() + "] not added.");
    }

    private Vehicle getVehicleFromInventory(String vehicleId, Dealership dealer) throws VehicleNotFoundException{
        for (Vehicle vehicle : dealer.getInventory()) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return vehicle;
            }
        }
        throw new VehicleNotFoundException("Vehicle with ID: " + vehicleId + " not found in inventory.");
    }
    
    @Test
    public void testManualVehicleAdd() throws Exception {
        Map<Key, Object> baseMap = new EnumMap<>(Key.class);

        String vehicleManufacturer = "Volkswagon";
        assertAdded(baseMap, Key.VEHICLE_MANUFACTURER, vehicleManufacturer);

        String vehicleModel = "Tiguan";
        assertAdded(baseMap, Key.VEHICLE_MODEL, vehicleModel);

        Long vehiclePrice = 32000L;
        assertAdded(baseMap, Key.VEHICLE_PRICE, vehiclePrice);

        Long acquisitionDate = 1515354694451L;
        assertAdded(baseMap, Key.VEHICLE_ACQUISITION_DATE, acquisitionDate);

        String vehicleType = "suv";
        assertAdded(baseMap, Key.VEHICLE_TYPE, vehicleType);

        String priceUnit = "USD";
        assertAdded(baseMap, Key.VEHICLE_PRICE_UNIT, priceUnit);

        String currentId = "V001a";
        assertAdded(baseMap, Key.VEHICLE_ID, currentId);

        Map<Key, Object> map = new EnumMap<>(baseMap);
        Map<Key, Object> mapA = map;

        assertDoesNotThrow(() -> dealership.manualVehicleAdd(mapA) );

        // Verifies vehicle was added to sales inventory
        Vehicle addedVehicle = getVehicleFromInventory(currentId, dealership);
        assertNotNull(addedVehicle);
        assertEquals(currentId, addedVehicle.getVehicleId());
        assertEquals(vehicleModel, addedVehicle.getVehicleModel());
        assertEquals(vehiclePrice, addedVehicle.getVehiclePrice());

        currentId = "V001b";
        map = new EnumMap<>(baseMap);
        assertAdded(map, Key.VEHICLE_ID, currentId);
        assertAdded(map, Key.VEHICLE_PRICE, -1L);
        Map<Key, Object> mapB = map;

        // Test invalid vehicle price
        InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class, () -> {
            
            dealership.manualVehicleAdd(mapB);
        });
        assertNotNull(invalidPriceException);

        currentId = "V001c";
        map = new EnumMap<>(baseMap);
        assertAdded(map, Key.VEHICLE_ID, currentId);
        assertAdded(map, Key.VEHICLE_TYPE, "canyonero");
        Map<Key, Object> mapC = map;

        // Test invalid vehicle type
        InvalidVehicleTypeException invalidTypeException = assertThrows(InvalidVehicleTypeException.class, () -> {
            dealership.manualVehicleAdd(mapC);
        });
        assertNotNull(invalidTypeException);

        currentId = "V001d";
        map = new EnumMap<>(baseMap);
        assertAdded(map, Key.VEHICLE_ID, currentId);
        Map<Key, Object> mapD = map;

        // Test duplicate vehicle addition
        VehicleAlreadyExistsException duplicateException = assertThrows(VehicleAlreadyExistsException.class, () -> {
            dealership.manualVehicleAdd(mapD);
            dealership.manualVehicleAdd(mapD);
        });
        assertNotNull(duplicateException);


        currentId = "V001e";
        map = new EnumMap<>(baseMap);
        assertAdded(map, Key.VEHICLE_ID, currentId);
        Map<Key, Object> mapE = map;

        // Test when dealership is not accepting vehicles
        dealership.setStatusAcquiringVehicle(false);
        DealershipNotAcceptingVehiclesException notAcceptingException = assertThrows(DealershipNotAcceptingVehiclesException.class, () -> {
            dealership.manualVehicleAdd(mapE);
        });
        assertNotNull(notAcceptingException);
    }

    @Test
    public void testAddMultipleVehicles() throws Exception {
        dealership.addIncomingVehicle(vehicle1);
        dealership.addIncomingVehicle(vehicle2);
        dealership.addIncomingVehicle(vehicle3);
        dealership.addIncomingVehicle(vehicle4);

        assertEquals(4, dealership.getInventory().size());
        assertEquals(vehicle1, dealership.getInventory().get(0));
        assertEquals(vehicle2, dealership.getInventory().get(1));
        assertEquals(vehicle3, dealership.getInventory().get(2));
        assertEquals(vehicle4, dealership.getInventory().get(3));
    }

    @Test
    public void testAddDuplicateVehicle() throws Exception {

        try {
            dealership.addIncomingVehicle(vehicle2);
        } catch (DealershipNotAcceptingVehiclesException | VehicleAlreadyExistsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        VehicleAlreadyExistsException exception = assertThrows(VehicleAlreadyExistsException.class, () -> {
            dealership.addIncomingVehicle(vehicle2);
        });
        assertNotNull(exception);
    }

    @Test
    public void testAddVehicleWhenNotAccepting() {
        dealership.setStatusAcquiringVehicle(false);

        DealershipNotAcceptingVehiclesException exception = assertThrows(DealershipNotAcceptingVehiclesException.class, () -> {
            dealership.addIncomingVehicle(vehicle3);
        });
        assertNotNull(exception);
    }

    @Test
    public void testRemoveVehicle() throws IllegalArgumentException {
        List<Vehicle> inventory = dealership.getInventory();
        inventory.add(vehicle4);

        dealership.removeFromInventory(vehicle4);

        assertTrue(dealership.getInventory().isEmpty());
    }

    @Test
    public void testRemoveVehicleWhenEmpty() throws EmptyInventoryException {
        dealership.removeFromInventory(vehicle4);

        assertTrue(dealership.getInventory().isEmpty());
    }

    @Test
    public void testRemoveNonExistentVehicle() throws IllegalArgumentException {
        List<Vehicle> inventory = dealership.getInventory();
        inventory.add(vehicle1);

        Vehicle nonExistentVehicle = new Vehicle("Truck", "V999", "Model Y", 60000L, new DefaultRentalStrategy()) {};
        dealership.removeFromInventory(nonExistentVehicle);
        
        assertEquals(1, dealership.getInventory().size());
        assertEquals(vehicle1, dealership.getInventory().get(0));
    }

    @Test
    public void testGetVehicleFromSalesInventory() throws VehicleNotFoundException, DealershipNotAcceptingVehiclesException, VehicleAlreadyExistsException {
        dealership.addIncomingVehicle(vehicle2);

        Vehicle retrievedVehicle = getVehicleFromInventory("V002", dealership);

        assertNotNull(retrievedVehicle);
        assertEquals(vehicle2, retrievedVehicle);
    }

    @Test
    public void testGetVehicleFromSalesInventoryNotFound() {
        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            getVehicleFromInventory("V999", dealership);
        });

        assertNotNull(exception);
        assertEquals("Vehicle with ID: V999 not found in inventory.", exception.getMessage());
    }

    @Test
    public void testGetVehicleFromRentalInventory() throws Exception {
        // TODO: Fix test
        /*
        Vehicle vehicle = new Vehicle("suv", "R001", "Model Y", 60000L, null) {
            @Override
            public boolean getRentalStatus() {
                return true; 
            }
        };

        dealership.setRentingVehicles(true); 

        assertDoesNotThrow(() -> dealership.addRentalVehicle(vehicle));

        Vehicle retrievedVehicle = dealership.getVehicleFromRentalInventory("R001");

        assertNotNull(retrievedVehicle);
        assertEquals(vehicle, retrievedVehicle);
         */
    }

    @Test
    public void testGetVehicleFromRentalInventoryNotFound() {
        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            getVehicleFromInventory("R999", dealership);
        });

        assertNotNull(exception);
        assertEquals("Vehicle with ID: R999 not found in inventory.", exception.getMessage());
    }

    @Test
    public void testGetTotalInventory() {
        dealership.getInventory().add(vehicle1);
        dealership.getInventory().add(vehicle2);
        dealership.getInventory().add(vehicle3);
        dealership.getInventory().add(vehicle4);

        List<Vehicle> totalInventory = dealership.getInventory();

        assertNotNull(totalInventory);
        assertEquals(4, totalInventory.size());
        assertTrue(totalInventory.contains(vehicle1));
        assertTrue(totalInventory.contains(vehicle2));
        assertTrue(totalInventory.contains(vehicle3));
        assertTrue(totalInventory.contains(vehicle4));
    }

    @Test
    public void testGetDataMap() {
        dealership.getInventory().add(vehicle1);
        dealership.getInventory().add(vehicle2);
        dealership.getInventory().add(vehicle3);
        dealership.getInventory().add(vehicle4);

        List<Map<Key, Object>> dataMapList = dealership.calcDataMap();

        assertNotNull(dataMapList);
        assertEquals(4, dataMapList.size());
    }

    private void isSameCauseType(ReadWriteException expectedException, ReadWriteException testedException) {
        assertNotNull(expectedException);
        Throwable causeTarget = expectedException.getCause();
        assertNotNull(causeTarget);

        assertNotNull(testedException);
        Throwable cause = testedException.getCause();
        assertNotNull(cause);

        assertEquals(causeTarget.getClass(), cause.getClass());
    }

    @Test
    public void testDataToInventory() {
        Map<Key, Object> validMap = new HashMap<>();
        validMap.put(Key.VEHICLE_TYPE, "suv");
        validMap.put(Key.VEHICLE_ID, "V001");
        validMap.put(Key.VEHICLE_MODEL, "Model X");
        validMap.put(Key.VEHICLE_PRICE, 50000L);
    
        boolean result = dealership.dataToInventory(validMap);
        assertTrue(result);
        assertEquals(1, dealership.getInventory().size());
        assertEquals("V001", dealership.getInventory().get(0).getVehicleId());
    
        Map<Key, Object> invalidTypeMap = new HashMap<>();
        invalidTypeMap.put(Key.VEHICLE_TYPE, "spaceship");
        invalidTypeMap.put(Key.VEHICLE_ID, "V002");
        invalidTypeMap.put(Key.VEHICLE_MODEL, "Model Y");
        invalidTypeMap.put(Key.VEHICLE_PRICE, 60000L);
    
        boolean invalidTypeResult = dealership.dataToInventory(invalidTypeMap);
        assertFalse(invalidTypeResult);

        InvalidVehicleTypeException cause = new InvalidVehicleTypeException("spaceship is not a valid Vehicle Type.");
        ReadWriteException testingException = Key.REASON_FOR_ERROR.getVal(invalidTypeMap, ReadWriteException.class);
        isSameCauseType(new ReadWriteException(cause), testingException);

        Map<Key, Object> duplicateMap = new HashMap<>(validMap);
    
        boolean duplicateResult = dealership.dataToInventory(duplicateMap);
        assertFalse(duplicateResult);

        String reason = "This vehicle is already located in the sales inventory." +
                         " Vehicle ID: V001 was not added to dealership D001.";
        VehicleAlreadyExistsException causeVehicleExists = new VehicleAlreadyExistsException(reason);
        ReadWriteException testingExceptionVehicleExists =
                Key.REASON_FOR_ERROR.getVal(duplicateMap, ReadWriteException.class);
        isSameCauseType(new ReadWriteException(causeVehicleExists), testingExceptionVehicleExists);
    }

    @Test
    public void testToString() {

        dealership.getInventory().add(vehicle2);
        dealership.getInventory().add(vehicle4);

        String result = dealership.toString();
    
        String expectedOutput = "Dealership ID: D001\n" +
                "Dealership Name: Test Dealership\n" +
                "Sales Inventory Num: 1\n" +
                "Rental Inventory Num: 1";

        assertEquals(expectedOutput, result);
    }
}
