package javafiles.dataaccessfiles.builderimplements

import javafiles.customexceptions.ReadWriteException
import javafiles.dataaccessfiles.FileIO
import javafiles.dataaccessfiles.fileioimplements.XMLIO
import java.io.File

internal class XMLIOBuilder(override val extensions: Array<String>) : FileIOReaderBuilder {
    @Throws(ReadWriteException::class)
    override fun createFileIOReader(path: String): FileIO {
        if (path.endsWith("pom.xml")) {
            throw ReadWriteException(
                """
                    Can't read or write to Maven's pom.xml file.
                    If this is not Maven's pom.xml file, rename it and try again.
                    """.trimIndent()
            )
        }
        return XMLIO(File(path))
    }
}
