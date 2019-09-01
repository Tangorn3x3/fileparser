package com.maxkain.fileparser.processors;

import com.maxkain.fileparser.processors.file.DelimitedFileProcessor;
import com.maxkain.fileparser.utils.configs.delimited.DelimitedProcessorConfig;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ProcessorRunner implements Runnable {
    /** Waiting interval between files processing cycles */
    private static final int WAIT_INTERVAL = 5000;

    /** File processor */
    private Processable processor;

    public ProcessorRunner() {
        DelimitedProcessorConfig delimitedProcessorConfig = new DelimitedProcessorConfig();
        processor = new DelimitedFileProcessor(delimitedProcessorConfig);
        processor.configure();
    }

    @Override
    public void run() {
        log.info("Starting file processing...");
        while (true) {
            if (!Thread.interrupted()) {
                processor.process();
            } else return;

            try {
                sleep();
            } catch (InterruptedException e) {
                log.info("Completion of file processing");
                return;
            }
        }
    }

    private void sleep() throws InterruptedException {
        log.debug(String.format("Waiting %s seconds for file processing...", WAIT_INTERVAL/1000));
        Thread.sleep(WAIT_INTERVAL);
    }
}
