package com.maxkain.fileparser.utils.configs.delimited;

import com.maxkain.fileparser.utils.configs.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Output settings for a file containing lines with delimeters
 */
@Log4j2
@Getter
@Setter
@NoArgsConstructor
public class DelimitedOutputConfig {

    /** Xml node in the configuration file containing the Output settings */
    @Getter(AccessLevel.NONE)  @Setter(AccessLevel.NONE)
    private final String CONFIG_NODE = "output";

    /** Output files directory */
    private String folderPath;

    /** Output files name template */
    private String namePattern;

    /** Line separator in Output file */
    private String lineSeparator;

    /** Column delimiter in Output line */
    private String delimiter;

    /**
     * Initializing settings from the main application configuration
     * @param prefix root path to current configuration in main xml file
     */
    void init(String prefix) {
        String outputPath = prefix + "." + CONFIG_NODE + ".";
        this.folderPath = Configuration.getInstance().getStringConfigValue(outputPath + "folderPath");
        this.namePattern = Configuration.getInstance().getStringConfigValue(outputPath + "namePattern");
        this.lineSeparator = Configuration.getInstance().getStringConfigValue(outputPath + "lineSeparator");
        this.delimiter = Configuration.getInstance().getStringConfigValue(outputPath + "delimiter");

        log.debug("Output configuration has been initialized");
    }
}
