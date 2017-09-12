package org.spafka.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime

object PvVisit {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.
      builder().
      appName("web_log_pre_process").
      master("local[*]").
      getOrCreate()


    val sc = spark.sparkContext


    val pvRdd = sc.textFile("sparksql\\src\\main\\resources\\pvOut")


    val step1: RDD[(String, pageViewBean)] = pvRdd.map(x => {
      val f = x.split("\001")
      (f(0), pageViewBean(f(0), f(1), f(3), f(4), Integer.valueOf(f(5)), f(6), f(7), f(8), f(9), f(10)))
    })

    val createCombiner = (x: pageViewBean) => List(x)
    val mergeValue = (x: List[pageViewBean], y: pageViewBean) => x :+ y
    val mergeCombiners = (x: List[pageViewBean], y: List[pageViewBean]) => x ++ y

    val step2: RDD[(String, List[pageViewBean])] = step1.combineByKey(createCombiner, mergeValue, mergeCombiners)

    val step3 = step2.mapValues(y => {
      val z = y.sortBy(_.timestr)
      val head = z.head
      val last = z.last
      val inPage = head.request
      val outPage = last.request
      val start = head.timestr
      val end = last.timestr
      val size = z.length
      val ip = head.remote_addr
      val referal = head.referal
      val session = head.session
      session+"\001"+ip+"\001"+start+"\001"+end+"\001"+inPage+"\001"+outPage+"\001"+referal+size
    }
    )

    val visit = step3.map(_._2).saveAsTextFile("hdfs://cdh01.spafka.com:8022"+DateTime.now().toString("yyyyMMdd"))




//create table ods_web_log.click_stream_visit(
//    session     string,
//    remote_addr string,
//    inTime      string,
//    outTime     string,
//    inPage      string,
//    outPage     string,
//    referal     string,
//    pageVisits  int)
//    partitioned by (datestr string);


//    create table ods_web_log.dim_time(
//      year string,
//      month string,
//      day string,
//      hour string)
//    row format delimited
//    fields terminated by ',';

    // Load data  inpath '/ods_web/pvVisit/20170828' overwrite into table ods_web_log.click_stream_visit partition(datestr='20170818');

    // select * from ods_web_log.click_stream_visit limit 10




  }

}

case class pageViewBean(session: String,
                        remote_addr: String,
                        timestr: String,
                        request: String,
                        step: Int = 0,
                        staylong: String,
                        referal: String,
                        useragent: String,
                        bytes_send: String,
                        status: String)

case class VisitBean(
                      session: String,
                      remote_addr: String,
                      inTime: String,
                      outTime: String,
                      inPage: String,
                      outPage: String,
                      referal: String,
                      pageVisits: Int = 0
                    )