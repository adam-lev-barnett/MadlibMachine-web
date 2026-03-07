package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import adam_barnett.madlibs.madlib_machine.madlib.DTOs.BlankMadlibResponse;
import adam_barnett.madlibs.madlib_machine.tagger.TextAnnotater;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import org.springframework.stereotype.Component;

import java.util.*;

/** Processes Madlib_File through the "blanking" process, which takes the madlib's source text and blanks out certain words to be filled in later by the user.

 * @author Adam Barnett */

@Component
public class MadlibBlanker {

    /** Removes the skipper-th word with a part of speech in the posBlocks hashset
     * @param skipper determines the frequency of madlibification (replacement of word with part-of-speech text block). Example: if skipper == 3, removeMadlibifiables will clear every third madlibifiable word
     * @return returns List of parts of speech removed so user can replace the removed words when prompted by CLI */
    public BlankMadlibResponse removeMadlibifiables(TextAnnotater annotatedText, int skipper)
            throws InvalidPartOfSpeechException {

        StringBuilder sb = new StringBuilder();

        if (skipper < 1) {
            skipper = 1;
            System.out.println("Invalid skip increment. Skip increment auto set to 1.");
        }

        // i tracks how many madlibifiable words have been seen since the last replacement
        int i = 1;

        String replacementBlock;

        // posList stores parts of speech for each removed word; list is passed to method that
        // prompts user to input replacement words based on the POS
        ArrayList<String> posList = new ArrayList<>();

        String originalText = annotatedText.getDocument().text();
        List<CoreLabel> tokens = annotatedText.getDocument().tokens();
        int prevEnd = 0;

        for (CoreLabel token : tokens) {

            int tokenStart = token.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
            int tokenEnd = token.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
            String precedingWhitespace = originalText.substring(prevEnd, tokenStart);

            // Retrieve the [part of speech block] to replace the word in the new madlib
            // Map returns null if part of speech can't be madlibified
            replacementBlock =
                    PosMap.posMap.get(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));

            // Disregard any words in wordsToSkip by resetting the block to null
            if (WordsToSkip.wordsToSkip.contains(token.word().toLowerCase())) {
                replacementBlock = null;
            }

            if (replacementBlock == null) {
                sb.append(precedingWhitespace).append(token.get(CoreAnnotations.TextAnnotation.class));
            } else if (i < skipper) {
                sb.append(precedingWhitespace).append(token.get(CoreAnnotations.TextAnnotation.class));
                // i only increments when the current word is madlibifiable
                i++;
            }
            // the skipper count resets after a word is madlibified
            else {
                if (!PosMap.posMap.containsValue(replacementBlock)) {
                    sb.append("[YouMessedUp]");
                    throw new InvalidPartOfSpeechException("Passed invalid part of speech. Replacing word with [YouMessedUp]");
                }
                sb.append(precedingWhitespace).append("[").append(replacementBlock).append("]");
                posList.add(replacementBlock);
                i = 1;
            }

            prevEnd = tokenEnd;
        }

        return new BlankMadlibResponse(sb.toString(), posList);
    }

}
