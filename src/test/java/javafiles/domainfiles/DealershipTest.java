package javafiles.domainfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void setUp() {
        dealership = new Dealership("D001", "Test Dealership");

        vehicle1 = new Vehicle("suv", "V001", "Model X", 50000L, null) {};
        vehicle2 = new Vehicle("pickup", "V002", "Model Y", 60000L, null) {};
        vehicle3 = new Vehicle("sedan", "V003", "Model Z", 40000L, null) {};
        vehicle4 = new Vehicle("sports car", "V004", "Model A", 80000L, null) {};
    }

    @Test
    public void testConstructor() {
        assertEquals("D001", dealership.getDealerId());
        assertEquals("Test Dealership", dealership.getDealerName());
        assertTrue(dealership.getStatusAcquiringVehicle());
        assertFalse(dealership.getRentingVehicles());
        assertTrue(dealership.getSaleVehicles().isEmpty());
        assertTrue(dealership.getRentalVehicles().isEmpty());
    }

    @Test
    public void testSetName() {
        dealership.setDealerName("New Name");
        assertEquals("New Name", dealership.getDealerName());
    }

    @Test
    public void testSetReceivingVehicle() {
        dealership.setReceivingVehicle(false);
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

        assertEquals(1, dealership.getSaleVehicles().size());
        assertEquals(vehicle1, dealership.getSaleVehicles().get(0));
    }

    @Test
    public void testManualVehicleAdd() throws Exception {
        String vehicleManufacturer = "Volkswagon";
        String vehicleModel = "Tiguan";
        Long vehiclePrice = 32000L;
        Long acquisitionDate = 1515354694451L;
        String vehicleType = "suv";
        String priceUnit = "USD";

        assertDoesNotThrow(() -> dealership.manualVehicleAdd("V001a", vehicleManufacturer, vehicleModel, 
            vehiclePrice, acquisitionDate, vehicleType, priceUnit));

        // Verifies vehicle was added to sales inventory
        Vehicle addedVehicle = dealership.getVehicleFromSalesInventory("V001a");
        assertNotNull(addedVehicle);
        assertEquals("V001a", addedVehicle.getVehicleId());
        assertEquals(vehicleModel, addedVehicle.getVehicleModel());
        assertEquals(vehiclePrice, addedVehicle.getVehiclePrice());
        
        // Test invalid vehicle price
        InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class, () -> {
            
            dealership.manualVehicleAdd("V001b", vehicleManufacturer, vehicleModel, 
            -1L, acquisitionDate, vehicleType, priceUnit);
        });
        assertEquals("Error: Vehicle price must be a positive value. Vehicle ID: V001b was not added.", invalidPriceException.getMessage());
    
        // Test invalid vehicle type
        InvalidVehicleTypeException invalidTypeException = assertThrows(InvalidVehicleTypeException.class, () -> {
            dealership.manualVehicleAdd("V001c", vehicleManufacturer, vehicleModel, 
            vehiclePrice, acquisitionDate, "canyonero", priceUnit);
        });
        assertNotNull(invalidTypeException);

        // Test duplicate vehicle addition
        VehicleAlreadyExistsException duplicateException = assertThrows(VehicleAlreadyExistsException.class, () -> {
            dealership.manualVehicleAdd("V001d", vehicleManufacturer, vehicleModel, vehiclePrice, acquisitionDate, vehicleType, priceUnit);
            dealership.manualVehicleAdd("V001d", vehicleManufacturer, vehicleModel, vehiclePrice, acquisitionDate, vehicleType, priceUnit);
        });
        assertNotNull(duplicateException);

        // Test when dealership is not accepting vehicles
        dealership.setReceivingVehicle(false);
        DealershipNotAcceptingVehiclesException notAcceptingException = assertThrows(DealershipNotAcceptingVehiclesException.class, () -> {
            dealership.manualVehicleAdd("V001e", vehicleManufacturer, vehicleModel, 
            vehiclePrice, acquisitionDate, vehicleType, priceUnit);
        });
        assertNotNull(notAcceptingException);
    }

    @Test
    public void testAddMultipleVehicles() throws Exception {
        dealership.addIncomingVehicle(vehicle1);
        dealership.addIncomingVehicle(vehicle2);
        dealership.addIncomingVehicle(vehicle3);
        dealership.addIncomingVehicle(vehicle4);

        assertEquals(4, dealership.getSaleVehicles().size());
        assertEquals(vehicle1, dealership.getSaleVehicles().get(0));
        assertEquals(vehicle2, dealership.getSaleVehicles().get(1));
        assertEquals(vehicle3, dealership.getSaleVehicles().get(2));
        assertEquals(vehicle4, dealership.getSaleVehicles().get(3));
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
        dealership.setReceivingVehicle(false);

        DealershipNotAcceptingVehiclesException exception = assertThrows(DealershipNotAcceptingVehiclesException.class, () -> {
            dealership.addIncomingVehicle(vehicle3);
        });
        assertNotNull(exception);
    }

    @Test
    public void testRemoveVehicle() throws IllegalArgumentException {
        ArrayList<Vehicle> inventory = dealership.getSaleVehicles();
        inventory.add(vehicle4);

        dealership.removeVehicleFromInventory(vehicle4);

        assertTrue(dealership.getSaleVehicles().isEmpty());
    }

    @Test
    public void testRemoveVehicleWhenEmpty() throws EmptyInventoryException {
        dealership.removeVehicleFromInventory(vehicle4);

        assertTrue(dealership.getSaleVehicles().isEmpty());
    }

    @Test
    public void testRemoveNonExistentVehicle() throws IllegalArgumentException {
        ArrayList<Vehicle> inventory = dealership.getSaleVehicles();
        inventory.add(vehicle1);

        Vehicle nonExistentVehicle = new Vehicle("Truck", "V999", "Model Y", 60000L, null) {};
        dealership.removeVehicleFromInventory(nonExistentVehicle);
        
        assertEquals(1, dealership.getSaleVehicles().size());
        assertEquals(vehicle1, dealership.getSaleVehicles().get(0));
    }

    @Test
    public void testGetVehicleFromSalesInventory() throws VehicleNotFoundException, DealershipNotAcceptingVehiclesException, VehicleAlreadyExistsException {
        dealership.addIncomingVehicle(vehicle2);

        Vehicle retrievedVehicle = dealership.getVehicleFromSalesInventory("V002");

        assertNotNull(retrievedVehicle);
        assertEquals(vehicle2, retrievedVehicle);
    }

    @Test
    public void testGetVehicleFromSalesInventoryNotFound() {
        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            dealership.getVehicleFromSalesInventory("V999");
        });

        assertNotNull(exception);
        assertEquals("Vehicle with ID: V999 not found in sales inventory.", exception.getMessage());
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
            dealership.getVehicleFromRentalInventory("R999");
        });

        assertNotNull(exception);
        assertEquals("Vehicle with ID: R999 not found in rental inventory.", exception.getMessage());
    }

    @Test
    public void testGetTotalInventory() {
        dealership.getSaleVehicles().add(vehicle1);
        dealership.getSaleVehicles().add(vehicle2);
        dealership.getRentalVehicles().add(vehicle3);
        dealership.getRentalVehicles().add(vehicle4);

        ArrayList<Vehicle> totalInventory = dealership.getTotalInventory();

        assertNotNull(totalInventory);
        assertEquals(4, totalInventory.size());
        assertTrue(totalInventory.contains(vehicle1));
        assertTrue(totalInventory.contains(vehicle2));
        assertTrue(totalInventory.contains(vehicle3));
        assertTrue(totalInventory.contains(vehicle4));
    }

    @Test
    public void testGetDataMap() {
        dealership.getSaleVehicles().add(vehicle1);
        dealership.getSaleVehicles().add(vehicle2);
        dealership.getRentalVehicles().add(vehicle3);
        dealership.getRentalVehicles().add(vehicle4);

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
        assertEquals(1, dealership.getSaleVehicles().size());
        assertEquals("V001", dealership.getSaleVehicles().get(0).getVehicleId());
    
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

        dealership.getSaleVehicles().add(vehicle3); 
        dealership.getRentalVehicles().add(vehicle4); 

        String result = dealership.toString();
    
        String expectedOutput = "Dealership ID: D001\n" +
                "Dealership Name: Test Dealership\n" +
                "Sales Inventory Num: 1\n" +
                "Rental Inventory Num: 1";

        assertEquals(expectedOutput, result);
    }
}
