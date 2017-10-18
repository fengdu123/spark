package org.spafka.spark

import java.text.{ParseException, SimpleDateFormat}
import java.util.Locale

import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime

object logProProcess {

  def main(args: Array[String]): Unit = {


    val spark = SparkSession.
      builder().
      appName("web_log_pre_process").
      master("local[*]").
      getOrCreate()

    val sc = spark.sparkContext

    val logRdd = sc.textFile("sparksql\\src\\main\\resources\\access.log.fensi", 2)

    var df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US)
    var df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    def formatDate(time_local: String) = try
      df2.format(df1.parse(time_local))
    catch {
      case e: ParseException =>
        null
    }

    val s = "\001"

    def proProcess(lines: Array[String]) = {
      val length = lines.length
      if (length > 10) {
        val ip = lines(0)
        val user = lines(1)
        val date = lines(3).substring(1)
        val request = lines(6).substring(1)
        val status = lines(8)
        val Body_bytes_sent = lines(9)
        val Http_referer = lines(10)
        var useragent: String = null;
        if (length > 11) {
          val l = for (i <- (11 to length - 1)) yield lines(i)
          useragent = l.mkString("", "", "")
        }
        ip + s + user + s + formatDate(date) + s + request + s + status + s + Body_bytes_sent + s + Http_referer + s + useragent
      }
    }


    val map = logRdd.map(_.split(" ")).map(proProcess(_))

    val tuple = map.filter(_ match {
      case x: String => true
      case _ => false
    })


   // tuple.saveAsTextFile("sparksql\\src\\main\\resources\\logPreOut")
    tuple.saveAsTextFile("hdfs://cdh01.spafka.com:8022/ods_web/original/"+DateTime.now().toString("yyyyMMdd"))


//    drop table if exists ods_web_log.ods_weblog_origin;
//    create table ods_weblog_origin(
//      remote_addr string,
//      remote_user string,
//      time_local string,
//      request string,
//      status string,
//      body_bytes_sent string,
//      http_referer string,
//      http_user_agent string)
//    partitioned by (datestr string)
//    row format delimited
//    fields terminated by '\001';

    // use ods_web_log;
    // load data inpath '/ods_web/original/20170828/' overwrite into table ods_weblog_origin partition(datestr='20170828');
    // select * from ods_web_log.ods_weblog_origin limit 10;
    spark.stop()


  }


}
