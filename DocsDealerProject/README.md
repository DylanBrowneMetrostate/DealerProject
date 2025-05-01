# DealershipProject
A tracking system for a company that owns multiple car dealerships.

## Table of Contents
- [Overview](#overview)
- [Build Instructions](#build-instructions)
- [Inventory Management](#inventory-management)
  - [Adding Vehicles to a Dealership](#adding-vehicles-to-a-dealership)
  - [Removing Vehicles from a Dealership](#removing-vehicles-from-a-dealership)
  - [Transferring Vehicles Between Dealerships](#transferring-vehicles-between-dealerships)
  - [Modifying Vehicle Rental Status](#modifying-vehicle-rental-status)
  - [Viewing the Company's Inventory](#viewing-the-companys-inventory)
- [Dealership Management](#dealership-management)
  - [Adding a New Dealership](#adding-a-new-dealership)
  - [Editing a Dealership's Name](#editing-a-dealerships-name)
  - [Changing Dealership Receiving Status](#changing-dealership-receiving-status)
  - [Changing Dealership Rental Status](#changing-dealership-rental-status)
- [Authors](#authors)

## Overview
The Dealership Management System is a Java-based application designed to help companies efficiently manage vehicle inventories across multiple dealerships. This system provides a graphical user interface (GUI) for performing various inventory and dealership management tasks, including:

- Adding, removing, and transferring vehicles between dealerships.
- Modifying vehicle rental statuses.
- Viewing and managing the company's entire inventory.
- Adding and editing dealership profiles, including toggling their receiving and rental statuses.
- Saving and loading vehicle data from a file.
- Managing erronious vehicle data loaded from a file.

The system ensures that all changes to the inventory are automatically saved to a `masterinventory.json` file, providing a reliable and persistent data storage solution.

## Build Instructions
To build the project, navigate to the project directory and run:
```bash
gradlew build
```
To run the project, use the following command:
```bash
gradlew run
```

### Inventory Management
The program provides a graphical user interface for managing vehicles for a company's car dealerships. The user is provided with the following options:

1. **Adding vehicles to a Dealership**
    - From the main menu, select Manage Company Inventory.
    - Select add Vehicle to Dealership.
    - Choose from the following options:
        - Load from file.
        - Enter manually.
    - Vehicles will be sent to the respective dealerships.
    - A message will be displayed indicating whether the vehicle was successfully added or if there was an error.

2. **Removing vehicles from a Dealership**
    - From the main menu, select Manage Company Inventory.
    - Select Remove Vehicle from Dealership.
    - Select the Dealership ID from which you want to remove the vehicle.
    - Click on the vehicle you want to remove from the list.
    - Click the Remove button to remove the vehicle from the dealership.

3. **Transferring vehicles between Dealerships**
    - From the main menu, select Manage Company Inventory.
    - Select Transfer Vehicle Between Dealerships.
    - Select the dealership sending the vehicle from the dropdown menu.
    - Select the dealership receiving the vehicle from the dropdown menu.
    - Click on the vehicle you want to transfer from the list.
    - Click the Transfer button to transfer the vehicle between dealerships.    

4. **Modifying vehicles Rental Status**
    - From the main menu, select Manage Company Inventory.
    - Select Modify Vehicle Rental Status.
    - Select the Dealership ID from which you want to modify the vehicle rental status.
    - Click on the vehicle you want to modify from the list.
    - Click the Change Rental button to change the rental status of the vehicle.

5. **Viewing the Company's Inventory**
    - From the main menu, select Manage Company Inventory.
    - Select View Company Inventory.
    - This screen provides a list of all vehicles in the company inventory, including their details and rental status.
    - The company inventory is automatically saved to `masterinventory.json` whenever changes are made.

6. **Saving Company Inventory to a File**
    - From the main menu, select Manage Company Inventory.
    - Select Save Company Inventory to File.
    - Choose an existing JSON file where you want to save the inventory data.
    - The inventory will be saved in JSON format.

### Dealership Management
1. **Adding a new Dealership**
    - From the main menu, select Manage Company Profile.
    - Select Add a Dealership.
    - Enter the dealership ID and name (optional).
    - The new dealership will be added to the company profile.


2. **Editing a Dealership's Name**
    - From the main menu, select Manage Company Profile.
    - Click on the dealership you want to edit from the list.
    - Click the Edit Dealership Name button.
    - Enter the new name for the dealership.
    - Click ok to save the changes.
    - The dealership name will be updated in the company profile.


3. **Changing Dealership receiving status**
    - From the main menu, select Manage Company Profile.
    - Click on the dealership you want to edit from the list.
    - Click the Change Receiving Status button to toggle the receiving status of the dealership.

4. **Changing Dealership rental status**
    - From the main menu, select Manage Company Profile.
    - Click on the dealership you want to edit from the list.
    - Click the Change Rental Status button to toggle the rental status of the dealership.

### Handling Erroneous Vehicle Data
- If a vehicle is loaded from a file and has erroneous data, the program will display an error message indicating the issue.
- From the main menu, select Manage Bad Inventory.

1. ** Correcting Erroneous Vehicle Data**
    - Select the vehicle data to be modified by clicking the checkbox under the Select column.
    - Click on the field you want to modify.
    - Enter the corrected data in the provided fields.
    - Click Upload Selected.
    - The selected vehicle will be added to the company's inventory.

2. **Removing Erroneous Vehicle Data**
    - Select the vehicle data to be removed by clicking the checkbox under the Select column.
    - Click Remove Selected to remove the selected vehicle from the list.
    - The selected vehicle will be removed from the erroneous data list.
    

## Authors
- Dylan Browne
- Chris Engelhart
- Mason Day
- Patrick McLucas