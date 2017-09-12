package org.shadow

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by shadow on 2017/6/17 0017.
  */
object saprkcsv {


  //
  System.setProperty("hadoop.home.dir","D:\\_20170526")

  case class person(var orderisn: String, var payicompanyicommissionifee: BigDecimal, var merchanticommissionifee: BigDecimal)

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("jdbc").master("local[*]").getOrCreate();

    spark.sparkContext.addJar("D:\\mysql-connector-java-5.1.38.jar")
    import spark.implicits._
    val tuple22Ds = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:3306/oozie?user=root&password=root",
        "dbtable" ->
          s"""(SELECT * from tuple22
            ) t""".stripMargin,
        "driver" -> "com.mysql.jdbc.Driver",
        "fetchSize" -> "1"
      )).load().as[tuple22]

    println(s" >>  tuple22Ds.show(1)")
    tuple22Ds.show(1)

    val tuple23Ds = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:3306/oozie?user=root&password=root",
        "dbtable" ->
          s"""(SELECT * from tuple23
            ) t""".stripMargin,
        "driver" -> "com.mysql.jdbc.Driver",
        "fetchSize" -> "1"
      )).load().as[tuple23]
    println(s" >>  tuple23Ds.show(1)")
    tuple23Ds.show()

    val tuple22DF = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:3306/oozie?user=root&password=root",
        "dbtable" ->
          s"""(SELECT * from tuple22
            ) t""".stripMargin,
        "driver" -> "com.mysql.jdbc.Driver",
        "fetchSize" -> "1"
      )).load()

    println(s" >>  tuple22DF.show(1)")
    tuple22DF.show(1)

    val tuple23DF = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:3306/oozie?user=root&password=root",
        "dbtable" ->
          s"""(SELECT * from tuple23
            ) t""".stripMargin,
        "driver" -> "com.mysql.jdbc.Driver",
        "fetchSize" -> "1"
      )).load()

    println(s" >>  tuple23DF.show(1)")
    tuple23DF.show(1)
    spark.close()


  }

  case class tuple23(i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, i11: Int, i12: Int, i13: Int, i14: Int, i15: Int, i16: Int, i17: Int, i18: Int, i19: Int, i20: Int, i21: Int, i22: Int, i23: Int) {
  }

  case class tuple22(i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, i8: Int, i9: Int, i10: Int, i11: Int, i12: Int, i13: Int, i14: Int, i15: Int, i16: Int, i17: Int, i18: Int, i19: Int, i20: Int, i21: Int, i22: Int) {
  }

}
