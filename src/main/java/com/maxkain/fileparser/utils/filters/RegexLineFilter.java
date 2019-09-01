package com.maxkain.fileparser.utils.filters;

import java.util.regex.Pattern;

/**
 * Filter line according to a given regexp pattern
 */
public class RegexLineFilter implements LineFilter {
    private String pattern;

    public RegexLineFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(String line) {
        return Pattern.matches(pattern, line);
    }
}
