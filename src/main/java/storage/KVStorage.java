package storage;

import java.util.Map;

/**
 * coded by mgh
 */
public interface KVStorage{

    /**
     *  write key into local storage
     **/
    void put(int partitionID, String key, String value);

    /**
     * get value using key and partition ID
     **/
    String get(int partitionID, String key);


    /**
     * get local meta table. K: partition id,  V: partition location
     */
    Map<Integer, String> getMetaLocal();

    /**
     * set local meta table
     */
    void setMetaLocal(Map<Integer, String> newMetaTable);
}
