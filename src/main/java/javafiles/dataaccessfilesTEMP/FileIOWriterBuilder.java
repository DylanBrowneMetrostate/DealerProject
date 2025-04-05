package javafiles.dataaccessfilesTEMP;

import javafiles.customexceptions.ReadWriteException;

public interface FileIOWriterBuilder extends FileIOBuilder{
    FileIO createFileIOWriter(String path) throws ReadWriteException;
}
