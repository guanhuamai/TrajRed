package storage;

/**
 *
 * Created by mgh on 4/12/17.
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
