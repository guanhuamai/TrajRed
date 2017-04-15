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
    private String dataName = null;


    private RedisStorage(int partitionID, String dataName, String host){
        this.jedis = new Jedis(host);
        this.jedis.select(1);
        this.partitionID = partitionID;
        this.dataName = dataName;
    }

    private String genKey(String key){
        String prefix = Util.concatWithDelim(dataName, partitionID, Util.REDIS_DELIMINATOR);
        return Util.concatWithDelim(prefix, key, Util.REDIS_DELIMINATOR);
    }


    public static RedisStorage createRedisStorage(int partitionID, String dataName, String host){
        return new RedisStorage(partitionID, dataName, host);
    }

    public void put(String key, String value) {
        jedis.set(genKey(key), value);
    }

    public String get(String key) {
        return jedis.get(genKey(key));
    }

}
