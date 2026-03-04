package adam_barnett.madlibs.madlib_machine.madlib.mvc;

import adam_barnett.madlibs.madlib_machine.madlib.DTOs.BlankMadlibResponse;
import adam_barnett.madlibs.madlib_machine.madlib.DTOs.FilledMadlibResponse;
import adam_barnett.madlibs.madlib_machine.madlib.DTOs.SavedMadlibResponse;
import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibBlanker;
import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibFiller;
import adam_barnett.madlibs.madlib_machine.users.User;
import adam_barnett.madlibs.madlib_machine.tagger.TextAnnotater;
import adam_barnett.madlibs.madlib_machine.utility.exceptions.InvalidPartOfSpeechException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * 1. User sends original text and skipper
 * 2. Server returns blanked text and pos list
 * 3. User sends blanked text and word list
 * 4. Server sends back finished text
 */

@Service
@RequiredArgsConstructor
public class MadlibService {

    private final MadlibBlanker madlibBlanker;
    private final MadlibFiller madlibFiller;
    private final MadlibRepository madlibRepository;

    /** Removes the skipper-th madlibifiable word with a part of speech in the posBlocks hashset. Madlibifiable word are tagged with parts of speech included in the posMap.
     Assigns the instance's posList returned by the helper method.
     @param skipper determines the frequency of madlib blanking (madlibification). Example: if skipper == 3, the method will clear every third madlibifiable word.
     @return DTO contains the blanked-out text and list of the removed parts of speech to prompt user to fill them out to complete the madlib*/
    public BlankMadlibResponse generateBlankMadlib(String sourceText, Integer skipper) throws InvalidPartOfSpeechException{
        // CoreNLP annotates each word in the text with their associated part of speech
        TextAnnotater annotatedText = new TextAnnotater(sourceText);
        return madlibBlanker.removeMadlibifiables(annotatedText, skipper);
    }

    public FilledMadlibResponse fillInMadlib(String blankedText, List<String> replacementWords){
        Queue<String> replacementWordsQueue = new ArrayDeque<>(replacementWords);
        String completedMadlib = madlibFiller.fillInMadlib(blankedText, replacementWordsQueue);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            madlibRepository.save(new Madlib(completedMadlib, user));
        } else {
            madlibRepository.save(new Madlib(completedMadlib));
        }

        return new FilledMadlibResponse(completedMadlib);
    }

    public List<SavedMadlibResponse> getAllMadlibs() {
        return madlibRepository.findAll().stream()
                .map(m -> new SavedMadlibResponse(m.getId(), m.getCompletedText(), m.getCreatedAt()))
                .toList();
    }

}
