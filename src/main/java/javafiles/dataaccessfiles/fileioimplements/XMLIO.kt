package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.DuplicateKeyException
import javafiles.customexceptions.ReadWriteException

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.File

import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

import kotlin.collections.ArrayList
import java.util.EnumMap

/**
 * Links the tag name of in the .xml file with the appropriate [Key].
 *
 * @author Dylan Browne
 */
internal enum class XMLKey(val key: Key, val xmlName: String) {
    D_ID(Key.DEALERSHIP_ID, "id"),
    D_NAME(Key.DEALERSHIP_NAME, "name"),

    TYPE(Key.VEHICLE_TYPE, "type"),
    V_ID(Key.VEHICLE_ID, "id"),

    PRICE_UNIT(Key.VEHICLE_PRICE_UNIT, "unit"),
    PRICE(Key.VEHICLE_PRICE, "price"),

    MODEL(Key.VEHICLE_MODEL, "model"),
    MAKE(Key.VEHICLE_MANUFACTURER, "make"),

    REASON(Key.REASON_FOR_ERROR, "reason")
}

/**
 * A class that reads from XML files
 *
 * @author Dylan Browne
 */
internal class XMLIO
    /**
     * Creates or opens an XML file with name path in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files. Writing is not enabled for [XMLIO] objects.
     * A write mode [XMLIO] object can be created, but trying to call writeInventory
     * will throw a [ReadWriteException].
     *
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    constructor(override val file: File) : FileIOReader {
    /**
     * Takes information about a node or attribute and appends it to the given [Map] if
     * the tag name or attribute name is a valid name for the given [XMLKey]s.
     * >
     * If a [XMLKey] was found but the nodeValue at that [XMLKey] causes an issue,
     * Key.REASON_FOR_ERROR + the reason for the issue is appended to map instead. Overrides the
     * previous Key.REASON_FOR_ERROR key values in the map
     *
     * @param keys An array of [XMLKey]s containing the [Key] to be searched for
     * from tagName.
     * @param map The [Map] that the nodeValue or Key.REASON_FOR_ERROR is appended on to,
     * if the tagName is found.
     * @param tagName The tag name of the [Node] that is being evaluated on this call.
     * @param nodeValue The value of the [Node] that is being evaluated. If the node was
     * originally an attribute, the nodeValue is just [XMLIO].getNodeValue().
     * Otherwise, the nodeValue is the [String] concatenation of all the
     * child [Node]s with the getNodeName() of "#text".
     */
    private fun parseNode(keys: Array<XMLKey>?, map: MutableMap<Key, Any>?, tagName: String, nodeValue: String) {
        // want to change value, but (seemingly) not allowed to without name shadowing.
        @Suppress("NAME_SHADOWING") var nodeValue = nodeValue

        if (map == null || keys == null) {
            return
        }

        nodeValue = nodeValue.trim()

        for (key in keys) {
            if (key.xmlName.equals(tagName, ignoreCase = true)) {
                // If there is an issue with the val, the first value is saved and the rest discarded.
                val nodeValCast = if (key.key.className == java.lang.Long::class.java.name) {
                    try {
                        nodeValue.toLong()
                    } catch (e: NumberFormatException) {
                        XMLKey.REASON.key.putValid(map, ReadWriteException(e))
                        return
                    }
                } else { nodeValue }

                if (map.containsKey(key.key) && map[key.key] != nodeValCast) {
                    val reason = "Key " + key.xmlName + " already has a value and [" +
                                 map[key.key].toString() + "] != [" + nodeValue + "]."
                    val cause = DuplicateKeyException(reason)
                    XMLKey.REASON.key.putValid(map, ReadWriteException(cause))
                    return
                }
                map[key.key] = nodeValCast
                return
            }
        }
    }

    /**
     * Calculates the value of a [Node] by taking a [NodeList] of their child
     * [Node]s and [String] concatenating all of the [Node]s text content
     * together, if the [Node].getName() of the node starts with "#text".
     *
     * @param nodes The [NodeList] of child [Node]s being evaluated.
     * @return A [String] concatenation of the text in the appropriate child [Node]s.
     */
    private fun parseNodeVal(nodes: NodeList): String {
        val nodeValBuilder = StringBuilder()
        for (i in 0..<nodes.length) {
            val node = nodes.item(i)
            if (node.nodeName.startsWith("#text")) {
                nodeValBuilder.append(node.textContent)
            }
        }
        return nodeValBuilder.toString()
    }

    /**
     * Evaluates a [Node] node for the [Key]s in [XMLKey].getKey() from the
     * array of [XMLKey] keys. The function will evaluate all of its child [Node]s,
     * and continue evaluating the child [Node]s of any child [Node]s evaluated until
     * all child [Node]s have been evaluated or the [Node].getNodeName() equals stopTag.
     * If the [Node].getNodeName() equals the stopTag, the [Node] that is being
     * evaluated is added to [List]<[Node]> haltedNodes. If a [Node] has a
     * [Key] being searched for, the calculated value of the [Node] is added to map
     * with that [Key]. If there is an error with the found value, Key.REASON_FOR_ERROR is
     * appended instead.
     *
     * @param node The [Node] that is being evaluated.
     * @param keys The array of [XMLKey]s that correspond to the [Key]s being searched for.
     * @param map The [Map]<[Key], [Object]> where found keys and values are put.
     * @param stopTag The [String] name of the tag name of [Node]s that stop being evaluated.
     * @param haltedNodes All [Node]s that are stopped with stopTag are added to this [List].
     */
    private fun readXMLObject(
        node: Node, keys: Array<XMLKey>?, map: MutableMap<Key, Any>?,
        stopTag: String?, haltedNodes: MutableList<Node>?)
    {
        val tagName = node.nodeName
        val nodeValue = parseNodeVal(node.childNodes)

        if (tagName.startsWith("#text")) {
            return
        }

        if (tagName.equals(stopTag, ignoreCase = true)) {
            haltedNodes?.add(node)
            return
        }

        parseNode(keys, map, tagName, nodeValue)

        if (node.nodeType == Node.ELEMENT_NODE) {
            val element = node as Element

            val childNodes = element.childNodes
            for (i in 0..<childNodes.length) {
                val newNode = childNodes.item(i)
                readXMLObject(newNode, keys, map, stopTag, haltedNodes)
            }

            val attributes = element.attributes
            for (i in 0..<attributes.length) {
                val atr = attributes.item(i)
                parseNode(keys, map, atr.nodeName, atr.nodeValue)
            }
        }
    }

    /*
     * So long as the tag is in the correct region, tags within tags is fine.
     * It is possible to parse values from a String with a tag within that
     * String (though it has a possibility of white space errors).
     *
     * Region: <Any Tag Name> -> <Dealer>
     * Tag / Attributes: None
     *
     * Region: <Dealer> -> <Vehicle>
     * Tag / Attributes: ID, Name
     *
     * Region: <Vehicle> -> <Rest of the Tags>
     * Tag / Attributes: ID, Type, Unit, Price, Make, Model
     */
    /**
     * Parses the given [Document] in order to return a [List] of
     * [Map]<[Key], [Object]>s where each [Map] corresponds to the info
     * held in a single Vehicle. If a tag name of a [Node] is not recognized, it is discarded
     * but its child [Node]s are still evaluated. If a [Key] is found and an issue with
     * the value is also found, the map is not discarded but rather a [Key].REASON_FOR_ERROR
     * is added instead.
     *
     * @param document The [Document] that is being parsed.
     * @return a [List]<[Map]> where each [Map] corresponds to a single Vehicle.
     */
    private fun parseDocument(document: Document): List<Map<Key, Any>> {
        val maps: MutableList<Map<Key, Any>> = ArrayList()

        val documentElement = document.documentElement

        val haltedNodesDealer: MutableList<Node> = ArrayList()

        readXMLObject(documentElement, null, null, "Dealer", haltedNodesDealer)

        val dealerKeys = arrayOf(XMLKey.D_ID, XMLKey.D_NAME)
        val vehicleKeys = arrayOf(
            XMLKey.TYPE, XMLKey.V_ID, XMLKey.PRICE,
            XMLKey.PRICE_UNIT, XMLKey.MAKE, XMLKey.MODEL
        )

        for (dealerNode in haltedNodesDealer) {

            val haltedNodesVehicle: MutableList<Node> = ArrayList()
            val dealerMap: MutableMap<Key, Any> = EnumMap(javafiles.Key::class.java)
            readXMLObject(dealerNode, dealerKeys, dealerMap, "Vehicle", haltedNodesVehicle)

            for (vehicleNode in haltedNodesVehicle) {
                val vehicleMap: MutableMap<Key, Any> = EnumMap(dealerMap)
                readXMLObject(vehicleNode, vehicleKeys, vehicleMap, null, null)
                maps.add(vehicleMap)
            }

        }

        return maps
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return A List of Map<Key></Key>, Object>s that correspond to the
     * data stored in the XML file for this object.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    @Throws(ReadWriteException::class)
    override fun readInventory(): List<Map<Key, Any>> {
        val factory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder
        val document: Document

        try {
            builder = factory.newDocumentBuilder()
            document = builder.parse(file)
        } catch (e: Exception) {
            // TODO: More testing needed to make sure this works as expected
            when(e) {
                is ParserConfigurationException, is SAXException, is IOException -> {throw ReadWriteException(e)}
                else -> throw e
            }
        }

        return parseDocument(document)
    }
}
