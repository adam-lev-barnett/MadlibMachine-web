package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MadlibFillerTest {
    static MadlibFiller madlibFiller;

    @BeforeAll
    public static void init() {
        madlibFiller = new MadlibFiller();
    }

    // Need to check
    @Test
    public void testMadlibFillerReplacesPOSBlocksAccurately() {
        String blankText = "The [adjective] [adjective] [verb, past-tense] [verb ending in -s] over the [adjective] [noun].";
        Queue<String> replacementWords = new ArrayDeque<>();
        replacementWords.add("goofy");
        replacementWords.add("purple");
        replacementWords.add("potato");
        replacementWords.add("wiggles");
        replacementWords.add("boring");
        replacementWords.add("clown");

        String result = madlibFiller.fillInMadlib(blankText, replacementWords);

        assertEquals("The goofy purple potato wiggles over the boring clown.", result);
    }
}
