package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that takes madlibified madlib (Stringwith some words replaced by [part of speech] blocks) and fills in with user's replacement words collected from the CLI*/

@Component
public class MadlibFiller {

    /** Responsible for replacing the part of speech blocks in the blanked madlib with the list of words prompted by the CLI
     * @param blankedMadlib Text resulting from the MadlibBlanker that has a number of words replaced by text blocks displaying their associated parts of speech
     * @param replacementWords The list of words the user chose to replace each removed word.
     * Note: The logic for replacing "a" or "an" with the proper article differs enough between the first word check and subsequent checks where it becomes to complex to refactor the logic into its own method*/
    public String fillInMadlib(@NotNull String blankedMadlib, Queue<String> replacementWords) {

        // No replacement words were entered, so no need to continue method
        if (replacementWords.isEmpty() || blankedMadlib.isEmpty()) return blankedMadlib;

        // Use string buffer for thread safety
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\[([\\w- ,]+)]");

        String firstWord = blankedMadlib.substring(0, 2);

        if (firstWord.equalsIgnoreCase("a ") ||
                firstWord.equalsIgnoreCase("an")) {

            // Shouldn't be null due to empty check at the beginning of the method
            String firstReplacementWord = replacementWords.peek();

            if (correctArticleFor(firstReplacementWord).equals("an") &&
                    firstWord.toLowerCase().startsWith("a")) {
                sb.append("An ");
                blankedMadlib = blankedMadlib.substring(2);

            }

            else if (correctArticleFor(firstReplacementWord).equals("a") &&
                        firstWord.toLowerCase().startsWith("an")) {
                sb.append("A ");
                blankedMadlib = blankedMadlib.substring(3);
            }
        }

        Matcher matcher = pattern.matcher(blankedMadlib);
        int lastAppendPosition = 0;

        while (matcher.find()) {
            String posLiteral = matcher.group(1);
            if (replacementWords.isEmpty() || !PosMap.posMap.containsValue(posLiteral)) {
                sb.append(blankedMadlib, lastAppendPosition, matcher.end());
                lastAppendPosition = matcher.end();
                continue;
            }
            String replacementWord = replacementWords.poll().toLowerCase();

            /* Choose correct article "a" versus "an" to prepend before replacement word, if applicable:
                • Read up to 4 characters immediately before the current match in blankedMadlib
                • Check if they end with " a " or " an " - assign article to non-null
                • If non-null, identify if replacement word begins with a vowel and assign "a" or "an" to the appropriate value
                • If the actual article isn't already correct, append the inter-match text up to (not including)
                  the old article, then append the corrected article + replacement word
             */
            String article = null;
            String potentialArticle = blankedMadlib.substring(Math.max(0, matcher.start() - 4), matcher.start());

            if (potentialArticle.equals(" a ") || potentialArticle.equals(" a\n")) {
                article = "a";
            }
            else if (potentialArticle.equals(" an ") || potentialArticle.equals(" an\n")) {
                article = "an";
            }

            if (article != null) {
                String correctArticle = correctArticleFor(replacementWord);
                if (!correctArticle.equals(article)) {
                    // articleStart is the position of the first letter of the old article in blankedMadlib.
                    // Append everything up to (not including) that letter; this preserves the space before it.
                    int articleStart = matcher.start() - article.length() - 1;
                    sb.append(blankedMadlib, lastAppendPosition, articleStart);
                    sb.append(correctArticle).append(" ").append(replacementWord);
                    lastAppendPosition = matcher.end();
                    continue;
                }
            }

            sb.append(blankedMadlib, lastAppendPosition, matcher.start());
            sb.append(replacementWord);
            lastAppendPosition = matcher.end();
        }

        // Add anything left after the last match
        sb.append(blankedMadlib, lastAppendPosition, blankedMadlib.length());

        return sb.toString();
    }

    /** Determines the correct preceding article for the replacement word*/
    private String correctArticleFor(String word) {
        return word.matches("(?i)[aeiouAEIOU].*") ? "an" : "a";
    }

}
