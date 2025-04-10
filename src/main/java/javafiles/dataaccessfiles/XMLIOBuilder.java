package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

class XMLIOBuilder implements FileIOReaderBuilder {
    private final String[] EXTENSIONS;

    XMLIOBuilder(String[] extensions) {
        EXTENSIONS = extensions;
    }

    @Override
    public FileIO createFileIOReader(String path) throws ReadWriteException {
        return new XMLIO(path, 'r');
    }

    @Override
    public String[] getExtensions() {
        return EXTENSIONS;
    }
}
