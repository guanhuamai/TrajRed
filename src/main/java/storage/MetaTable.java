package storage;
import redis.clients.jedis.Jedis;
import util.Util;

/**
 *
 * Created by mgh on 4/12/17.
 */


public class MetaTable {

    private Jedis jedis = null;
    private String dataName = null;

    private MetaTable(String dataName){
        jedis = new Jedis("localhost");
        jedis.select(0);
        this.dataName = dataName;
    }

    public static MetaTable createMetaTable(String dataName){
        return new MetaTable(dataName);
    }


    public void put(int partitionID, String hostAddress) {
        jedis.set(Util.concatWithDelim(dataName, partitionID, Util.REDIS_DELIMINATOR), hostAddress);
    }

    public String get(String key) {
        return jedis.get(Util.concatWithDelim(dataName, key, Util.REDIS_DELIMINATOR));
    }
}
