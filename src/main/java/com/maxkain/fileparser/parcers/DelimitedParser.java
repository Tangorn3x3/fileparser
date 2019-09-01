package com.maxkain.fileparser.parcers;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;

/**
 * Splits the row into columns according to the settings
 */
@Builder
@Log4j2
public class DelimitedParser implements Parser {

    private static final char QUOTE = '"';

    /**
     * Column separator
     */
    private String delimeter = ";";

    /**
     * Splits the row into columns
     *
     * @param line Line from file
     * @return List of column values
     */
    public List<String> parseLine(String line) {
        String[] result = preprocessLine(line).split(buildRegexp());
        return postProcessList(Arrays.asList(result));
    }

    /**
     * Creates a regular expression to split the line
     */
    private String buildRegexp() {
        return String.format("%1$s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", delimeter);
    }

    private String preprocessLine(String line) {
        return line + "$";
    }

    private List<String> postProcessList(List<String> list) {
        String item = list.get(list.size() - 1);
        if (item != null && item.length() > 0) {
            item = item.substring(0, item.length() - 1);
        }
        list.set(list.size() - 1, item);
        return list;
    }
}
