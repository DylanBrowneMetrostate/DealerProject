package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.fileioimplements.FileIOReader
import javafiles.dataaccessfiles.fileioimplements.JSONIORead
import java.io.File

internal class JSONIOReadBuilder(override val extensions: Array<String>) : FileIOReaderBuilder {
    override fun createFileIO(path: String): FileIOReader {
        return JSONIORead(File(path))
    }
}
