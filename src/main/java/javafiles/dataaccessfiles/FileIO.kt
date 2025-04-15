package javafiles.dataaccessfiles

import java.io.File

/**
 * An abstract class whose inheritors reads and writes to different file types.
 *
 * @author Dylan Browne
 */
interface FileIO {
    val file: File
}
