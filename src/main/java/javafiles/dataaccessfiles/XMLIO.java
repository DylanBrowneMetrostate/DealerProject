package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.DuplicateKeyException;
import javafiles.customexceptions.ReadWriteException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Links the tag name of in the .xml file with the appropriate {@link Key}.
 *
 * @author Dylan Browne
 */
enum XMLKey {
    D_ID (Key.DEALERSHIP_ID, "id"),
    D_NAME (Key.DEALERSHIP_NAME,  "name"),

    TYPE (Key.VEHICLE_TYPE, "type"),
    V_ID (Key.VEHICLE_ID, "id"),

    PRICE_UNIT (Key.VEHICLE_PRICE_UNIT, "unit"),
    PRICE (Key.VEHICLE_PRICE, "price"),

    MODEL (Key.VEHICLE_MODEL, "model"),
    MAKE (Key.VEHICLE_MANUFACTURER, "make"),

    REASON (Key.REASON_FOR_ERROR, "reason");

    private final Key KEY;
    private final String NAME;

    XMLKey(Key key, String xmlName) {
        KEY = key;
        NAME = xmlName;
    }

    public Key getKey() {return KEY;}
    public String getName() {return NAME;}
}

/**
 * A class that reads from XML files
 *
 * @author Dylan Browne
 */
class XMLIO extends FileIO {
    /**
     * Creates or opens an XML file with name path in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files. Writing is not enabled for {@link XMLIO} objects.
     * A write mode {@link XMLIO} object can be created, but trying to call writeInventory
     * will throw a {@link ReadWriteException}.
     *
     * @param path The full path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w')
     * @throws ReadWriteException if the mode is an invalid char or the file is recognized as pom.xml
     */
    public XMLIO(String path, char mode) throws ReadWriteException {
        super(path, mode);
        if (path.endsWith("pom.xml")) {
            throw new ReadWriteException("Can't read or write to Maven's pom.xml file.\n" +
                    "If this is not Maven's pom.xml file, rename it and try again.");
        }
    }

    /**
     * Takes information about a node or attribute and appends it to the given {@link Map} if
     * the tag name or attribute name is a valid name for the given {@link XMLKey}s.
     * </>
     * If a {@link XMLKey} was found but the nodeValue at that {@link XMLKey} causes an issue,
     * Key.REASON_FOR_ERROR + the reason for the issue is appended to map instead. Overrides the
     * previous Key.REASON_FOR_ERROR key values in the map
     *
     * @param keys An array of {@link XMLKey}s containing the {@link Key} to be searched for
     *             from tagName.
     * @param map The {@link Map} that the nodeValue or Key.REASON_FOR_ERROR is appended on to,
     *            if the tagName is found.
     * @param tagName The tag name of the {@link Node} that is being evaluated on this call.
     * @param nodeValue The value of the {@link Node} that is being evaluated. If the node was
     *                  originally an attribute, the nodeValue is just {@link XMLIO}.getNodeValue().
     *                  Otherwise, the nodeValue is the {@link String} concatenation of all the
     *                  child {@link Node}s with the getNodeName() of "#text".
     */
    private void parseNode(XMLKey[] keys, Map<Key, Object> map, String tagName, String nodeValue) {
        if (map == null || keys == null) {return;}

        nodeValue = nodeValue.strip();

        for (XMLKey key: keys) {

            if (key.getName().equalsIgnoreCase(tagName)) {
                // If there is an issue with the val, the first value is saved and the rest discarded.

                Object nodeValCast = nodeValue;
                if (key.getKey().getClassName().equals(Long.class.getName())) {
                    try {
                        nodeValCast = Long.parseLong(nodeValue);
                    } catch (NumberFormatException e) {
                        XMLKey.REASON.getKey().putValid(map, new ReadWriteException(e));
                        return;
                    }
                }
                if (map.containsKey(key.getKey()) && !map.get(key.getKey()).equals(nodeValCast)) {
                    String reason = "[" + map.get(key.getKey()) + "] != [" + nodeValue + "].";
                    DuplicateKeyException cause = new DuplicateKeyException(reason);
                    XMLKey.REASON.getKey().putValid(map, new ReadWriteException(cause));
                    return;
                }
                map.put(key.getKey(), nodeValCast);
                return;
            }

        }
    }

    /**
     * Calculates the value of a {@link Node} by taking a {@link NodeList} of their child
     * {@link Node}s and {@link String} concatenating all of the {@link Node}s text content
     * together, if the {@link Node}.getName() of the node starts with "#text".
     *
     * @param nodes The {@link NodeList} of child {@link Node}s being evaluated.
     * @return A {@link String} concatenation of the text in the appropriate child {@link Node}s.
     */
    private String parseNodeVal(NodeList nodes) {
       StringBuilder nodeVal = new StringBuilder();
       for (int i = 0; i < nodes.getLength(); i++) {
           Node node = nodes.item(i);
           if (node.getNodeName().startsWith("#text")) {
               nodeVal.append(node.getTextContent());
           }
       }
       return nodeVal.toString();
    }

