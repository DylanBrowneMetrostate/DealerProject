package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

class XMLIOBuilder extends FileIOBuilder {
    XMLIOBuilder(String[] extensions) {
        super(extensions);
    }

    /**
     * Creates and returns a new {@link XMLIO} with parameters (path, mode).
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file that is created (read 'r' or write 'w').
     * @return the newly created {@link XMLIO}.
     * @throws ReadWriteException if the mode is an invalid char or the file is recognized as pom.xml
     */
    @Override
    protected XMLIO createFileIO(String path, char mode) throws ReadWriteException {
        return new XMLIO(path, mode);
    }

    /**
     * Returns a list o list of {@link String}s that correspond with the extensions of
     * files that can be created with the given mode. Since {@link XMLIO} cannot write,
     * an empty array is returned in write mode. Read mode returns EXTENSIONS.
     *
     * @param mode A char representation of the mode associated with the extensions (read 'r' or write 'w').
     * @return an array of {@link String}s corresponding to the extensions that can be written with the mode.
     */
    @Override
    protected String[] getExtensions(char mode) {
        if (mode == 'r') {
            return EXTENSIONS;
        } else {
            return new String[]{};
        }
    }
}
