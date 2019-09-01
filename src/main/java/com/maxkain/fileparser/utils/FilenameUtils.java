package com.maxkain.fileparser.utils;

import org.stringtemplate.v4.ST;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains helper methods for working with filenames
 */
public class FilenameUtils {

    /**
     * Creates a file name using the passed pattern
     *
     * @param template  Filename template
     *                  Allow use {INPUT_NAME}, {INPUT_EXT}, {SEQ}, {DATE}, {TIME}, {DATE_TIME} placeholders
     * @param inputPath Input file
     * @param seq       Current input file number in processing
     * @return Generated filename
     */
    public static String generateOutputName(String template, Path inputPath, int seq) {
        ST tpl = new ST(template, '{', '}');

        // Replace {DATE} placeholder with current date
        tpl.add("DATE", getDateFormatted());

        // Replace {TIME} placeholder with current time
        tpl.add("TIME", getTimeFormatted());

        // Replace {DATE_TIME} placeholder with current date and time
        tpl.add("DATE_TIME", getDatetimeFormatted());

        // Replace {INPUT_NAME} placeholder with name of Input file
        tpl.add("INPUT_NAME", getNameFromPath(inputPath));

        // Replace {INPUT_EXT} placeholder with extension of Input file
        tpl.add("INPUT_EXT", getExtensionFromPath(inputPath));

        // Replace {SEQ} placeholder with current input file number in processing
        tpl.add("SEQ", seq);

        return tpl.render();
    }

    /**
     * Gets the file name
     *
     * @param path File in filesystem
     * @return File name
     */
    public static String getNameFromPath(Path path) {
        String fname = path.toFile().getName();
        int    pos   = fname.lastIndexOf(".");
        if (pos > 0) {
            fname = fname.substring(0, pos);
        }
        return fname;
    }

    /**
     * Gets the file extension
     *
     * @param path File in filesystem
     * @return File extension
     */
    public static String getExtensionFromPath(Path path) {
        String fname = path.toFile().getName();
        int    pos   = fname.lastIndexOf(".");
        if (pos > 0) {
            fname = fname.substring(pos + 1);
        }
        return fname;
    }

    private static String getDateFormatted() {
        return getFormattedDate("yyyy-mm-dd");
    }

    private static String getTimeFormatted() {
        return getFormattedDate("HH_mm_ss");
    }

    private static String getDatetimeFormatted() {
        return getFormattedDate("yyyy-mm-dd_HH_mm_ss");
    }

    private static String getFormattedDate(String format) {
        Date       date       = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


}
