# Startup Safary Budapest @enbrite.ly presentation codes

# Spark demo

You can find the source code of the Spark demo, presented by @joemeszaros in the `spark-demo` folder.

### Get started with Apache Spark

To dig your teeth into Apache Spark I suggest to start with the [official website](http://spark.apache.org/).

You will find the [quick start guide](http://spark.apache.org/docs/latest/quick-start.html), which is a compact and useful guide.

If you would like to move to the next step browse the Internet for slided, books, code samples and do not forget to read the [programming guide](http://spark.apache.org/docs/latest/programming-guide.html).

### Requirements

You will need the following software packages:
- [Apache Maven](https://maven.apache.org/)
- Java 8

Do not forget to download Apache Spark to run your application in local mode. Download and unzip spark-1.6.1-bin-hadoop2.6.tgz, which is prebuilt Spark for Hadoop 2.6 or later from [Spark website](http://spark.apache.org/downloads.html).

Try running Spark interactive shell, inside the spark-1.6.1-bin-hadoop2.6 directory, by typing:

```bash
$ ./bin/spark-shell
```

### Build application

It is really easy to create a jar file with all the dependencies with:

```bash
$ cd spark-demo
$ mvn clean package
```
### Submit your application to Spark

Use spark-submit to run your application

```bash
$ YOUR_SPARK_HOME/bin/spark-submit \
  --class "com.enbritely.spark.EventToSession" \
  --master local[4] \
  target/spark-demo-0.0.1-jar-with-dependencies.jar \
  --limit 10 \
  --input data/sample_events \
  --output bad_sessions
 ```

... and finally if you are lucky enough get the list of bad sessions with

```bash
less bad_sessions/part-00000
```
