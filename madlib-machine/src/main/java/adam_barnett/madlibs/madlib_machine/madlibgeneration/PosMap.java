package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import java.util.HashMap;
import java.util.Map;

/** Controls the parts of speech annotations you want to pull and be able to replace from CoreNLP*/
public enum PosMap {

    // Singleton
    INSTANCE;

    static final Map<String, String> posMap = new HashMap<>();

    // Ensure that any punctuation included in the POS map is adjusted in the MadlibFiller regex
    static {
        // Comment out parts of speech you don't want to blank
        posMap.put("NN", "noun");
        posMap.put("NNS", "plural noun");
        posMap.put("VB", "verb");
        posMap.put("VBD", "verb, past-tense");
        posMap.put("VBG", "verb ending in -ing");
        posMap.put("VBZ", "verb ending in -s");
        posMap.put("JJ", "adjective");
        // posReplacements.put("JJR", "adjective ending in \"er\"");
        posMap.put("RB", "adverb");
        // posReplacements.put("RBS", "adverb ending in \"est\"");
        posMap.put("UH", "interjection");
    }
}
