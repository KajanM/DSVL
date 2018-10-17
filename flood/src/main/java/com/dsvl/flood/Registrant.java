package com.dsvl.flood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This {@code Component} starts with the application and attempts
 * to register with the bootstrap server.
 * <br>
 * If register attempt is failed for some reason
 * this will reattempt to register after waiting for five seconds.
 * <br>
 * This {@code CommandLineRunner} exists once registration is successful
 */
@Component
public class Registrant implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Registrant.class);

    @Autowired
    private Node node;

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            logger.info("Attempting to register with the bootstrap server");

            if(node.register()) break;

            // TODO: instead of sleeping for fixed 5 seconds apply some incremental logic
            logger.info("Sleeping for 5 seconds");
            Thread.sleep(5000);
        }
    }
}
