package adam_barnett.madlibs.madlib_machine.madlib;

import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibBlanker;
import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MadlibService {

    private MadlibBlanker madlibBlanker;
    private MadlibFiller madlibFiller;

    


}
