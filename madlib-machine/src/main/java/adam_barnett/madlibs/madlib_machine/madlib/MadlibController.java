package adam_barnett.madlibs.madlib_machine.madlib;

import adam_barnett.madlibs.madlib_machine.madlibgeneration.Madlib;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.TextNotProcessedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("madlibs")
public class MadlibController {

    @GetMapping("/madlibify")
    public Madlib madlibifyText(String originalText, int skipper) throws TextNotProcessedException, InvalidPartOfSpeechException, IOException {
        return new Madlib(originalText, skipper);
    }

}
