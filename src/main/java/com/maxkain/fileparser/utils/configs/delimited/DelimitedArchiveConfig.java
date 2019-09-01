package com.maxkain.fileparser.utils.configs.delimited;

import com.maxkain.fileparser.utils.configs.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Archiving settings for a file containing lines with delimeters
 */

@Log4j2
@Getter
@Setter
@NoArgsConstructor
public class DelimitedArchiveConfig {

    /**
     * Xml node in the configuration file containing the Archiving settings
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String CONFIG_NODE = "archive";

    /**
     * Archive Activity Flag
     */
    private boolean enabled;

    /**
     * Archive files directory
     */
    private String folderPath;

    /**
     * Archive files name pattern
     */
    private String namePattern;

    /**
     * Initializing settings from the main application configuration
     *
     * @param prefix root path to current configuration in main xml file
     */
    void init(String prefix) {
        String outputPath = prefix + "." + CONFIG_NODE + ".";
        this.enabled = Configuration.getInstance().getBooleanConfigValue(outputPath + "enabled");
        this.folderPath = Configuration.getInstance().getStringConfigValue(outputPath + "folderPath");
        this.namePattern = Configuration.getInstance().getStringConfigValue(outputPath + "namePattern");

        log.debug("Archive configuration has been initialized");
    }
}
