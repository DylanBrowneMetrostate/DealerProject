package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOReader : FileIO {
    fun readInventory(): List<Map<Key, Any>>
}
