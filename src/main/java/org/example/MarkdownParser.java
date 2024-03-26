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
    String boldRegex = "(?<![\\w`*\u0400-\u04FF])\\*\\*(\\S(?:.*?\\S)?)\\*\\*(?![\\w`*\u0400-\u04FF])";
    String italicRegex = "(?<![\\w`*\\u0400-\\u04FF])_(\\S(?:.*?\\S)?)_(?![\\w`*\\u0400-\\u04FF])";
    String monospacedRegex = "(?<![\\w`*\\u0400-\\u04FF])`(\\S(?:.*?\\S)?)`(?![\\w`*\\u0400-\\u04FF])";
    String preformattedRegex = "```([\\s\\S]*?)```";
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
        List<String> boldBlocks = getMatchPatternList(boldRegex, html);
        List<String> monospacedBlocks = getMatchPatternList(monospacedRegex, html);
        List<String> italicBlocks = getMatchPatternList(italicRegex, html);

        checkUnpairedMarkup(html);
        checkNested(boldRegex, italicRegex, monospacedBlocks);
        checkNested(boldRegex, monospacedRegex, italicBlocks);
        checkNested(italicRegex, monospacedRegex, boldBlocks);

        html = html.replaceAll(boldRegex, "<b>$1</b>");
        html = html.replaceAll(italicRegex, "<i>$1</i>");
        html = html.replaceAll(monospacedRegex, "<tt>$1</tt>");
        html = setParagraphs(html);
        return html;
    }

    private void checkNested(String firstRegex, String secondRegex, List<String> regexes) {
        Pattern firstPattern = Pattern.compile(firstRegex, Pattern.DOTALL);
        Pattern secondPattern = Pattern.compile(secondRegex, Pattern.DOTALL);
        for (String regex : regexes) {
            Matcher firstMatcher = firstPattern.matcher(regex);
            Matcher secondMatcher = secondPattern.matcher(regex);
            if (firstMatcher.find() || secondMatcher.find()) {
                throw new IllegalArgumentException("ERROR: nested markup");
            }
        }
    }

    private List<String> getMatchPatternList(String regex, String html) {
        List<String> regexList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            regexList.add(matcher.group(1));
        }
        return regexList;
    }

    private void checkUnpairedMarkup(String text) {
        text = text.replaceAll(boldRegex, "BOLD");
        text = text.replaceAll(italicRegex, "ITALIC");
        text = text.replaceAll(monospacedRegex, "MONO");
        hasUnpairedMarkup(text, "**");
        hasUnpairedMarkup(text, "`");
        hasUnpairedMarkup(text, "_");
    }

    private void hasUnpairedMarkup(String text, String markup) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.contains(markup) && (!word.endsWith(markup) || !word.startsWith(markup))) {
                throw new IllegalArgumentException("Error: unpaired markup");
            }
        }
    }

    private String removePreformattedText(String text) {
        Pattern preformattedPattern = Pattern.compile(preformattedRegex, Pattern.DOTALL);
        Matcher matcher = preformattedPattern.matcher(text);
        while (matcher.find()) {
            String preformattedBlock = matcher.group();
            if (!preformattedBlock.matches("(?s)```\\s*\n.*?\n```")) {
                throw new IllegalArgumentException("ERROR: invalid preformatted text");
            }
            preformattedText.add(matcher.group());
        }
        return text.replaceAll("```([\\s\\S]*?)```", "PRE");
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
