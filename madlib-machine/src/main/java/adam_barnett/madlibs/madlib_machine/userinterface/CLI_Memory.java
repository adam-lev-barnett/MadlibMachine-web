package adam_barnett.madlibs.madlib_machine.userinterface;

import adam_barnett.madlibs.madlib_machine.madlibgeneration.Madlib;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.NullPOSListException;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.TextNotProcessedException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Singleton command-line interface controller responsible for parsing user input to create blanked and filled-in madlibs.
 * Enum singleton to maintain thread safety, readability, and serialization.
 * @see Madlib
 * @author Adam Barnett */
public enum CLI_Memory {
    INSTANCE;

    private static final Scanner SCANNER = new Scanner(System.in);

    /** Parsing logic to ensure user is inputting valid numbers when prompted */
    private static final Pattern DIGITS = Pattern.compile("[0-9]+");

    /** Used by Main to begin program logic*/
    public static CLI_Memory getInstance() {
        return INSTANCE;
    }

    /** Main controller logic that facilitates the madlib creation process in steps by requesting filenames, calling helper methods, and confirming successful text processing*/

    public void initiateMadlibCreation() throws NullPointerException, TextNotProcessedException, IOException, NullPOSListException, InvalidPartOfSpeechException {
        System.out.println("Welcome to the Madlib_File Machine!");
        System.out.println();

        System.out.println("Please enter text you want to madlibify");
        String sourceText = SCANNER.nextLine();

        if (sourceText.equals("quit")) return;

        /*  skipper variable: prompts user for how many madlibifiable words will be skipped before a madlibifiable word is blanked
         *  Madlibifiable words are words with parts of speech accepted by the Madlib_File Machine (nouns, adjectives, etc.).
         *  See Madlib_File class for list of accepted parts of speech as well as madlibifiable words to be excluded altogether
         */

        int skipper = Integer.parseInt(getMadlibifiableSkipper());

        /* Instantiation automatically blanks the madlibFile (annotates the source file, replaces the madlibifiable words not skipped with appropriate [part of speech blocks])
           Madlib_File instance is saved to call its methods if the user intends to fill the Madlib_File in
         */

        Madlib madlib = new Madlib(sourceText, skipper);

        System.out.println();
        System.out.println(madlib.getBlankedText());

        //~ Queries if the user wants to fill in the madlibFile; exits if no
        if (!queryFillInMadlib()) return;

        System.out.println("You will now be prompted to fill in a word for each provided part of speech.");
        System.out.println();

        // Prompts the user with parts of speech for the purpose of obtaining a queue of words used to replace the speech blocks in the blanked Madlib_File
        Queue<String> userWords = getReplacementWords(madlib.getPosList());


        madlib.fillInMadlib(userWords);
        System.out.println(madlib.getFilledText());
        System.out.println();
        System.out.println("Congratulations! You did it! Whether you created a new spin on a short story or perverted your favorite bible chapter, thank you for having fun.");

        System.out.println("Goodbye.");
    }

    /** Helper method to ensure user is entering a proper numerical value for the madlibifiable skipper variable*/
    private String getMadlibifiableSkipper() {
        // if skipMadlibifiables is higher than the total madlibifiable word count, the text will be unchanged; 0 results in original text
        System.out.println("How many madlibifiable words would you like to ignore? The lower the number, the more words you'll need to replace");
        String skipMadlibifiables = SCANNER.nextLine();
        Matcher matcher = DIGITS.matcher(skipMadlibifiables);

        while (!matcher.matches()) {
            System.out.println("Please enter a number.");
            skipMadlibifiables = SCANNER.nextLine();
            matcher = DIGITS.matcher(skipMadlibifiables);
        }
        return skipMadlibifiables;
    }

    /** Helper method that determines if CLI should prompt the user for parts of speech or end the program.
     *  If user decides to enter parts of speech, the CLI will call getReplacementWords. Otherwise, it ends the program.
     * */
    private boolean queryFillInMadlib() {
        System.out.println("Would you like to fill in your new madlib? (yes/no) ");
        String response = SCANNER.nextLine();

        while (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")) {
            System.out.println("Please answer yes or no");
            response = SCANNER.nextLine();
        }

        if (response.equalsIgnoreCase("no")) {
            System.out.println("Thank you for madlibbing! Enjoy, I guess!");
            return false;
        }
        return true;
    }
    /** Prompt user to enter words one at a time based on the parts of speech of the words removed during madlib blanking
     * @param posList CLI iterates over the parts of speech in order so that the user can enter replacement words in order of when they appear in the text
     * @return Returns the list of words in a queue for cleaner replacement when filling in the madlib; no indexing required,
     * and constant time removal better handles errors involved in case the list empties before madlib filling is complete.
     * */
    private Queue<String> getReplacementWords(List<String> posList) throws NullPOSListException {

        // Convert null list to empty ArrayList
        if (posList == null) {
            throw new NullPOSListException("No POS list found. Blanked words will not be replaced, and program will exit");
        }
        Queue<String> wordList = new ArrayDeque<>();
        System.out.println("Please enter a word for each of the following parts of speech:");
        for (String pos : posList) {
            System.out.println(pos + ": ");
            if (pos != null) wordList.add(SCANNER.nextLine());
        }
        return wordList;
    }

}
