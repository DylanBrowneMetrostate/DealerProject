package javafiles.dataaccessfiles.builderimplements

/**
 * An abstract class whose inheritors creates and returns versions of [FileIO]s.
 *
 * @author Dylan Browne
 */
interface FileIOBuilder {
    val extensions: Array<String>
}
