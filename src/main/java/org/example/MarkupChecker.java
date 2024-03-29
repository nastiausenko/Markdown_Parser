package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkupChecker {
    private final String[] markups = new String[]{"**", "```", "`", "_"};
    public void checkNested(String firstRegex, String secondRegex, List<String> regexes) {
        Pattern firstPattern = Pattern.compile(firstRegex, Pattern.DOTALL);
        Pattern secondPattern = Pattern.compile(secondRegex, Pattern.DOTALL);
        for (String regex : regexes) {
            Matcher firstMatcher = firstPattern.matcher(regex);
            Matcher secondMatcher = secondPattern.matcher(regex);
            if (firstMatcher.find() || secondMatcher.find()) {
                throw new MarkdownException("ERROR: nested markup");
            }
        }
    }

    public void checkUnpairedMarkup(String text) {
        checkUnpairedPreformatted(text);
        for (String markup : markups) {
            checkInnerMarkup(text, markup);
            hasUnpairedMarkup(text, markup);
        }
    }

    private void hasUnpairedMarkup(String text, String markup) {
        text = text.replaceAll(MarkdownParser.BOLD_REGEX, "BOLD");
        text = text.replaceAll(MarkdownParser.ITALIC_REGEX, "ITALIC");
        text = text.replaceAll(MarkdownParser.MONOSPACED_REGEX, "MONO");

        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.startsWith(markup) && !word.endsWith(markup) ||
                word.endsWith(markup) && !word.startsWith(markup)) {
                throw new MarkdownException("ERROR: unpaired markup");
            }
        }
    }

    private void checkInnerMarkup(String text, String markup) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            hasInnerMarkup(word, markup);
        }
    }

    private void checkUnpairedPreformatted(String text) {
        List<String> preformattedMarkupList = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (String word: words) {
            if (word.equals("```")) {
                preformattedMarkupList.add(word);
            }
        }
        if (preformattedMarkupList.size() % 2  != 0) {
            throw new MarkdownException("ERROR: unpaired markup");
        }
    }

    private void hasInnerMarkup(String word, String markup) {
        String result;
        String regex = ".*[A-Za-z0-9].*";
        for (String mark : markups) {
            if (word.startsWith(markup) && word.matches(regex)) {
                result = word.substring(markup.length());
                if (result.startsWith(mark)) {
                    throw new MarkdownException("ERROR: unpaired nested markup");
                }
            }
            if (word.endsWith(markup) && word.matches(regex)) {
                result = word.substring(0, word.length() - markup.length());
                if (result.endsWith(mark)) {
                    throw new MarkdownException("ERROR: unpaired nested markup");
                }
            }
        }
    }
}
