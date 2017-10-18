package org.spafka.storm;

import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storm {

    Logger logger = LogManager.getLogger(Storm.class);
    private Random random = new Random();

    private BlockingQueue sentenceQueue = new ArrayBlockingQueue(50000);
    private BlockingQueue wordQueue = new ArrayBlockingQueue(50000);
    // 用来保存最后计算的结果key=单词，value=单词个数
    Map<String, Integer> counters = Maps.newHashMap();

    //用来发送句子
    public void nextTuple() {
        String[] sentences = new String[]{"the cow jumped over the moon",
                "an apple a day keeps the doctor away",
                "four score and seven years ago",
                "snow white and the seven dwarfs", "i am at two with nature"};
        String sentence = sentences[random.nextInt(sentences.length)];
        try {
            sentenceQueue.put(sentence);
           logger.info("send sentence:" + sentence);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //用来切割句子
    public void split(String sentence) {
       logger.info("resv sentence" + sentence);
        String[] words = sentence.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                word = word.toLowerCase();
                //collector.emit()
                wordQueue.add(word);
               logger.info("split word:" + word);
            }
        }

    }

    //用来计算单词
    public void wordcounter(String word) {
        if (!counters.containsKey(word)) {
            counters.put(word, 1);
        } else {
            Integer c = counters.get(word) + 1;
            counters.put(word, c);
        }
       logger.info("print map:" + counters);
    }


    public static void main(String[] args) {
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Storm Storm = new Storm();
        //发射句子到sentenceQuequ
        executorService.submit(new Spout(Storm));
        //接受一个句子，并将句子切割
        executorService.submit(new BoltSplit(Storm));
        //接受一个单词，并进行据算
        executorService.submit(new BoltWordCount(Storm));
    }

    public BlockingQueue getSentenceQueue() {
        return sentenceQueue;
    }

    public void setSentenceQueue(BlockingQueue sentenceQueue) {
        this.sentenceQueue = sentenceQueue;
    }

    public BlockingQueue getWordQueue() {
        return wordQueue;
    }

    public void setWordQueue(BlockingQueue wordQueue) {
        this.wordQueue = wordQueue;
    }
}

class Spout extends Thread {

    Logger logger = LogManager.getLogger(Spout.class);
    private Storm Storm;

    public Spout(Storm Storm) {
        this.Storm = Storm;
    }

    @Override
    public void run() {
        //storm框架在循环调用spout的netxTuple方法
        while (true) {
            Storm.nextTuple();
            try {
                this.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BoltWordCount extends Thread {

    Logger logger = LogManager.getLogger(BoltWordCount.class);
    private Storm Storm;


    @Override
    public void run() {
        while (true) {
            try {
                String word = (String) Storm.getWordQueue().take();
                Storm.wordcounter(word);
            } catch (Exception e) {
               logger.info(e);
            }
        }
    }

    public BoltWordCount(Storm Storm) {
        this.Storm = Storm;
    }
}

@Log4j2(topic = "")
class BoltSplit extends Thread {

    private Storm Storm;

    @Override
    public void run() {
        while (true) {
            try {
                String sentence = (String) Storm.getSentenceQueue().take();
                Storm.split(sentence);
            } catch (Exception e) {
               log.info(e);
            }
        }
    }

    public BoltSplit(Storm Storm) {
        this.Storm = Storm;
    }
}
