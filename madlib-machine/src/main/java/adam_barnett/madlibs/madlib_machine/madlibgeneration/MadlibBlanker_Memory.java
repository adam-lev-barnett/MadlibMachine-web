package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import adam_barnett.madlibs.madlib_machine.tagger.TextAnnotater;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.TextNotProcessedException;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/** Processes Madlib_File through the "blanking" process, which takes the madlib's source text and blanks out certain words to be filled in later by the user.
 * @see Madlib_File
 * @see MadlibFiller
 * @author Adam Barnett */
class MadlibBlanker_Memory {
    private StringBuilder sb;

    /** Identifies where in the text file the words should be replaced with the user's new words */
    private static final Set<String> wordsToSkip = new HashSet<>();

    /** Reference to Madlib_File's pos list to verify correct blanking of word. This is important in case the source text already has words within square brackets */
    private static final Map<String, String> posMap = Madlib_File.getPosMap();

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
    }

    /** Removes the skipper-th word with a part of speech in the posBlocks hashset
     * @param skipper determines the frequency of madlibification (replacement of word with part-of-speech text block). Example: if skipper == 3, removeMadlibifiables will clear every third madlibifiable word
     * @see Madlib_File for more skipper information
     * @return returns List of parts of speech removed so user can replace the removed words when prompted by CLI */
    public List<String> removeMadlibifiables(TextAnnotater annotatedText, int skipper) throws IOException, TextNotProcessedException, InvalidPartOfSpeechException {

        if (skipper < 1) {
            skipper = 1;
            System.out.println("Invalid skip increment. Skip increment auto set to 1.");
        }
        int i = 1;


        String replacementBlock;
        // posList stores parts of speech for each removed word; list is passed to method that prompts user to input replacement words based on the POS
        ArrayList<String> posList = new ArrayList<>();

        for (CoreLabel token : annotatedText.getDocument().tokens()) {

            // First word won't have a space added before it
            boolean isFirstWord = annotatedText.getDocument().tokens().indexOf(token) == 0;

            // Retrieve the [part of speech block] to replace the word in the new madlib
            // Map above returns null if part of speech can't be madlibified
            replacementBlock = posMap.get((token.get(CoreAnnotations.PartOfSpeechAnnotation.class)));

            // disregard any words in wordsToSkip by resetting the block to null
            if (wordsToSkip.contains(token.word())) replacementBlock = null;

            if (i < skipper) {
                justWriteWord(token, isFirstWord);
                // i only increments when the current word is madlibifiable
                if (replacementBlock != null) i++;
            }
            // the skipper count resets after a word is madlibified
            else {
                if (replacementBlock != null) {
                    replaceWordWithBlock(isFirstWord, replacementBlock);
                    posList.add(replacementBlock);
                    i = 1;
                }
                else {
                    justWriteWord(token, isFirstWord);
                }
            }
        }
        return Collections.unmodifiableList(posList);
    }

    /** Like justWriteWord but handles Strings instead of tokens to print the part of speech returned by the part of speech map inside square brackets */
    private void replaceWordWithBlock(boolean isFirstWord, String replacementBlock) throws IOException, InvalidPartOfSpeechException {
        if (!posMap.containsValue(replacementBlock)) {
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
    private void justWriteWord(CoreLabel token, boolean isFirstWord) throws IOException {

        if (token.word().matches("\\p{Punct}") || isFirstWord) {
            sb.append(token.get(CoreAnnotations.TextAnnotation.class));
        }
        else sb.append(" " + token.get(CoreAnnotations.TextAnnotation.class));
    }

    /** Returns blanked madlib to Madlib object and clears the stringbuilder because the stored value is no longer needed*/
    String extractBlankMadlib() {
        String blankMadlib = sb.toString();

        // Clear stringbuilder instance from memory because it will be stored in the madlib object itself
        sb = new StringBuilder();

        return blankMadlib;
    }
}
