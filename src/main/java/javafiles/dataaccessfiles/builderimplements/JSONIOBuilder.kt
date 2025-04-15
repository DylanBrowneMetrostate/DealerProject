package javafiles.dataaccessfiles.builderimplements

import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.*
import javafiles.dataaccessfiles.fileioimplements.JSONIORead
import javafiles.dataaccessfiles.fileioimplements.JSONIOWrite
import java.io.File

internal class JSONIOBuilder(override val extensions: Array<String>) : FileIOReaderBuilder, FileIOWriterBuilder {
    @Throws(ReadWriteException::class)
    override fun createFileIOReader(path: String): FileIO {
        return JSONIORead(File(path))
    }

    @Throws(ReadWriteException::class)
    override fun createFileIOWriter(path: String): FileIO {
        return JSONIOWrite(File(path))
    }
}
