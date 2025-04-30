package javafiles.domainfiles;

import javafiles.customexceptions.SportsCarRentalNotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SportsCarRentalStrategyTest {

    @Test
    public void testEnableRentalThrowsException() {
        SportsCarRentalStrategy strategy = new SportsCarRentalStrategy();

        assertThrows(SportsCarRentalNotAllowedException.class, () -> {
            strategy.updateTo(true);
        }, "Expected updateTo(true) to throw for SportsCar");

    }

    @Test
    public void testDisableRentalThrowsException() {
        SportsCarRentalStrategy strategy = new SportsCarRentalStrategy();

        assertThrows(SportsCarRentalNotAllowedException.class, () -> {
            strategy.updateTo(false);
        }, "Expected disableRental to throw for SportsCar");

    }
}