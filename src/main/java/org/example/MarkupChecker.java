package org.example;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.MarkdownParser.*;

public class MarkupChecker {
    private final String[] markups = new String[]{"**", "```", "`", "_"};
    public void checkNested(String firstRegex, String secondRegex, List<String> regexes) {
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

    public void checkUnpairedMarkup(String text) {
        text = text.replaceAll(BOLD_REGEX, "BOLD");
        text = text.replaceAll(ITALIC_REGEX, "ITALIC");
        text = text.replaceAll(MONOSPACED_REGEX, "MONO");
        for (String markup: markups) {
            hasUnpairedMarkup(text, markup);
        }
    }

    private void hasUnpairedMarkup(String text, String markup) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.startsWith(markup) && !word.endsWith(markup) ||
                    word.endsWith(markup) && !word.startsWith(markup)) {
                throw new IllegalArgumentException("Error: unpaired markup");
            }
        }
    }
}
