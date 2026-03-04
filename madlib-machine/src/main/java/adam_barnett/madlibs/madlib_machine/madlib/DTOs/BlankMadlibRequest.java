package adam_barnett.madlibs.madlib_machine.madlib.DTOs;

/**DTO to match frontend POST for original madlib submission*/
public record BlankMadlibRequest(String sourceText, Integer skipper) {
}
