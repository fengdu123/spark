package org.spafka.storm;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "order.cl")
public class order {
    public static void main(String[] args) {

        log.trace("trace level");
        log.debug("debug level");
        log.info("info level");
        log.warn("warn level");
        log.error("error level");
        log.fatal("fatal level");
    }
}
