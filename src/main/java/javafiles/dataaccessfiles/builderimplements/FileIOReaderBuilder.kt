package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.fileioimplements.FileIOReader

interface FileIOReaderBuilder : FileIOBuilder {
    override fun createFileIO(path: String): FileIOReader
}
