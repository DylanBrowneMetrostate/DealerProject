package javafiles.domainfiles;

import javafiles.customexceptions.DealershipNotAcceptingVehiclesException;
import javafiles.customexceptions.DuplicateSenderException;
import javafiles.customexceptions.VehicleAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyTest {

    private Company company;
    private Dealership dealership1;
    private Dealership dealership2;
    private Vehicle mockVehicle;

    @BeforeEach
    public void setUp() throws VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException {
        company = new Company();
        dealership1 = new Dealership("D001", "Alpha Motors");
        dealership2 = new Dealership("D002", "Beta Autos");

        mockVehicle = new Sedan("V001", "Toyota", 20000L);
        dealership1.addIncomingVehicle(mockVehicle);

        dealership1.setRentingVehicles(true);
        dealership2.setRentingVehicles(false);

        company.addDealership(dealership1);
        company.addDealership(dealership2);
    }

    @Test
    public void testAddAndFindDealership() {
        Dealership result = company.findDealership("D001");
        assertNotNull(result);
        assertEquals("Alpha Motors", result.getDealerName());
    }

    @Test
    public void testGetDealershipIndex() {
        assertEquals(1, company.getDealershipIndex("D002"));
        assertEquals(-1, company.getDealershipIndex("D999"));
    }

    @Test
    public void testIsDealershipRentingEnabled() {
        assertTrue(company.isDealershipRentingEnabled("D001"));
        assertFalse(company.isDealershipRentingEnabled("D002"));
    }

    @Test
    public void testGetDealershipCompleteInventory() {
        ArrayList<Vehicle> inventory = dealership1.getTotalInventory();
        assertEquals(1, inventory.size());
        assertEquals("V001", inventory.get(0).getVehicleId());
    }

    @Test
    public void testUpdateVehicleRental_enablesAndMoves() throws Exception {
        mockVehicle.disableRental(); // currently not rentable
        dealership1.updateVehicleRental(mockVehicle);
        assertTrue(mockVehicle.getRentalStatus());
    }

    @Test
    public void testUpdateVehicleRental_throwsExceptionForSportsCar() throws VehicleAlreadyExistsException, DealershipNotAcceptingVehiclesException {
        Vehicle sportsCar = new SportsCar("V002", "911", 90000L);
        dealership1.addIncomingVehicle(sportsCar);
        assertThrows(Exception.class, () -> dealership1.updateVehicleRental(sportsCar));
    }

    @Test
    public void testRemoveVehicleFromDealership() {
        dealership1.removeVehicleFromInventory(mockVehicle);
        assertTrue(dealership1.getTotalInventory().isEmpty());
    }

    @Test
    public void testDealershipVehicleTransfer_success() throws Exception {
        dealership2.setReceivingVehicle(true);
        dealership1.dealershipVehicleTransfer(dealership2, mockVehicle);

        assertFalse(dealership1.getTotalInventory().contains(mockVehicle));
        assertTrue(dealership2.getTotalInventory().contains(mockVehicle));
    }

    @Test
    public void testDealershipVehicleTransfer_duplicateSenderException() {
        assertThrows(DuplicateSenderException.class, () -> dealership1.dealershipVehicleTransfer(dealership1, mockVehicle));
    }

    @Test
    public void testGetAllDealershipIds() {
        ArrayList<String> ids = company.getAllDealershipIds();
        assertTrue(ids.contains("D001"));
        assertTrue(ids.contains("D002"));
    }

    @Test
    public void testGetDealershipIdList() {
        String list = company.getDealershipIdList();
        assertTrue(list.contains("D001"));
        assertTrue(list.contains("D002"));
    }

    @Test
    public void testChangeReceivingStatus() {
        boolean result = company.changeReceivingStatus(0, "disable");
        assertFalse(result);
        assertFalse(dealership1.getStatusAcquiringVehicle());
    }

    @Test
    public void testChangeReceivingStatus_invalidInput() {
        boolean result = company.changeReceivingStatus(0, "unknown");
        assertTrue(result);
    }
}