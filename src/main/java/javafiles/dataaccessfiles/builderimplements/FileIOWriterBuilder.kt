package javafiles.dataaccessfiles.builderimplements

import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO

interface FileIOWriterBuilder : FileIOBuilder {
    @Throws(ReadWriteException::class)
    fun createFileIOWriter(path: String): FileIO
}
