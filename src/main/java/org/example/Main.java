package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("java Main <inputFile> [--out outputFile]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;

        if (args.length > 2 && "--out".equals(args[1])) {
            outputFile = args[2];
        }

        MarkdownParser markdownParser = new MarkdownParser(inputFile, outputFile);
        try {
            markdownParser.parse();
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            System.exit(1);
        } catch (IllegalStateException e) {
            System.err.println("Error parsing Markdown: " + e.getMessage());
            System.exit(1);
        }
    }
}
