package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.fileioimplements.FileIOReader
import javafiles.dataaccessfiles.fileioimplements.XMLIO
import java.io.File

internal class XMLIOBuilder(override val extensions: Array<String>) : FileIOReaderBuilder {
    override fun createFileIO(path: String): FileIOReader {
        return XMLIO(File(path))
    }
}
