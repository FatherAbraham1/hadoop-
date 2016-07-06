1 package com.hq
 2 
 3 /**
 4  * User: hadoop
 5  * Date: 2014/10/10 0010
 6  * Time: 18:59
 7  */
 8 import org.apache.spark.SparkConf
 9 import org.apache.spark.SparkContext
10 import org.apache.spark.SparkContext._
11 
12 /**
13  * 统计字符出现次数
14  */
15 object WordCount {
16   def main(args: Array[String]) {
17     if (args.length < 1) {
18       System.err.println("Usage: <file>")
19       System.exit(1)
20     }
21 
22     val conf = new SparkConf()
23     val sc = new SparkContext(conf)
24     val line = sc.textFile(args(0))
25 
26     line.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_).collect().foreach(println)
27 
28     sc.stop()
29   }
30 }