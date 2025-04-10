package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;

import java.util.List;
import java.util.Map;

public interface FileIOReader {
    List<Map<Key, Object>> readInventory() throws ReadWriteException;
}
