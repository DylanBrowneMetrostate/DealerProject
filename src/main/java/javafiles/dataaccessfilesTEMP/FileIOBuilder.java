package javafiles.dataaccessfilesTEMP;

/**
 * An abstract class whose inheritors creates and returns versions of {@link FileIO}s.
 *
 * @author Dylan Browne
 */
public interface FileIOBuilder {
    String[] getExtensions();
}
