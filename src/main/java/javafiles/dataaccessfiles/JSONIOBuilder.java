package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

class JSONIOBuilder extends FileIOBuilder {
    JSONIOBuilder(String[] extensions) {
        super(extensions);
    }

    /**
     * Creates and returns a new {@link JSONIO} with parameters (path, mode).
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file that is created (read 'r' or write 'w').
     * @return the newly created {@link JSONIO}.
     * @throws ReadWriteException if the mode is an invalid char.
     */
    @Override
    protected JSONIO createFileIO(String path, char mode) throws ReadWriteException {
        return new JSONIO(path, mode);
    }

    /**
     * Returns a list o list of {@link String}s that correspond with the extensions of
     * files that can be created with the given mode. Since {@link JSONIO} can read,
     * and write, it just returns EXTENSIONS.
     *
     * @param mode A char representation of the mode associated with the extensions (read 'r' or write 'w').
     * @return an array of {@link String}s corresponding to the extensions that can be written.
     */
    @Override
    protected String[] getExtensions(char mode) {
        return EXTENSIONS;
    }
}
