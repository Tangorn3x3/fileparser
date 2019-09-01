package com.maxkain.fileparser.utils;

import com.maxkain.fileparser.exporters.DelimitedExporter;
import com.maxkain.fileparser.utils.configs.delimited.DelimitedOutputConfig;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Contains helper methods for working with files containing delimited lines
 */
@Log4j2
public class DelimitedFileUtils {

    /**
     * Creates a Scanner to read an incoming file
     *
     * @param filePath      a file in filesystem
     * @param lineSeparator line separator string in file
     * @return Scanner to read an incoming file
     * @throws IOException
     */
    public static Scanner createDelimitedScanner(Path filePath, String lineSeparator) throws IOException {
        Scanner scanner = new Scanner(filePath);
        if (lineSeparator != null) {
            scanner.useDelimiter(getLineSeparator(lineSeparator));
        }
        return scanner;
    }

    /**
     * Creates a class to export lines from an input file.
     *
     * @param config    Configuration for exporting lines
     * @param inputPath Input file
     * @param sequence  Current input file number in processing
     * @return Configured Exporter class
     * @throws IOException
     */
    public static DelimitedExporter createDelimitedExporter(DelimitedOutputConfig config, Path inputPath, int sequence) throws IOException {

        // Create output filename according to template
        String filename = FilenameUtils.generateOutputName(config.getNamePattern(), inputPath, sequence);

        // Checks if there is a directory
        FileUtils.checkOrCreateDirectory(config.getFolderPath());

        // Create output path for file saving
        Path outputPath = FileUtils.createPath(config.getFolderPath(), filename);

        BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8);

        return DelimitedExporter.builder()
                .config(config)
                .writer(writer)
                .build();
    }

    private static String getLineSeparator(String lineSeparator) {
        return lineSeparator != null ? lineSeparator : System.lineSeparator();
    }
}
