package javafiles.dataaccessfiles.fileioimplements

import javafiles.Key
import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOWriter : FileIO {
    fun writeInventory(maps: Map<Map<Key, Any>, List<Map<Key, Any>>>)
}
