package com.maxkain.fileparser.exporters;

import com.maxkain.fileparser.utils.configs.delimited.DelimitedFileColumn;
import com.maxkain.fileparser.utils.configs.delimited.DelimitedOutputConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Class for saving parsed data to a file depending on column settings
 */
@Builder
@Log4j2
public class DelimitedExporter implements Exporter {

    /**
     * Configuration for output
     */
    private DelimitedOutputConfig config;

    /**
     * Class for writing to file
     */
    private BufferedWriter writer;

    /**
     * List of columns to save
     */
    @Getter
    @Setter
    private List<DelimitedFileColumn> columns;

    /**
     * Saves one parsed line to a file
     *
     * @param line single parsed line
     */
    @Override
    public void exportLine(List<String> line) {
        String formatedLine = formatLine(line);
        try {
            writer.write(formatedLine);
            writer.newLine();
            writer.flush();
            log.debug(String.format("Exported line: %s", formatedLine));
        } catch (IOException e) {
            log.error("Error occurred while exporting line", e);
        }
    }

    /**
     * Formats the parsed line into a string depending on the required format and set of columns
     *
     * @param line single parsed line
     * @return string depending on the required format and set of columns
     */
    private String formatLine(List<String> line) {
        StringBuilder result = new StringBuilder();

        for (Iterator<DelimitedFileColumn> col = columns.iterator(); col.hasNext(); ) {
            DelimitedFileColumn column = col.next();

            // Finds the current column value in the parsed line
            result.append(line.get(column.getPosition()));

            // Adds column separator according to settings
            result.append(col.hasNext() ? config.getDelimiter() : "");
        }

        return String.format("%s%s", result.toString(), config.getLineSeparator());
    }

    /**
     * Closes the current writer
     */
    public void closeWriter() throws IOException {
        this.writer.close();
    }

}
