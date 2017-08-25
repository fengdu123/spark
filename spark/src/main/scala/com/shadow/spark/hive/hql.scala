package com.shadow.spark.hive

import org.apache.spark.sql.SparkSession

class hql {

}

object hql{
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
        .master("local[*]")
      .getOrCreate()

    import  spark.sql

    sql("show databases").show()



    spark.stop()
  }
}
