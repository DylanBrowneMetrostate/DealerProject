package javafiles.dataaccessfiles.builderimplements

import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOReaderBuilder : FileIOBuilder {
    @Throws(ReadWriteException::class)
    fun createFileIOReader(path: String): FileIO
}
