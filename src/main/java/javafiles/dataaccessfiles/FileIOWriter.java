package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;

import java.util.List;
import java.util.Map;

public interface FileIOWriter {
    void writeInventory(List<Map<Key, Object>> maps) throws ReadWriteException;
}