    /**
     * Evaluates a {@link Node} node for the {@link Key}s in {@link XMLKey}.getKey() from the
     * array of {@link XMLKey} keys. The function will evaluate all of its child {@link Node}s,
     * and continue evaluating the child {@link Node}s of any child {@link Node}s evaluated until
     * all child {@link Node}s have been evaluated or the {@link Node}.getNodeName() equals stopTag.
     * If the {@link Node}.getNodeName() equals the stopTag, the {@link Node} that is being
     * evaluated is added to {@link List}<{@link Node}> haltedNodes. If a {@link Node} has a
     * {@link Key} being searched for, the calculated value of the {@link Node} is added to map
     * with that {@link Key}. If there is an error with the found value, Key.REASON_FOR_ERROR is
     * appended instead.
     *
     * @param node The {@link Node} that is being evaluated.
     * @param keys The array of {@link XMLKey}s that correspond to the {@link Key}s being searched for.
     * @param map The {@link Map}<{@link Key}, {@link Object}> where found keys and values are put.
     * @param stopTag The {@link String} name of the tag name of {@link Node}s that stop being evaluated.
     * @param haltedNodes All {@link Node}s that are stopped with stopTag are added to this {@link List}.
     */
    private void readXMLObject(Node node, XMLKey[] keys, Map<Key, Object> map, String stopTag, List<Node> haltedNodes) {
        String tagName = node.getNodeName();
        String nodeValue = parseNodeVal(node.getChildNodes());

        if (tagName.startsWith("#text")) {return;}

        if (tagName.equalsIgnoreCase(stopTag)) {
            if (haltedNodes != null) {
                haltedNodes.add(node);
            }
            return;
        }

        parseNode(keys, map, tagName, nodeValue);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node newNode = childNodes.item(i);
                readXMLObject(newNode, keys, map, stopTag, haltedNodes);
            }

            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node atr = attributes.item(i);
                parseNode(keys, map, atr.getNodeName(), atr.getNodeValue());
            }
        }
    }

    /**
     * Parses the given {@link Document} in order to return a {@link List} of
     * {@link Map}<{@link Key}, {@link Object}>s where each {@link Map} corresponds to the info
     * held in a single Vehicle. If a tag name of a {@link Node} is not recognized, it is discarded
     * but its child {@link Node}s are still evaluated. If a {@link Key} is found and an issue with
     * the value is also found, the map is not discarded but rather a {@link Key}.REASON_FOR_ERROR
     * is added instead.
     *
     * @param document The {@link Document} that is being parsed.
     * @return a {@link List}<{@link Map}>s where each {@link Map} corresponds to a single Vehicle.
     */
    private List<Map<Key, Object>> parseDocument(Document document) {
        List<Map<Key, Object>> maps = new ArrayList<>();

        Element documentElement = document.getDocumentElement();

        List<Node> haltedNodesDealer = new ArrayList<>();

        readXMLObject(documentElement, null, null, "Dealer", haltedNodesDealer);

        XMLKey[] dealerKeys = {XMLKey.D_ID, XMLKey.D_NAME};
        XMLKey[] vehicleKeys = {XMLKey.TYPE, XMLKey.V_ID, XMLKey.PRICE,
                XMLKey.PRICE_UNIT, XMLKey.MAKE, XMLKey.MODEL};

        for (Node dealerNode : haltedNodesDealer) {

            List<Node> haltedNodesVehicle = new ArrayList<>();
            Map<Key, Object> dealerMap = new HashMap<>();
            readXMLObject(dealerNode, dealerKeys, dealerMap, "Vehicle", haltedNodesVehicle);

            for (Node vehicleNode : haltedNodesVehicle) {
                Map<Key, Object> vehicleMap = new HashMap<>(dealerMap);
                readXMLObject(vehicleNode, vehicleKeys, vehicleMap, null, null);
                maps.add(vehicleMap);
            }

        }

        return maps;
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return A List of {@link Map}<{@link Key}, {@link Object}>s that correspond to the
     *         data stored in the XML file for this object.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    public List<Map<Key, Object>> readInventory() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ReadWriteException(e);
        }

        return parseDocument(document);
    }

    /**
     * Throws a {@link ReadWriteException} as {@link XMLIO} can not be used to write to files.
     * Overrides {@link FileIO} abstract method.
     *
     * @param maps Discarded.
     * @throws ReadWriteException Always thrown.
     */
    public void writeInventory(List<Map<Key, Object>> maps) throws ReadWriteException {
        throw new ReadWriteException("Can not write to XML Files.");
    }
}
