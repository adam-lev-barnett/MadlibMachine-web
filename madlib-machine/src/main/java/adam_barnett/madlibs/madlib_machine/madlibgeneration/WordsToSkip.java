package adam_barnett.madlibs.madlib_machine.madlibgeneration;

import java.util.HashSet;
import java.util.Set;

public enum WordsToSkip {
    INSTANCE;

    /** Identifies where in the text file the words should be replaced with the user's new words */
    static final Set<String> wordsToSkip = new HashSet<>();

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
}
