package com.maxkain.fileparser.utils;

import com.maxkain.fileparser.utils.filters.FileFilter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains helper methods for working with files
 */
@Log4j2
public class FileUtils {

    /**
     * Gets a list of files matching the filter in the directory
     *
     * @param directoryPath  Path to Directory
     * @param incomingFilter Filenames filter
     * @return List of files
     */
    public static List<Path> getFiles(String directoryPath, FileFilter incomingFilter) {

        log.debug("Reading a list of files in a directory " + directoryPath);

        Path         path = Paths.get(directoryPath);
        Stream<Path> list = null;

        try {
            list = Files.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list == null) {
            return new ArrayList<>();
        }

        return list.filter(incomingFilter::accept)
                .collect(Collectors.toList());
    }

    /**
     * Creates a file path in the specified directory
     *
     * @param directory Path to Directory
     * @param filename  Name of file
     * @return File path
     */
    public static Path createPath(String directory, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(directory);

        if (directory.charAt(directory.length() - 1) != File.separatorChar) {
            stringBuilder.append(File.separator);
        }

        stringBuilder.append(filename);

        return Paths.get(stringBuilder.toString());
    }

    /**
     * Archives the processed input file
     *
     * @param source             Original file
     * @param targetFolder       The directory in which the file will be moved
     * @param targetNameTemplate The name template that will have the archive copy of the file
     */
    public static void archiveFile(Path source, String targetFolder, String targetNameTemplate) throws IOException {
        String archiveFilename = FilenameUtils.generateOutputName(targetNameTemplate, source, 1);
        Path   archivePath     = createPath(targetFolder, archiveFilename);

        FileUtils.checkOrCreateDirectory(targetFolder);

        try {
            copyFile(source, archivePath);
        } catch (IOException e) {
            String message = String.format("Error while copying file %s to %s due archiving", source.toFile().getName(), archiveFilename);
            log.error(message, e);
            throw new IOException(message);
        }

        try {
           deleteFile(source);
        } catch (IOException e) {
            String message = String.format("Error while deleting file %s to %s due archiving", source.toFile().getName(), archiveFilename);
            log.error(message, e);
            throw new IOException(message);
        }
    }

    public static void checkOrCreateDirectory(String directoryPath) {
        if (!Files.exists(Paths.get(directoryPath))) {
            log.warn(String.format("Directory %s does not exit. Creating...", directoryPath));
            try {
                createDirectory(Paths.get(directoryPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copies file
     *
     * @param source Original file
     * @param target Copied file
     * @throws IOException
     */
    private static void copyFile(Path source, Path target) throws IOException {
        Files.copy(source, target);
        log.debug(String.format("File %s has been copied to %s", source.toString(), target.toString()));
    }

    /**
     * Deletes file
     *
     * @param source Original file
     * @throws IOException
     */
    private static void deleteFile(Path source) throws IOException {
        Files.delete(source);
        log.debug(String.format("File %s has been deleted", source.toString()));
    }

    /**
     * Creates directory
     *
     * @param path Original file
     * @throws IOException
     */
    private static void createDirectory(Path path) throws IOException {
        Files.createDirectory(path);
        log.debug(String.format("Directory %s has been created", path.toString()));
    }
}
