package storage;

import redis.clients.jedis.Jedis;

/**
 *
 * Created by mgh on 4/12/17.
 */
public class MetaStorage implements KVStorage{

    private Jedis jedis = null;

    private MetaStorage(){
        jedis = new Jedis("localhost");
        jedis.select(0);
    }

    /**
     * @return key value storage object
     */
    public static MetaStorage createMetaKVStorage(){
        return new MetaStorage();
    }

    public void put(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }
}
