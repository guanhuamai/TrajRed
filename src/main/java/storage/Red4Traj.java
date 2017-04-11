package storage;

import util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.*;
/**
 * coded by mgh
 */
public class Red4Traj implements KVStorage{
    private Jedis localJedis = null;
    private int numPartitions = 0;

    public Red4Traj(int numPartitions){
        this.localJedis = new Jedis("localhost");
        this.numPartitions = numPartitions;
    }

    public void put(int partitionID, String key, String value) {
        int redisId = Util.getLocalDBId(partitionID, numPartitions);
        localJedis.select(redisId);
        localJedis.set(key, value);

        // question:
        // update the meta data of the local machine
        // actually we should move the below operation to setMetaLocal
        localJedis.select(Util.getMetaDBId(numPartitions));
        String localAddress = Util.getLocalIP();
        localJedis.set(Util.genMetaKey(partitionID), Util.genMetaValue(localAddress, numPartitions));
    }

    // question:
    // assume we have high performance when the remoteJedis is actually localJedis
    // actually not for sure
    public String get(int partitionID, String key) {
        localJedis.select(Util.getMetaDBId(numPartitions));
        if (!localJedis.exists(Util.genMetaKey(partitionID))){
            return null;
        }

        String remoteJedInfo = localJedis.get(Util.genMetaKey(partitionID));
        String[] parsedMetaValue = Util.parseMetaValue(remoteJedInfo);
        String remoteAddress = parsedMetaValue[0];
        int remotePartitions = Integer.valueOf(parsedMetaValue[1]);

        Jedis remoteJedis = new Jedis(remoteAddress);
        remoteJedis.select(Util.getLocalDBId(partitionID, remotePartitions));
        return remoteJedis.get(key);
    }

    public Map<Integer, String> getMetaLocal() {
        localJedis.select(numPartitions);  // select the local meta db

        List<String> keys = localJedis.scan("0").getResult(); // scan keys in meta
        Map<Integer, String> res = new HashMap<Integer, String>();
        for (String key: keys){
            res.put(Integer.parseInt(key), localJedis.get(key));
        }

        return res;
    }

    public void setMetaLocal(Map<Integer, String> newMetaTable) {
        localJedis.select(numPartitions);
        localJedis.flushDB();
        for(int k : newMetaTable.keySet()){
            localJedis.set(Integer.toString(k), newMetaTable.get(k));
        }
    }

}
