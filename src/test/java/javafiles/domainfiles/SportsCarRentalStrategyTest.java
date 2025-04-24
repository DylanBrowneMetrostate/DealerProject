package javafiles.domainfiles;

import javafiles.customexceptions.SportsCarRentalNotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SportsCarRentalStrategyTest {

    @Test
    public void testEnableRentalThrowsException() {
        fail("TODO: Re-implement test: testEnableRentalThrowsException.");
        /*
        SportsCarRentalStrategy strategy = new SportsCarRentalStrategy();
        Vehicle dummyVehicle = new SportsCar("SC001", "911", 90000L);

        assertThrows(SportsCarRentalNotAllowedException.class, () -> {
            strategy.enableRental(dummyVehicle);
        }, "Expected enableRental to throw for SportsCar");
        */
    }

    @Test
    public void testDisableRentalThrowsException() {
        fail("TODO: Re-implement test: testDisableRentalThrowsException.");
        /*
        SportsCarRentalStrategy strategy = new SportsCarRentalStrategy();
        Vehicle dummyVehicle = new SportsCar("SC001", "911", 90000L);

        assertThrows(SportsCarRentalNotAllowedException.class, () -> {
            strategy.disableRental(dummyVehicle);
        }, "Expected disableRental to throw for SportsCar");
        */
    }
}