package com.maxkain.fileparser.utils.configs.delimited;

import com.maxkain.fileparser.utils.configs.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Input settings for a file containing lines with delimeters
 */
@Log4j2
@Getter
@Setter
@NoArgsConstructor
public class DelimitedInputConfig {

    /** Xml node in the configuration file containing the Input settings */
    @Getter(AccessLevel.NONE)  @Setter(AccessLevel.NONE)
    private final String CONFIG_NODE = "input";

    /** Input files directory */
    private String folderPath;

    /** Input files name pattern */
    private String namePattern;

    /** Regexp pattern for line filtering */
    private String linePattern;

    /** Line separator in input file */
    private String lineSeparator;

    /** Column delimiter in input line */
    private String delimiter;

    /**
     * Initializing settings from the main application configuration
     * @param prefix root path to current configuration in main xml file
     */
    void init(String prefix) {
        String inputPath = prefix + "." + CONFIG_NODE + ".";
        this.folderPath = Configuration.getInstance().getStringConfigValue(inputPath + "folderPath");
        this.namePattern = Configuration.getInstance().getStringConfigValue(inputPath + "namePattern");
        this.linePattern = Configuration.getInstance().getStringConfigValue(inputPath + "linePattern");
        this.delimiter = Configuration.getInstance().getStringConfigValue(inputPath + "delimiter");
        this.lineSeparator = Configuration.getInstance().getStringConfigValue(inputPath + "lineSeparator");

        log.debug("Input configuration has been initialized");
    }
}
