package javafiles.dataaccessfiles.builderimplements

import javafiles.dataaccessfiles.FileIO

/**
 * An abstract class whose inheritors creates and returns versions of [FileIO]s.
 *
 * @author Dylan Browne
 */
interface FileIOBuilder {
    val extensions: Array<String>
    fun createFileIO(path: String): FileIO
}
