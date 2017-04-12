package storage;

import redis.clients.jedis.Jedis;
import util.Util;


/**
 *
 * Created by mgh on 4/12/17.
 */
public class RedisStorage implements KVStorage{

    private Jedis jedis = null;
    private int partitionID = 0;
    private RedisStorage(int partitionID){
        KVStorage metaStorage = MetaStorage.createMetaKVStorage();
        String ipAddress = metaStorage.get(Util.genMetaKey(partitionID));
        this.jedis = new Jedis(ipAddress);
        this.jedis.select(1);
        this.partitionID = partitionID;
    }

    public static RedisStorage createRedisStorage(int partitionID){
        return new RedisStorage(partitionID);
    }


    public void put(String key, String value) {
        jedis.set(Util.genDataKey(partitionID, key), value);
    }

    public String get(String key) {
        return jedis.get(Util.genDataKey(partitionID, key));
    }

}
