package javafiles.dataaccessfiles.builderimplements

import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO
import javafiles.dataaccessfiles.fileioimplements.XMLIO
import java.io.File

internal class XMLIOBuilder(override val extensions: Array<String>) : FileIOReaderBuilder {
    @Throws(ReadWriteException::class)
    override fun createFileIOReader(path: String): FileIO {
        // pom.xml is no longer disallowed
        return XMLIO(File(path))
    }
}
