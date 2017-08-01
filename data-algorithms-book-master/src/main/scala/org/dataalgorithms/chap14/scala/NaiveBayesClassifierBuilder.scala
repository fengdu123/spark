package org.dataalgorithms.chap14.scala

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * NaiveBayesClassifierBuilder builds a Naive Bayes 
 * Classifier. Two outputs are generated by this class:
 * 
 *   1. Probability Table 
 *   2. Classes used in classification
 *   
 * These above generated outputs are used (in BayesClassifierBuilder)
 * to classify new data.
 * 
 * @author Gaurav Bhardwaj (gauravbhardwajemail@gmail.com)
 *
 * @editor Mahmoud Parsian (mahmoud.parsian@yahoo.com)
 *
 */
object NaiveBayesClassifierBuilder {
  //
  def main(args: Array[String]): Unit = {
    if (args.size < 2) {
      println("Usage: NaiveBayesClassifierBuilder <input-training-data-path> <output-path>")
      sys.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("NaiveBayesClassifierBuilder")
    val sc = new SparkContext(sparkConf)

    val input = args(0)
    val output = args(1)

    val training = sc.textFile(input)
    val trainingDataSize = training count

    val pairs = training.flatMap(line => {
      val tokens = line.split(",")
      val theClassification = tokens.last
      (("CLASS", theClassification), 1) :: tokens.init.map(token => ((token, theClassification), 1)).toList
    })

    val counts = pairs reduceByKey (_ + _)

    val countsAsMap = counts collectAsMap
    val pt = countsAsMap.map(tuple => {
      if (tuple._1._1 == "CLASS") (tuple._1, (tuple._2 / trainingDataSize.toDouble)) else {
        val count = countsAsMap.getOrElse(("CLASS", tuple._1._2), 0)
        if (count == 0) (tuple._1, 0d) else (tuple._1, (tuple._2 / count.toDouble))
      }
    })
    val ptRDD = sc.parallelize(pt.toList)
    
    // For debugging purpose
    pt.foreach(f => println(s"${f._1._1},${f._1._2},${f._2}"))

    // save the probability table
    ptRDD.saveAsObjectFile(output + "/naivebayes/pt")

    val classifications = pairs.filter(_._1._1 == "CLASS").map(_._1._2).distinct
    
    // For debugging purpose
    classifications.foreach(println)

    // save the classified data (classes used in classification)
    classifications.saveAsTextFile(output + "/naivebayes/classes")

    // done!
    sc.stop()
  }
}