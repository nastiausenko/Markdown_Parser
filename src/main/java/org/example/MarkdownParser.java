package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MarkdownParser {
    private final String path;
    private final String out;
    static final String BOLD_REGEX = "(?<![\\w`*\u0400-\u04FF])\\*\\*(\\S(?:.*?\\S)?)\\*\\*(?![\\w`*\u0400-\u04FF])";
    static final String ITALIC_REGEX = "(?<![\\w`*\\u0400-\\u04FF])_(\\S(?:.*?\\S)?)_(?![\\w`*\\u0400-\\u04FF])";
    static final String MONOSPACED_REGEX = "(?<![\\w`*\\u0400-\\u04FF])`(\\S(?:.*?\\S)?)`(?![\\w`*\\u0400-\\u04FF])";
    private static final String PREFORMATTED_REGEX = "```([\\s\\S]*?)```";
    private final List<String> preformattedText = new ArrayList<>();

    public MarkdownParser(String path, String out) {
        this.path = path;
        this.out = out;
    }

    public void parse() throws IOException {
        String file = readFile();

        file = removePreformattedText(file);
        file = processInlineElements(file);
        file = setPreformattedText(file);

        if (out != null) {
            writeFile(file, out);
        } else {
            System.out.println(file);
        }
    }

    private String readFile() throws IOException {
        Path pathToFile = Paths.get(path);
        if (!Files.exists(pathToFile)) {
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

    private String processInlineElements(String html) {
        MarkupChecker markupChecker = new MarkupChecker();

        markupChecker.checkUnpairedMarkup(html);

        html = html.replaceAll(BOLD_REGEX, "<b>$1</b>");
        html = html.replaceAll(ITALIC_REGEX, "<i>$1</i>");
        html = html.replaceAll(MONOSPACED_REGEX, "<tt>$1</tt>");
        html = setParagraphs(html);
        return html;
    }

    private String removePreformattedText(String text) {
        Pattern preformattedPattern = Pattern.compile(PREFORMATTED_REGEX, Pattern.DOTALL);
        Matcher matcher = preformattedPattern.matcher(text);
        while (matcher.find()) {
            String preformattedBlock = matcher.group();
            if (!preformattedBlock.matches("(?s)```\\s*\n.*?\n```")) {
                throw new MarkdownException("ERROR: invalid preformatted text");
            }
            preformattedText.add(matcher.group());
        }
        return text.replaceAll(PREFORMATTED_REGEX, "PRE");
    }

    private String setPreformattedText(String text) {
        for (String cur : preformattedText) {
            String html = "<pre>" + cur.replaceAll("```", "") + "</pre>";
            text = text.replaceFirst("PRE", html);
        }
        return text;
    }

    private String setParagraphs(String text) {
        String[] paragraphs = text.split(System.lineSeparator());
        StringBuilder result = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (!paragraph.isEmpty()) {
                result.append("<p>").append(paragraph.trim()).append("</p>\n");
            }
        }
        return result.toString().trim();
    }
}
