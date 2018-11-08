package com.twq

import com.twq.preparse.{PreParsedLog, WebLogPreparser}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}

object PreParserETL {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    if (args.isEmpty) {
      conf.setMaster("local")
    }

    val spark = SparkSession.builder()
      .appName("PreParserETL")
      .config(conf)
      .enableHiveSupport()
      .getOrCreate()

    val inputPath = spark.conf.get("spark.traffic.analysis.rawdata.input",
    "hdfs://master:9999/user/hadoop-twq/traffic-analysis/rawlog/20180616")

    val numPartitions = spark.conf.get("spark.traffic.analysis.rawdata.numberPartitions", "2").toInt

    val preparserLogRDD = spark.sparkContext.textFile(inputPath)

    val preParsedLogRDD = preparserLogRDD.flatMap(line => Option(WebLogPreparser.parse(line)))

    val preParsedLogDS = spark.createDataset(preParsedLogRDD)(Encoders.bean(classOf[PreParsedLog]))

    preParsedLogDS.coalesce(numPartitions)
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("year","month", "day")
      .saveAsTable("rawdata.web")

    spark.stop()
  }

}
