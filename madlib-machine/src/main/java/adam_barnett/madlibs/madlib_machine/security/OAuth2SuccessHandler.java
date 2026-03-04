package adam_barnett.madlibs.madlib_machine.security;

import adam_barnett.madlibs.madlib_machine.persistence.User;
import adam_barnett.madlibs.madlib_machine.persistence.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String googleId  = oauthUser.getAttribute("sub");
        String email     = oauthUser.getAttribute("email");
        String name      = oauthUser.getAttribute("name");
        String picture   = oauthUser.getAttribute("picture");

        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> userRepository.save(new User(googleId, email, name, picture)));

        String token = jwtService.generateToken(user);
        response.sendRedirect(frontendUrl + "/auth/callback?token=" + token);
    }
}
