package adam_barnett.madlibs.madlib_machine.madlib;

import java.util.List;

/** DTO to match frontend fetch so user can fill out the madlib with replacement words */
public record BlankMadlibResponse(String blankedText, List<String> partsOfSpeech) {
}
