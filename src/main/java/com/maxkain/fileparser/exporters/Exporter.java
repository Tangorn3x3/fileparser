package com.maxkain.fileparser.exporters;

import java.io.IOException;
import java.util.List;

public interface Exporter {
    void exportLine(List<String> line);
    void closeWriter() throws IOException;
}
