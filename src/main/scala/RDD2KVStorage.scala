
import org.apache.spark.rdd.RDD
import storage.KVStorage

/**
  * Created by mgh on 4/12/17.
  */

trait RDD2KVStorage[T] {
  def saveRDD(rdd: RDD[T], dataName: String)

  def loadRDD(dataName: String): RDD[KVStorage]
}
