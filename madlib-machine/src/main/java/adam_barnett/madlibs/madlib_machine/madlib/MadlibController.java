package adam_barnett.madlibs.madlib_machine.madlib;

import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("madlibs")
public class MadlibController {

    private final MadlibService madlibService;

    @PostMapping("/madlibify")
    public BlankMadlibResponse madlibifyText(@RequestBody BlankMadlibRequest sourceTextAndSkipper) throws InvalidPartOfSpeechException {
        return madlibService.generateBlankMadlib(sourceTextAndSkipper.sourceText(), sourceTextAndSkipper.skipper());
    }

    @PostMapping("/fillMadlib")
    public FilledMadlibResponse fillInMadlib(@RequestBody FillMadlibRequest blankTextAndWordList) {
        return madlibService.fillInMadlib(blankTextAndWordList.blankedText(), blankTextAndWordList.replacementWords());
    }

}
