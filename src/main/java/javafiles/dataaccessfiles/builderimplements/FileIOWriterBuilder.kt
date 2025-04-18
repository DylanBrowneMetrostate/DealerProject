package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.fileioimplements.FileIOWriter

interface FileIOWriterBuilder : FileIOBuilder {
    override fun createFileIO(path: String): FileIOWriter
}
