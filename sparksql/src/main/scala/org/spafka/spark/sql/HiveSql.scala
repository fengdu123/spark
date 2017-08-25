package org.spafka.spark.sql

import java.sql.DriverManager

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object HiveSql {

  def main(args: Array[String]): Unit = {


    // locacl模式必须保证hdfs\本地/tmp/hive 有777权限
//    val hiveConf = new SparkConf().setMaster("local[*]").setAppName("hive")
//    val sc = new SparkContext(hiveConf);
//    val hiveContext = new HiveContext(sc)
//    hiveContext.sql("show databases").show()

    val spark = SparkSession.builder().appName("thrift").master("local[*]").enableHiveSupport().getOrCreate()

    import spark.sql()
    sql("show databases").show()
    spark.stop()
  }

}
