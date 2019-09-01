package com.maxkain.fileparser.parcers;

import java.util.List;

public interface Parser {
    List<String> parseLine(String line);
}
