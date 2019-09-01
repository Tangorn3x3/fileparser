package com.maxkain.fileparser.processors.file;

import com.maxkain.fileparser.exporters.DelimitedExporter;
import com.maxkain.fileparser.exporters.Exporter;
import com.maxkain.fileparser.parcers.DelimitedParser;
import com.maxkain.fileparser.parcers.Parser;
import com.maxkain.fileparser.processors.Processable;
import com.maxkain.fileparser.utils.DelimitedFileUtils;
import com.maxkain.fileparser.utils.FileUtils;
import com.maxkain.fileparser.utils.configs.delimited.DelimitedProcessorConfig;
import com.maxkain.fileparser.utils.filters.FileFilter;
import com.maxkain.fileparser.utils.filters.InputFileFilter;
import com.maxkain.fileparser.utils.filters.LineFilter;
import com.maxkain.fileparser.utils.filters.RegexLineFilter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Class that processes files containing line with delimiter
 */

@Log4j2
public class DelimitedFileProcessor implements Processable {

    /**
     * The number of the current file in processing
     */
    private int fileSequence = 1;

    /**
     * Import, Export, and Archive Settings
     */
    private DelimitedProcessorConfig config;

    /**
     * Class for filtering files to be processed
     */
    private FileFilter incomingFilter;

    /**
     * Class for filtering lines to be processed
     */
    private LineFilter lineFilter;

    /**
     * Class for lines parsing
     */
    private Parser fileParser;

    public DelimitedFileProcessor(DelimitedProcessorConfig config) {
        this.config = config;
    }

    /**
     * Configures the current processor.
     * Creates the classes necessary to work
     */
    @Override
    public void configure() {
        incomingFilter = new InputFileFilter(config.getInput().getNamePattern());
        lineFilter = new RegexLineFilter(config.getInput().getLinePattern());

        fileParser = DelimitedParser.builder().delimeter(config.getInput().getDelimiter()).build();
    }

    /**
     * Starts files processing according to the settings
     */
    @Override
    public void process() {
        // Gets a list of files to process with the filter applied.
        List<Path> files = FileUtils.getFiles(config.getInput().getFolderPath(), incomingFilter);

        if (files.size() > 0) {
            log.info(String.format("Processing of %s files matching the condition starts", files.size()));
        } else {
            log.debug("No matching files were found");
        }

        for (Path file : files) {
            try {
                processFile(file);
            } catch (IOException e) {
                log.error(String.format("Error processing file %s", file.toString()), e);
            }
        }
    }

    /**
     * Processes single file
     *
     * @param filePath file in filesystem
     * @throws IOException
     */
    public void processFile(Path filePath) throws IOException {
        log.info(String.format("Processing file %s started", filePath.toString()));

        Scanner scanner = DelimitedFileUtils.createDelimitedScanner(filePath, config.getInput().getLineSeparator());

        DelimitedExporter exporter = DelimitedFileUtils.createDelimitedExporter(config.getOutput(), filePath, fileSequence);
        exporter.setColumns(config.getExportedColumns());

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // Checks if the string matches the specified filter
            if (lineFilter.accept(line)) {
                processLine(line, exporter);
            }
        }

        scanner.close();
        exporter.closeWriter();
        fileSequence++;

        // Archive the input file
        if (config.getArchive().isEnabled()) {
            log.info(String.format("Archiving file %s ...", filePath.toString()));
            try {
                FileUtils.archiveFile(filePath, config.getArchive().getFolderPath(), config.getArchive().getNamePattern());
            } catch (IOException e) {
                log.error(String.format("Archiving file %s failed", filePath.toString()));
            }
        }

        log.info(String.format("Processing file %s completed", filePath.toString()));
    }

    /**
     * Processes single line in file
     *
     * @param line     single line in file
     * @param exporter class responsible for saving the string
     */
    private void processLine(String line, Exporter exporter) {
        List<String> splited = fileParser.parseLine(line);
        exporter.exportLine(splited);
    }
}
