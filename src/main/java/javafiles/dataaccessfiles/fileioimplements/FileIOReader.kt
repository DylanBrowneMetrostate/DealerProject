package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOReader : FileIO {
    @Throws(ReadWriteException::class)
    fun readInventory(): List<Map<Key, Any>>
}
