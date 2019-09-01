package com.maxkain.fileparser;

import com.maxkain.fileparser.processors.ProcessorRunner;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

/**
 * Application for processing and exporting files
 */
@Log4j2
public class App {
    public static void main(String[] args) {
        runProcess();
        log.info("Program closed");
    }

    private static void runProcess() {
        Thread processorThread = new Thread(new ProcessorRunner());
        processorThread.setName("DELIMITED");
        processorThread.start();

        log.info("Program started. Type <quit> to close");

        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            String input = keyboard.nextLine();
            if(input != null) {
                if ("quit".equals(input.toLowerCase())) {
                    log.info("Exit program...");
                    processorThread.interrupt();
                    exit = true;
                }
            }
        }
        keyboard.close();
    }

}
