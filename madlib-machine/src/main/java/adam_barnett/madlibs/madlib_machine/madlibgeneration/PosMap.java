package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import java.util.HashMap;
import java.util.Map;

public enum PosMap {

    INSTANCE;

    static final Map<String, String> posMap = new HashMap<>();

    static {
        // Comment out parts of speech you don't want to blank
        posMap.put("NN", "noun");
        posMap.put("NNS", "pluralNoun");
        posMap.put("VB", "verb");
        posMap.put("VBD", "verbPast");
        // posReplacements.put("VBG", "gerund");
        posMap.put("VBZ", "verbEndingInS");
        posMap.put("JJ", "adjective");
        // posReplacements.put("JJR", "adjective ending in \"er\"");
        posMap.put("RB", "adverb");
        // posReplacements.put("RBS", "adverb ending in \"est\"");
        posMap.put("UH", "interjection");
    }
}
