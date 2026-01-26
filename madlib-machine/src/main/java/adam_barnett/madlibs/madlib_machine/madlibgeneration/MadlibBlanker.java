package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import adam_barnett.madlibs.madlib_machine.madlib.BlankMadlibResponse;
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

    /** Identifies where in the text file the words should be replaced with the user's new words */
    private static final Set<String> wordsToSkip = new HashSet<>();

    static {
        // List of words to avoid that have the accepted parts of speech
        wordsToSkip.add("be");
        wordsToSkip.add("being");
        wordsToSkip.add("am");
        wordsToSkip.add("not");
        wordsToSkip.add("using");
        wordsToSkip.add("uses");
        wordsToSkip.add("use");
        wordsToSkip.add("used");
        wordsToSkip.add("have");
        wordsToSkip.add("having");
        wordsToSkip.add("has");
        wordsToSkip.add("had");
        wordsToSkip.add("shall");
        wordsToSkip.add("is");
        wordsToSkip.add("was");
        wordsToSkip.add("were");
        wordsToSkip.add("isn't");
        wordsToSkip.add("behalf");
        wordsToSkip.add("can");
        wordsToSkip.add("cannot");
        wordsToSkip.add("can't");
        wordsToSkip.add("will");
        wordsToSkip.add("won't");
        wordsToSkip.add("would");
        wordsToSkip.add("must");
        wordsToSkip.add("might");
        wordsToSkip.add("may");
        wordsToSkip.add("should");
        wordsToSkip.add("could");
        wordsToSkip.add("does");
        wordsToSkip.add("did");
        wordsToSkip.add("do");
        wordsToSkip.add("doing");
        wordsToSkip.add("about");
        wordsToSkip.add("that");
        wordsToSkip.add("this");
        wordsToSkip.add("they");
        wordsToSkip.add("he");
        wordsToSkip.add("she");
        wordsToSkip.add("my");
        wordsToSkip.add("yours");
        wordsToSkip.add("his");
        wordsToSkip.add("hers");
        wordsToSkip.add("theirs");
    }

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

        List<CoreLabel> tokens = annotatedText.getDocument().tokens();

        // First word won't have a space added before it
        for (int tokenIndex = 0; tokenIndex < tokens.size(); tokenIndex++) {

            CoreLabel token = tokens.get(tokenIndex);
            boolean isFirstWord = (tokenIndex == 0);

            // Retrieve the [part of speech block] to replace the word in the new madlib
            // Map returns null if part of speech can't be madlibified
            replacementBlock =
                    PosMap.posMap.get(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));

            // Disregard any words in wordsToSkip by resetting the block to null
            if (wordsToSkip.contains(token.word().toLowerCase())) {
                replacementBlock = null;
            }

            if (replacementBlock == null) {
                // Word is not madlibifiable; just write it
                justWriteWord(token, isFirstWord, sb);
                continue;
            }

            if (i < skipper) {
                justWriteWord(token, isFirstWord, sb);
                // i only increments when the current word is madlibifiable
                i++;
            }
            // the skipper count resets after a word is madlibified
            else {
                replaceWordWithBlock(isFirstWord, replacementBlock, sb);
                posList.add(replacementBlock);
                i = 1;
            }
        }

        return new BlankMadlibResponse(sb.toString(), posList);
    }

    /** Like justWriteWord but handles Strings instead of tokens to print the part of speech returned by the part of speech map inside square brackets */
    private void replaceWordWithBlock(boolean isFirstWord, String replacementBlock, StringBuilder sb) throws InvalidPartOfSpeechException {
        if (!PosMap.posMap.containsValue(replacementBlock)) {
            sb.append("[YouMessedUp]");
            throw new InvalidPartOfSpeechException("Passed invalid part of speech. Replacing word with [YouMessedUp]");
        }
        if (isFirstWord) {
            sb.append("[" + replacementBlock + "]");
        }
        else sb.append(" [" + replacementBlock + "]");
    }

    /** Helper method for removeMadlibifiable() that writes each word to a file with a preceding space. Adds space before each word for simple avoidance of spaces before punctuation.
     * Nothing is added to the punctuation character itself*/
    private void justWriteWord(CoreLabel token, boolean isFirstWord, StringBuilder sb) {

        if (token.word().matches("\\p{Punct}") || isFirstWord) {
            sb.append(token.get(CoreAnnotations.TextAnnotation.class));
        }
        else sb.append(" " + token.get(CoreAnnotations.TextAnnotation.class));
    }

}
