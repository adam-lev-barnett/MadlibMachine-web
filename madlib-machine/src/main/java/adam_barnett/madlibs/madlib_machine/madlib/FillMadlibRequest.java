package adam_barnett.madlibs.madlib_machine.madlib;

import java.util.List;

public record FillMadlibRequest(String blankedText, List<String> replacementWords) {
}
