package adam_barnett.madlibs.madlib_machine.madlib;

import java.util.List;

public record FillMadlibRequestDto(String blankedText, List<String> partsOfSpeech) {
}
