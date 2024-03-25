package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MarkdownParser {
    private final String path;
    private final String out;
    private final String boldRegex = "(?<![\\\\w`*\\u0400-\\u04FF])\\\\*\\\\*(\\\\S(?:.*?\\\\S)?)\\\\*\\\\*(?![\\\\w`*\\u0400-\\u04FF])";
    private final String italicRegex = "(?<![\\\\w`*\\\\u0400-\\\\u04FF])_(\\\\S(?:.*?\\\\S)?)_(?![\\\\w`*\\\\u0400-\\\\u04FF])";
    private final String monospacedRegex = "(?<![\\\\w`*\\\\u0400-\\\\u04FF])`(\\\\S(?:.*?\\\\S)?)`(?![\\\\w`*\\\\u0400-\\\\u04FF])";
    private final String preformatedRegex = "(?m)(^\\\\n?|^)```(.*?)```(\\\\n?|$)";

    public MarkdownParser(String path, String out) {
        this.path = path;
        this.out = out;
    }

    public void parse() throws IOException {
        String file = readFile();

        if (out != null) {
            writeFile(file, out);
        } else {
            System.out.println(file);
        }
    }

    private String readFile() throws IOException {
        Path pathToFile = Paths.get(path);
        if(!Files.exists(pathToFile)) {
            throw new IOException("File not found");
        }
        return Files.readString(pathToFile);
    }

    private void writeFile(String text, String outputFile) throws IOException {
        Path outputPath = Paths.get(outputFile);
        if (!Files.exists(outputPath.getParent())) {
            Files.createDirectories(outputPath.getParent());
        }
        Files.writeString(outputPath, text);
    }
}
