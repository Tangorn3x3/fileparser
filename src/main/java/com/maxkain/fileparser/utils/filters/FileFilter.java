package com.maxkain.fileparser.utils.filters;

import java.nio.file.Path;

public interface FileFilter {
    boolean accept(Path path);
}
