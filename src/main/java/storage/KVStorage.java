package storage;

import java.util.Map;

/**
 * coded by mgh
 */
public interface KVStorage{

    /**
     *  write key into local storage
     **/
    void put(String key, String value);

    /**
     * get value using key and partition ID
     **/
    String get(String key);

}
