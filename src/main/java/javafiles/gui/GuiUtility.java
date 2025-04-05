package javafiles.gui;

import javafiles.Key;
import javafiles.customexceptions.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A utility class that contains commonly used functions for managing GUI elements,
 * specifically for creating and manipulating JTables to display data.
 */

public class GuiUtility {
    /**
     * An enum for linking Key.REASON_FOR_ERROR value with a message for the gui.
     */
    private enum CauseEnum {
        NUM_FORM(NumberFormatException.class, "Num Form", "Could not convert String to Long."),
        DUP(DuplicateKeyException.class, "Dup Key", "Conflicting keys in file."),
        VEHICLE_EXISTS(VehicleAlreadyExistsException.class, "Dup ID", "Vehicle already exists."),
        INVALID_PRICE(InvalidPriceException.class, "Price Err", "Price was null or <= 0."),
        DEAL_REC(DealershipNotAcceptingVehiclesException.class, "Rec Err", "Dealership is not receiving vehicles."),
        TYPE_ERR(InvalidVehicleTypeException.class, "Type", "Vehicle type or id is invalid."),
        MISSING(MissingCriticalInfoException.class, "Missing", "Missing critical information to create a vehicle");

        final String CLASS_NAME;
        final String KEY;
        final String REASON;
        CauseEnum(Class<? extends Exception> clazz, String key, String reason) {
            CLASS_NAME = clazz.getName();
            KEY = key;
            REASON = reason;
        }

        /**
         * Creates ane returns a List of Object[] to be used in the creation of the error JTable.
         *
         * @return the created List of Object[].
         */
        static List<Object[]> getListObjectArr() {
            List<Object[]> errorData = new ArrayList<>();
            for (CauseEnum key: CauseEnum.values()) {
                errorData.add(new Object[]{key.KEY, key.REASON});
            }

            return errorData;
        }

        /**
         * Checks if the given ReadWriteExceptions cause is one of the causes in CauseEnum. If it is,
         * returns the KEY, else returns "Unknown" or "ReadWriteException" depending on the existance of the
         * cause.
         *
         * @param exception The ReadWriteException from Key.REASONS_FOR_ERROR that is being evaluated.
         * @return The linked key for exception.
         */
        static String getCauseKey(ReadWriteException exception) {
            if (exception == null) {return "Unknown";}
            if (exception.getCause() == null) {return "ReadWriteException";}

            String causeName = exception.getCause().getClass().getName();

            for (CauseEnum key: CauseEnum.values()) {
                if (key.CLASS_NAME.equals(causeName)) {
                    return key.KEY;
                }
            }
            return "Unknown";
        }
    }

    /**
     * Creates and returns a new JScrollPane from the given JTable.
     *
     * @param table The table that is being added to the JScrollPanel.
     * @param message The title of the JScrollPanel.
     * @param height The preferred height of the JScrollPanel
     * @return the newly created JScrollPane.
     */
    private static JScrollPane createJScrollPane(JTable table, String message, int height) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(message));
        scrollPane.setPreferredSize(new java.awt.Dimension(500, height));

        return scrollPane;
    }

    /**
     * Sets the preferred column of each column to be the int in the same index as size.
     *
     * @param table The table whose preferred column sizes is being updated.
     * @param size An array of the size of columns being created.
     */
    private static void resizeTable(JTable table, int[] size) {
        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < size.length; i++) {
            TableColumn col = colModel.getColumn(i);
            col.setPreferredWidth(size[i]);
        }
    }

    /**
     * Creates and returns a new JTable with the given data and column names.
     *
     * @param data The list of rows of data for the JTable.
     * @param colNames The array of names for the columns in the JTable.
     * @return the newly created JTable.
     */
    private static JTable createJTable(List<Object[]> data, String[] colNames) {
        return new JTable(data.toArray(new Object[0][]), colNames);
    }

    /**
     * Shows a JPanel containing JTables for good and bad maps that were or were not added to Company, respectively.
     *
     * @param successData The list of rows of successfully added data for the JTable.
     * @param invalidData The list of rows of not successfully added data for the JTable.
     */
    public static void showMapFromFileInfo(List<Object[]> successData, List<Object[]> invalidData) {
        String[] columnNames = {"Vehicle ID", "Make", "Model", "Type", "Dealership ID"};
        String[] badColNames = {"Vehicle ID", "Make", "Model", "Type", "Dealership ID", "Error ID"};
        String[] errorColNames = {"Error ID", "Error Reason"};

        List<Object[]> errorData = CauseEnum.getListObjectArr();

        JTable successTable = createJTable(successData, columnNames);
        JTable invalidTable = createJTable(invalidData, badColNames);
        JTable errorTable = createJTable(errorData, errorColNames);

        resizeTable(errorTable, new int[]{100, 400});

        JScrollPane successScrollPane = createJScrollPane(successTable, "Successfully Added Vehicles", 150);
        JScrollPane invalidScrollPane = createJScrollPane(invalidTable, "Unable to Add Vehicles", 150);
        JScrollPane errorScrollPane = createJScrollPane(errorTable,"Error Reasons", 160);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(successScrollPane);
        panel.add(invalidScrollPane);
        panel.add(errorScrollPane);

        JOptionPane.showMessageDialog(null, panel, "Vehicle Import Results", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a JPanel containing the JTables for good and bad maps that were or were not added to Company, respectively.
     *
     * @param maps The List of all Maps read from a FileIO.
     * @param badMaps THe List of bad Maps that could not be added to Company.
     */
    public static void showMapTables(List<Map<Key, Object>> maps, List<Map<Key, Object>> badMaps) {
        List<Object[]> successData = new ArrayList<>();
        List<Object[]> invalidData = new ArrayList<>();

        for (Map<Key, Object> map : maps) {
            Object[] rowData = {
                    map.get(Key.VEHICLE_ID),
                    map.get(Key.VEHICLE_MANUFACTURER),
                    map.get(Key.VEHICLE_MODEL),
                    map.get(Key.VEHICLE_TYPE),
                    map.get(Key.DEALERSHIP_ID),
                    null
            };

            if (badMaps.contains(map)) {
                String message = CauseEnum.getCauseKey(Key.REASON_FOR_ERROR.getVal(map, ReadWriteException.class));
                rowData[rowData.length - 1] = message;
                invalidData.add(rowData);
            }
            else {
                successData.add(rowData);
            }
        }

        showMapFromFileInfo(successData, invalidData);
    }
}