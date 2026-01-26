package adam_barnett.madlibs.madlib_machine.madlib;

import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** Primary means of frontend communication for source text submission, backend blanking, and receiving user-submitted replacement words to return the completed madlib*/
@RestController
@RequiredArgsConstructor
@RequestMapping("madlibs")
@CrossOrigin(origins = "https://madlib-frontend-deploy.vercel.app")
public class MadlibController {

    private final MadlibService madlibService;

    /** Ingests the source text and number of madlibifiable words to skip to return a list of parts of speech that prompt the user to fill out replacement words*/
    @PostMapping("/madlibify")
    public BlankMadlibResponse madlibifyText(@RequestBody BlankMadlibRequest sourceTextAndSkipper) throws InvalidPartOfSpeechException {
        return madlibService.generateBlankMadlib(sourceTextAndSkipper.sourceText(), sourceTextAndSkipper.skipper());
    }

    /** Receives the replacement words to return the filled madlib*/
    @PostMapping("/fillMadlib")
    public FilledMadlibResponse fillInMadlib(@RequestBody FillMadlibRequest blankTextAndWordList) {
        return madlibService.fillInMadlib(blankTextAndWordList.blankedText(), blankTextAndWordList.replacementWords());
    }

}
