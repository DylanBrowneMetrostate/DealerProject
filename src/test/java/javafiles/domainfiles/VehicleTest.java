package javafiles.domainfiles;

import javafiles.customexceptions.RentalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    private Sedan sedan;
    private SUV suv;
    private Pickup pickup;
    private SportsCar sportsCar;

    @BeforeEach
    public void setUp() {
        // Constructors based on your class definitions
        sedan = new Sedan("V001", "Camry", 20000L);
        suv = new SUV("V002", "CR-V", 25000L);
        pickup = new Pickup("V003", "F-150", 30000L);
        sportsCar = new SportsCar("V004", "911", 90000L);
    }

    @Test
    public void testGetVehicleType() {
        assertEquals("Sedan", sedan.getVehicleType());
        assertEquals("SUV", suv.getVehicleType());
        assertEquals("Pickup", pickup.getVehicleType());
        assertEquals("Sports car", sportsCar.getVehicleType());
    }

    @Test
    public void testVehicleAttributes() {
        assertEquals("Unknown", sedan.getVehicleManufacturer());
        assertEquals("Camry", sedan.getVehicleModel());
        assertEquals(20000L, sedan.getVehiclePrice());

        assertEquals("F-150", pickup.getVehicleModel());
        assertEquals(30000L, pickup.getVehiclePrice());
    }

    @Test
    public void testRentalStatusToggle() throws RentalException {
        assertFalse(suv.getRentalStatus());
        suv.enableRental();
        assertTrue(suv.getRentalStatus());
        suv.disableRental();
        assertFalse(suv.getRentalStatus());
    }

    @Test
    public void testVehicleIdUniqueness() {
        assertEquals("V001", sedan.getVehicleId());
        assertEquals("V004", sportsCar.getVehicleId());
    }
}