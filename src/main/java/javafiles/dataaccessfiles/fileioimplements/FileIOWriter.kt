package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOWriter : FileIO {
    @Throws(ReadWriteException::class)
    fun writeInventory(maps: List<Map<Key, Any>>)
}
