package javafiles.dataaccessfilesTEMP;

import javafiles.customexceptions.ReadWriteException;

class JSONIOBuilder implements FileIOReaderBuilder, FileIOWriterBuilder {
    private final String[] EXTENSIONS;

    JSONIOBuilder(String[] extensions) {
        EXTENSIONS = extensions;
    }

    @Override
    public FileIO createFileIOReader(String path) throws ReadWriteException {
        return new JSONIO(path, 'r');
    }

    @Override
    public FileIO createFileIOWriter(String path) throws ReadWriteException {
        return new JSONIO(path, 'w');
    }

    @Override
    public String[] getExtensions() {
        return EXTENSIONS;
    }
}
