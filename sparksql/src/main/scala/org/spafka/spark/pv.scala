package org.spafka.spark

import java.util.UUID

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime

object pv {


  def main(args: Array[String]): Unit = {


    //

    val spark = SparkSession.
      builder().
      appName("web_log_pre_process").
      master("local[*]").
      getOrCreate()

    val sc = spark.sparkContext

    var IP2ALL: RDD[(String, (String, String, String, String, String, String, String, String))] = sc.textFile("sparksql\\src\\main\\resources\\logPreOut").map(x => {
      val xs = x.split("\001")
      (xs(0), (xs(0), xs(1), xs(2), xs(3), xs(4), xs(5), xs(6), xs(7)))
    })


    val createCombiner = (x: (String, String, String, String, String, String, String, String)) => List(x)
    val mergeValue = (x: List[(String, String, String, String, String, String, String, String)], y: (String, String, String, String, String, String, String, String)) => x.+:(y)
    val mergeCombiners = (x: List[(String, String, String, String, String, String, String, String)], y: List[(String, String, String, String, String, String, String, String)]) => x ++ (y)

    val Ip2IpAll: RDD[(String, List[(String, String, String, String, String, String, String, String)])] = IP2ALL.combineByKey(createCombiner, mergeValue, mergeCombiners)

    val Ip2IpAllSorted: RDD[(String, List[(String, String, String, String, String, String, String, String)])] = Ip2IpAll.mapValues(_.sortBy(_._3))

    // session+"\001"+key.toString()+"\001"+bean.getRemote_user() + "\001" + bean.getTime_local() + "\001" + bean.getRequest() + "\001" + step + "\001" + (60) + "\001" + bean.getHttp_referer() + "\001" + bean.getHttp_user_agent() + "\001" + bean.getBody_bytes_sent() + "\001" + bean.getStatus()


    val pv = Ip2IpAllSorted.map(_ match { case (x: String, y: List[(String, String, String, String, String, String, String, String)]) => {

      val uuid = UUID.randomUUID()
      val date = DateTime.now().toString("yyyyMMdd")
      //(ip, user, formatDate(date), request, status,Http_refer Body_bytes_sent, useragent)
      val xyz = for (z <- 0 until y.length) yield uuid + "\001" + y(z)._1 + "\001" + y(z)._2 + "\001" + y(z)._3 + "\001" + y(z)._4 + "\001" + (z+1) + "\001" + 60 + "\001" + y(z)._6 + "\001" + y(z)._7 + "\001" + y(z)._8 + "\001" + y(z)._5 + "\001" + date
      xyz.mkString("\n")
    }
    })
    //Ip2IpAllSorted.foreach(println(_))

    pv.saveAsTextFile("hdfs://cdh01.spafka.com:8022/ods_web/pv/"+DateTime.now().toString("yyyyMMdd"))

    //drop table if exists ods_web_log.ods_click_pageviews;
//    create table ods_click_pageviews(
//      Session string,
//      remote_addr string,
//      remote_user string,
//      time_local string,
//      request string,
//      visit_step string,
//      page_staylong string,
//      http_referer string,
//      http_user_agent string,
//      body_bytes_sent string,
//      status string)
//    partitioned by (datestr string)
//    row format delimited
//    fields terminated by '\001';

 //   load data inpath '/ods_web/pv/20170828' overwrite into table ods_click_pageviews partition(datestr='20170828');

   // select * from ods_web_log.ods_click_pageviews limit 10;
  }
}
