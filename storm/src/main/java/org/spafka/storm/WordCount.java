package org.spafka.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class WordCount {


    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("mySpout", new MySpout());
        builder.setBolt("mybolt1", new MySplitBolt(), 2).shuffleGrouping("mySpout");


        builder.setBolt("mybolt2", new MyCountBolt(), 4).fieldsGrouping("mybolt1", new Fields("word"));
//        builder.setBolt("mybout2",new MyCountBolt(),2).shuffleGrouping("mybolt1");


        Config config = new Config();
        config.setNumWorkers(2);
        config.setDebug(true);

        StormSubmitter.submitTopology("mywordcount", config, builder.createTopology());
//        LocalCluster localCluster = new LocalCluster();
//        localCluster.submitTopology("mywordcount", config, builder.createTopology());
    }
}
