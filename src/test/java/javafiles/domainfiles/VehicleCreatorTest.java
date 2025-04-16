package javafiles.domainfiles;

import javafiles.customexceptions.InvalidPriceException;
import javafiles.customexceptions.InvalidVehicleTypeException;
import javafiles.customexceptions.MissingCriticalInfoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleCreatorTest {

    @Test
    public void testGetInstance() {
        VehicleCreator instance1 = VehicleCreator.Companion.getInstance();
        VehicleCreator instance2 = VehicleCreator.Companion.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2, "getInstance() should return the same singleton instance");
    }

    @Test
    public void testCreateSedan() throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException {
        Vehicle vehicle = VehicleCreator.Companion.getInstance().createVehicle("sedan", "S001", "Camry", 20000L);
        assertTrue(vehicle instanceof Sedan);
        assertEquals("S001", vehicle.getVehicleId());
        assertEquals("Camry", vehicle.getVehicleModel());
        assertEquals("Sedan", vehicle.getVehicleType());
    }

    @Test
    public void testCreateSUV() throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException {
        Vehicle vehicle = VehicleCreator.Companion.getInstance().createVehicle("suv", "U001", "CR-V", 25000L);
        assertTrue(vehicle instanceof SUV);
        assertEquals("U001", vehicle.getVehicleId());
        assertEquals("CR-V", vehicle.getVehicleModel());
        assertEquals("SUV", vehicle.getVehicleType());
    }

    @Test
    public void testCreatePickup() throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException {
        Vehicle vehicle = VehicleCreator.Companion.getInstance().createVehicle("pickup", "P001", "F-150", 30000L);
        assertTrue(vehicle instanceof Pickup);
        assertEquals("P001", vehicle.getVehicleId());
        assertEquals("F-150", vehicle.getVehicleModel());
        assertEquals("Pickup", vehicle.getVehicleType());
    }

    @Test
    public void testCreateSportsCar() throws InvalidVehicleTypeException, InvalidPriceException, MissingCriticalInfoException {
        Vehicle vehicle = VehicleCreator.Companion.getInstance().createVehicle("sports car", "SC001", "911", 90000L);
        assertTrue(vehicle instanceof SportsCar);
        assertEquals("SC001", vehicle.getVehicleId());
        assertEquals("911", vehicle.getVehicleModel());
        assertEquals("Sports car", vehicle.getVehicleType());
    }

    @Test
    public void testCreateInvalidTypeThrowsException() {
        assertThrows(InvalidVehicleTypeException.class, () -> {
            VehicleCreator.Companion.getInstance().createVehicle("hovercraft", "H001", "Hover", 50000L);
        });
    }
}