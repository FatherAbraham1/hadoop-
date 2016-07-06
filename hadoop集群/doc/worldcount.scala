package com.intelligrape.spark;
import java.util.Arrays;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
/**
 * @author surendra.singh
 **/
public class WordCount {
   public static void main(String[] args) {
  //Create Java Spark context object by passing SparkConfig Object
  JavaSparkContext sc = new JavaSparkContext(new SparkConf());
  //load sample data file containing the words using Spark context object.
  //Spark will read file line by line and convert it in a RDD of Sting.
  //each object in RDD represent a single line in data file.
  JavaRDD<String> lines = sc.textFile("/home/test-data/Hello World.txt");
  //convert RDD of string to RDD of words by spliting line with space.
  JavaRDD<String> words = lines.flatMap(l -> Arrays.asList(l.split(" ")));
  //Create tuple of each word having count '1'. Spark create tuple using Tuple2 class
  JavaPairRDD<String,Integer> tuple = words.mapToPair(w->new Tuple2<>(w, 1));
  //reduce all keys by adding their individual count
  JavaPairRDD<String, Integer> count = tuple.reduceByKey((a, b) -> a + b).sortByKey();
  //Print the result
  for (Tuple2<String, Integer> tuple2 : tuple.toArray()) {
      System.out.println(tuple2._1() + " - " + tuple2._2());
  }
   }
}