import org.apache.spark.rdd.RDD
import redis.clients.jedis.Jedis
import util.Util
import storage.{KVStorage, MetaTable, RedisStorage}
/**
  * Created by mgh on 4/12/17.
  */
class RDD2RedisStorage[T] extends RDD2KVStorage[T]{

  override def saveRDD(rdd: RDD[T], dataName: String) {
    val redisRDD = rdd.mapPartitionsWithIndex((partitionID, _) => {
      val localJedis = new Jedis("localhost")
      val hostName = localJedis.get("hostname")
      localJedis.close()
      Iterator(Tuple2(partitionID, hostName))
    })

    val metaTable = redisRDD.collect()

    redisRDD.sparkContext.broadcast(metaTable)

    redisRDD.mapPartitions((partitionElements) => {
      val localJedis = new Jedis("localhost")
      localJedis.select(1)

      partitionElements.foreach((meta) => {
        localJedis.set(meta._1.toString, meta._2)
      })

      partitionElements
    })

  }


  override def loadRDD(dataName: String): RDD[KVStorage] = ???
}
