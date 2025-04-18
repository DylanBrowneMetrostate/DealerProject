package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.fileioimplements.FileIOWriter
import javafiles.dataaccessfiles.fileioimplements.JSONIOWrite
import java.io.File

internal class JSONIOWriteBuilder(override val extensions: Array<String>) : FileIOWriterBuilder {
    override fun createFileIO(path: String): FileIOWriter {
        return JSONIOWrite(File(path))
    }
}
