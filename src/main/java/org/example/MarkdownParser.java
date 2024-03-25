package org.example;


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
}
