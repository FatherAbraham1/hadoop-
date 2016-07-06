 1 package com.hq;
 2 
 3 /**
 4  * User: hadoop
 5  * Date: 2014/10/10 0010
 6  * Time: 19:26
 7  */
 8 
 9 import org.apache.spark.SparkConf;
10 import org.apache.spark.api.java.JavaPairRDD;
11 import org.apache.spark.api.java.JavaRDD;
12 import org.apache.spark.api.java.JavaSparkContext;
13 import org.apache.spark.api.java.function.FlatMapFunction;
14 import org.apache.spark.api.java.function.Function2;
15 import org.apache.spark.api.java.function.PairFunction;
16 import scala.Tuple2;
17 
18 import java.util.Arrays;
19 import java.util.List;
20 import java.util.regex.Pattern;
21 
22 public final class JavaWordCount {
23   private static final Pattern SPACE = Pattern.compile(" ");
24 
25   public static void main(String[] args) throws Exception {
26 
27     if (args.length < 1) {
28       System.err.println("Usage: JavaWordCount <file>");
29       System.exit(1);
30     }
31 
32     SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount");
33     JavaSparkContext ctx = new JavaSparkContext(sparkConf);
34     JavaRDD<String> lines = ctx.textFile(args[0], 1);
35 
36     JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
37       @Override
38       public Iterable<String> call(String s) {
39         return Arrays.asList(SPACE.split(s));
40       }
41     });
42 
43     JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
44       @Override
45       public Tuple2<String, Integer> call(String s) {
46         return new Tuple2<String, Integer>(s, 1);
47       }
48     });
49 
50     JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
51       @Override
52       public Integer call(Integer i1, Integer i2) {
53         return i1 + i2;
54       }
55     });
56 
57     List<Tuple2<String, Integer>> output = counts.collect();
58     for (Tuple2<?, ?> tuple : output) {
59       System.out.println(tuple._1() + ": " + tuple._2());
60     }
61     ctx.stop();
62   }
63 }