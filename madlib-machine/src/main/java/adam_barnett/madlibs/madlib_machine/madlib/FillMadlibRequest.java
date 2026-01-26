package adam_barnett.madlibs.madlib_machine.madlib;

import java.util.List;

/** DTO that POSTS the user-submitted words and the already-blanked madlib to the backend endpoint to return the completed madlib*/
public record FillMadlibRequest(String blankedText, List<String> replacementWords) {
}
