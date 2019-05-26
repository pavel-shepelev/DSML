import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row

object WordCount {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(conf)

    val text = sc.textFile("pulp-fiction.txt")
    val words = text.flatMap(line => line.toLowerCase().split(" ")).map(word => (word,1)).reduceByKey(_ + _)
    words.collect()

    val voc = sc.textFile("voc.txt")
    val keys = voc.flatMap(line => line.toLowerCase().split(" ")).map(word => (word,1)).reduceByKey(_ + _)
    keys.collect()

    val common = words.join(keys).map(x => (x._1 , x._2._1))
    common.collect().foreach(println)

    //(gun,26) (damn,4) (hell,5) (burger,4) (shit,39)

    words.saveAsTextFile("output")

  }
}
