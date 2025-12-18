package adam_barnett.madlibs.madlib_machine.utility.filehandling;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.NullPathException;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Utility class that operates file loading from other classes to return the text that will be operated on */
public abstract class TextFileLoader {

    /** Returns the text of various states of madlib text so CLI and Madlib_File operate on Strings instead of files */
    @Contract("null -> fail")
    public static @NotNull String loadTextFile(Path path) throws IOException, NullPathException, TextNotProcessedException {

        if (path == null) throw new NullPathException("Path cannot be null");

        if (!Files.exists(path)) {
            throw new TextNotProcessedException("txt file could not be parsed. You blew it. Exiting program.");
        }

        return Files.readString(path);
    }
}
