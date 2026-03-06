package adam_barnett.madlibs.madlib_machine.madlib.mvc;

import adam_barnett.madlibs.madlib_machine.madlib.DTOs.FilledMadlibResponse;
import adam_barnett.madlibs.madlib_machine.madlib.DTOs.MadlibRepository;
import adam_barnett.madlibs.madlib_machine.madlib.DTOs.SavedMadlibResponse;
import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibBlanker;
import adam_barnett.madlibs.madlib_machine.madlibgeneration.MadlibFiller;
import adam_barnett.madlibs.madlib_machine.users.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MadlibServiceTest {

    @Mock MadlibBlanker madlibBlanker;
    @Mock MadlibFiller madlibFiller;
    @Mock
    MadlibRepository madlibRepository;
    @InjectMocks MadlibService madlibService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void setAuthenticatedUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );
    }

    @Test
    void fillInMadlib_whenAuthenticated_savesMadlibWithUser() {
        User user = new User("g123", "test@example.com", "Test User", null);
        setAuthenticatedUser(user);
        when(madlibFiller.fillInMadlib(anyString(), any())).thenReturn("completed madlib");

        madlibService.fillInMadlib("blanked text", List.of("word1"));

        ArgumentCaptor<Madlib> captor = ArgumentCaptor.forClass(Madlib.class);
        verify(madlibRepository).save(captor.capture());
        assertEquals("completed madlib", captor.getValue().getCompletedText());
        assertEquals(user, captor.getValue().getUser());
    }

    @Test
    void fillInMadlib_whenAnonymous_doesNotSave() {
        when(madlibFiller.fillInMadlib(anyString(), any())).thenReturn("completed madlib");

        madlibService.fillInMadlib("blanked text", List.of("word1"));

        verify(madlibRepository, never()).save(any());
    }

    @Test
    void fillInMadlib_returnsFilledText() {
        when(madlibFiller.fillInMadlib(anyString(), any())).thenReturn("completed madlib");

        FilledMadlibResponse response = madlibService.fillInMadlib("blanked text", List.of("word1"));

        assertEquals("completed madlib", response.completeMadlib());
    }

    @Test
    void getAllMadlibs_returnsMappedDtos() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Madlib madlib = mock(Madlib.class);
        when(madlib.getId()).thenReturn(id);
        when(madlib.getCompletedText()).thenReturn("some madlib");
        when(madlib.getCreatedAt()).thenReturn(now);
        when(madlibRepository.findAll(any(Sort.class))).thenReturn(List.of(madlib));

        List<SavedMadlibResponse> result = madlibService.getAllMadlibs();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("some madlib", result.get(0).completedText());
        assertEquals(now, result.get(0).createdAt());
    }

    @Test
    void getMyMadlibs_returnsOnlyUserMadlibs() {
        User user = new User("g123", "test@example.com", "Test User", null);
        setAuthenticatedUser(user);
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Madlib madlib = mock(Madlib.class);
        when(madlib.getId()).thenReturn(id);
        when(madlib.getCompletedText()).thenReturn("my madlib");
        when(madlib.getCreatedAt()).thenReturn(now);
        when(madlibRepository.findByUser(user)).thenReturn(List.of(madlib));

        List<SavedMadlibResponse> result = madlibService.getMyMadlibs();

        assertEquals(1, result.size());
        assertEquals("my madlib", result.get(0).completedText());
        verify(madlibRepository).findByUser(user);
    }
}
