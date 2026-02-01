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
     * @param replacementWords The list of words the user chose to replace each removed word (prompted by the CLI class). */

    public String fillInMadlib(@NotNull String blankedMadlib, Queue<String> replacementWords) {

        // Use string buffer for thread safety
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("\\[([\\w- ,]+)]");

        Matcher matcher = pattern.matcher(blankedMadlib);

        while (matcher.find()) {
            String posLiteral = matcher.group(1);
            if (replacementWords.isEmpty() || !PosMap.posMap.containsValue(posLiteral)) {
                matcher.appendReplacement(sb, matcher.group());
                continue;
            }

            String replacementWord = replacementWords.poll();

            // Use quoteReplacement over the replacement word due to appendReplacement following RegEx formatting
            // appendReplacement appends everything since the previous match and then replaces the next word
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacementWord));
        }

        // Add anything left in the scanner
        matcher.appendTail(sb);

        return sb.toString();
    }

}
