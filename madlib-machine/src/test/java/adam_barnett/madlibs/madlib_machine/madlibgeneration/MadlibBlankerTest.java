package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import adam_barnett.madlibs.madlib_machine.madlib.BlankMadlibResponse;
import adam_barnett.madlibs.madlib_machine.tagger.TextAnnotater;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MadlibBlankerTest {
    static String sampleString;
    static TextAnnotater textAnnotater;
    static MadlibBlanker madlibBlanker;

    @BeforeAll
    static void setUp() {
        sampleString = "The quick brown fox jumps over the lazy dog";
        textAnnotater = new TextAnnotater(sampleString);
        madlibBlanker = new MadlibBlanker();
    }

    @Test
    void testRemoveEveryMadlibifiablesRemovesAllWords() throws InvalidPartOfSpeechException {
        // Skipper set to 1 to ensure all words skipped
        BlankMadlibResponse result = madlibBlanker.removeMadlibifiables(textAnnotater, 1);
        assertEquals("The [adjective] [adjective] [noun] [verb ending in -s] over the [adjective] [noun]", result.blankedText());
    }

    @Test
    void testRemoveMadlibifiablesExtractsAllPartsOfSpeech() throws InvalidPartOfSpeechException {
        // Skipper set to 1 to ensure all words skipped
        ArrayList<String> partsOfSpeech = new ArrayList<>();
        partsOfSpeech.add("adjective");
        partsOfSpeech.add("adjective");
        partsOfSpeech.add("noun");
        partsOfSpeech.add("verb ending in -s");
        partsOfSpeech.add("adjective");
        partsOfSpeech.add("noun");


        BlankMadlibResponse result = madlibBlanker.removeMadlibifiables(textAnnotater, 1);
        assertEquals(partsOfSpeech, result.partsOfSpeech());
    }

    @Test
    void testRemoveMadlibifiablesWithHighSkipperReturnsOriginalString() throws InvalidPartOfSpeechException {
        BlankMadlibResponse result = madlibBlanker.removeMadlibifiables(textAnnotater, 5000000);
        assertEquals(sampleString, result.blankedText());

        // Parts of speech list should also be empty because no words were extracted
        assertTrue(result.partsOfSpeech().isEmpty());
    }


}
