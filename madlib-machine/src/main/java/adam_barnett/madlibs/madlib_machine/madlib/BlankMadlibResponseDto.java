package adam_barnett.madlibs.madlib_machine.madlib;

import java.util.Queue;

public record BlankMadlibResponseDto(String blankedText, Queue<String> partsOfSpeech) {
}
