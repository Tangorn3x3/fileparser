package com.maxkain.fileparser.utils.filters;

import lombok.extern.log4j.Log4j2;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 * Filter files according to a given glob pattern
 */
@Log4j2
public class InputFileFilter implements FileFilter {
    private static final String PATTERN_PREFIX = "glob:**\\\\";

    private String pattern;

    public InputFileFilter(String pattern) {
        this.pattern = PATTERN_PREFIX + pattern;
    }

    /**
     * Checks if the name of the file matches the specified pattern
     *
     * @param path file in filesystem
     * @return result of checking
     */
    @Override
    public boolean accept(Path path) {
        PathMatcher matcher =
                FileSystems.getDefault().getPathMatcher(pattern);

        boolean result = matcher.matches(path) && Files.isRegularFile(path);

        if (result) log.debug("Accepted file: " + path);

        return result;
    }
}
