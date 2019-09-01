package com.maxkain.fileparser.utils.configs.delimited;

import com.maxkain.fileparser.utils.configs.AbstractProcessorConfig;
import com.maxkain.fileparser.utils.configs.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Settings for a file containing lines with delimeters
 */

@Getter @Setter @Log4j2
public class DelimitedProcessorConfig extends AbstractProcessorConfig {

   /** Xml node in the configuration file containing the DelimitedProcessor settings */
   @Getter(AccessLevel.NONE)  @Setter(AccessLevel.NONE)
   private final String PROCESSOR_NODE = "delimiterProcessor";

   /** Xml node in the configuration file containing the Columns settings */
   @Getter(AccessLevel.NONE)  @Setter(AccessLevel.NONE)
   private final String COLUMNS_NODE = "columns";

   /** Xml node in the configuration file containing the single Column settings */
   @Getter(AccessLevel.NONE)  @Setter(AccessLevel.NONE)
   private final String COLUMN_SINGLE_NODE = "column";

   private DelimitedInputConfig      input;
   private DelimitedOutputConfig     output;
   private DelimitedArchiveConfig archive;
   private List<DelimitedFileColumn> columns;

   public DelimitedProcessorConfig() {
      initInputConfig();
      initOutputConfig();
      initArchiveConfig();
      initColumns();

      log.info("Processor configuration has been initialized");
   }

   /** Gets a list of columns marked for export */
   public List<DelimitedFileColumn> getExportedColumns() {
      return columns.stream()
              .filter(DelimitedFileColumn::isExported)
              .collect(Collectors.toList());
   }

   /** Initializing and setting Input configuration */
   private void initInputConfig() {
      DelimitedInputConfig inputConfig = new DelimitedInputConfig();
      inputConfig.init(String.format("%s.%s", ROOT_PROCESSORS_NODE, PROCESSOR_NODE));
      this.input = inputConfig;
   }

   /** Initializing and setting Output configuration */
   private void initOutputConfig() {
      DelimitedOutputConfig outputConfig = new DelimitedOutputConfig();
      outputConfig.init(String.format("%s.%s", ROOT_PROCESSORS_NODE, PROCESSOR_NODE));
      this.output = outputConfig;
   }

   /** Initializing and setting Archive configuration */
   private void initArchiveConfig() {
      DelimitedArchiveConfig archiveConfig = new DelimitedArchiveConfig();
      archiveConfig.init(String.format("%s.%s", ROOT_PROCESSORS_NODE, PROCESSOR_NODE));
      this.archive = archiveConfig;
   }

   /** Initializing and setting Columns configuration */
   private void initColumns() {
      String columnsPath = String.format("%s.%s.%s.%s", ROOT_PROCESSORS_NODE, PROCESSOR_NODE, COLUMNS_NODE, COLUMN_SINGLE_NODE);

      // Getting column name from configuration
      String[] columnNames = Configuration.getInstance().getStringArrayConfigValue(columnsPath);

      List<DelimitedFileColumn> columnList = new ArrayList<>();

      // Creating a list of columns
      for (int i = 0; i < columnNames.length; i++) {

         // Getting exported attribute
         String exportedAttrPath = String.format("%s(%s)[@exported]", columnsPath, i);
         boolean exported = false;
         try {
            exported = Configuration.getInstance().getBooleanConfigValue(exportedAttrPath);
         } catch (NoSuchElementException e) {
            log.debug("Column "+columnNames[i]+" will not be exported");
         }
         columnList.add(new DelimitedFileColumn(columnNames[i], exported, i));

      }

      this.columns = columnList;

      log.debug("Columns configuration has been initialized");
   }

}
