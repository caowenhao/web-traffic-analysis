package com.twq
import org.apache.spark.sql.SparkSession
object test {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("PreParserETL")
      .master("local")
      .getOrCreate()

    val preparserLogRDD = spark.sparkContext.parallelize(Seq(1,2,null,3)).flatMap(x => Option(x))

    preparserLogRDD.foreach(println(_))

    spark.stop()
  }

}
