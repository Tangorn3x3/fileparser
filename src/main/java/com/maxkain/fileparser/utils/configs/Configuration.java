package com.maxkain.fileparser.utils.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * The main configuration of the entire application
 */

@Getter
@Setter
@Log4j2
public class Configuration {
    private static Configuration instance = null;

    public static final String FILENAME = "config/config.xml";

    private FileBasedConfiguration configuration;

    public static Configuration getInstance() {
        if (Configuration.instance == null) {
            Configuration.instance = new Configuration();
        }

        return Configuration.instance;
    }

    private Configuration() {
        boolean result = this.loadConfig(FILENAME);

        if (!result) {
            log.fatal("Unable to load application configuration");
            System.exit(0);
        }
    }

    /**
     * Loads application configuration from file
     *
     * @param fileName Location and file name
     * @return Status of loading and processing the settings file
     */
    private boolean loadConfig(String fileName) {
        Configurations configurations = new Configurations();
        File           file           = new File(fileName);

        // Reading the settings xml-file
        FileBasedConfigurationBuilder<XMLConfiguration> builder = configurations.xmlBuilder(file);

        try {
            this.configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            log.error("Unable to read configuration file", e);
            return false;
        }

        return true;
    }

    /**
     * Gets the configuration parameter's string value
     *
     * @param key Path to parameter in xml file
     * @return parameter's string value
     */
    public String getStringConfigValue(String key) {
        String value = null;
        try {
            value = configuration.getString(key);
        } catch (NoSuchElementException e) {
            log.debug("Config with key [" + key + "] not exists");
        }
        return value;
    }

    /**
     * Gets the configuration parameter's boolean value
     *
     * @param key Path to parameter in xml file
     * @return parameter's boolean value
     */
    public boolean getBooleanConfigValue(String key) {
        boolean value = false;
        try {
            value = configuration.getBoolean(key);
        } catch (NoSuchElementException e) {
            log.debug("Config with key [" + key + "] not exists");
        }
        return value;
    }

    /**
     * Gets an array of parameter values
     *
     * @param key Path to parameter in xml file
     * @return parameter's array value
     */
    public String[] getStringArrayConfigValue(String key) {
        String[] value = {};
        try {
            value = configuration.getStringArray(key);
        } catch (NoSuchElementException e) {
            log.debug("Config with key [" + key + "] not exists");
        }
        return value;
    }
}
