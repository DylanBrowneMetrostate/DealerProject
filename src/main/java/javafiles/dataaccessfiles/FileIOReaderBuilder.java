package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

public interface FileIOReaderBuilder extends FileIOBuilder{
    FileIO createFileIOReader(String path) throws ReadWriteException;
}
