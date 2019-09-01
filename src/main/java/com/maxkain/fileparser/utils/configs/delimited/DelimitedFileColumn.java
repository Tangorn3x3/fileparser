package com.maxkain.fileparser.utils.configs.delimited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Describes the settings of a column in a row
 */

@Getter @Setter
@AllArgsConstructor
public class DelimitedFileColumn {

    /** Column name */
    private String name;

    /** Is the column exported to the output file */
    private boolean exported;

    /** Column position in input line */
    private int position;

}
