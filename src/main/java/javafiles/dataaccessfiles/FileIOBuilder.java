package javafiles.dataaccessfiles;

/**
 * An abstract class whose inheritors creates and returns versions of {@link FileIO}s.
 *
 * @author Dylan Browne
 */
interface FileIOBuilder {
    String[] getExtensions();
}
