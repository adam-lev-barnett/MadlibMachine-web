package adam_barnett.madlibs.madlib_machine;

import adam_barnett.madlibs.madlib_machine.userinterface.CLI_Memory;

/** Only needs to call the CLI to access full logic of program */
public class Main {

    public static void main(String[] args) throws Exception {

        CLI_Memory.getInstance().initiateMadlibCreation();

    }
}
