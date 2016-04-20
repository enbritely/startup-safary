package com.enbritely.spark;

import com.google.common.collect.Lists;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.util.Collections;
import java.util.List;

public class EventToSession {

    public static List<Event> sortyByTimestamp(Iterable<Event> events) {
        List<Event> list = Lists.newArrayList(events);
        Collections.sort(list, (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
        return list;
    }

    public static void main(String[] args) {

        CommandLineParser parser = new GnuParser();
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt("input").isRequired().hasArg().withDescription("Location of event files to process").create("i"));
        options.addOption(OptionBuilder.withLongOpt("output").isRequired().hasArg().withDescription("Target location where the output are written").create("o"));
        options.addOption(OptionBuilder.withLongOpt("limit").isRequired().hasArg().withDescription("Threshold of click counts").create("l"));
        options.addOption("t", "test", false, "Test flag, runs with local Spark. SPARK_HOME env variable has to be set.");
        options.addOption("s", "spark", true, "Spark home directory, used only in test mode");

        CommandLine params = null;
        try {
            params = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("posix", options);
            return;
        }

        String input = params.getOptionValue("input");
        String output = params.getOptionValue("output");
        int threshold = Integer.parseInt(params.getOptionValue("limit"));
        boolean testMode = params.hasOption("test");

        SparkConf conf = new SparkConf().setAppName("safary-event-to-session");
        if (testMode) {
            String sparkHome = params.getOptionValue("spark");
            if (sparkHome == null || sparkHome.equals("")) {
                System.err.println("In test mode you need to pass Spark home!");
                return;
            }
            conf.setSparkHome(sparkHome);
            conf.setMaster("local[2]");
        }

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        run(input, output, threshold, sparkContext);



    }

    public static void run(String eventLocation, String dest, int threshold, JavaSparkContext context) {
        JavaRDD<String> lines = context.textFile(eventLocation);

        JavaRDD<Event> events = lines.map(Converter::jsonToEvent);
        JavaRDD<Event> clickEvents = events.filter(e -> e.getType().equals("click"));

        JavaPairRDD<String, Iterable<Event>> groupedEvents = clickEvents.groupBy(Event::getSessionId);
        JavaRDD<Session> sessions = groupedEvents.mapValues(
                (Function<Iterable<Event>, Session>) unorderedEvents -> {
                    List<Event> clickOrdered = sortyByTimestamp(unorderedEvents);
                    Session session = new Session(clickOrdered.get(0).getSessionId());
                    for (Event event: clickOrdered) {
                        session.addClick(event.getTimestamp());
                    }
                    return session;
        }).values();


        JavaRDD<String> badSessions = sessions.filter(s -> s.getClickCount() > threshold)
                .map(s -> String.format("%s:%s", s.getSessionId(), s.getClickCount()));

        badSessions.coalesce(1,true).saveAsTextFile(dest);
    }
}
