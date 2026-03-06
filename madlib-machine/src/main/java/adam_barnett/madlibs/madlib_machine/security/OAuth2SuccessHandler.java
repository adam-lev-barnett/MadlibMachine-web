package adam_barnett.madlibs.madlib_machine.security;

import adam_barnett.madlibs.madlib_machine.users.User;
import adam_barnett.madlibs.madlib_machine.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/** Uses Google OAuth to verify or create user, save user in the database if not found, and return token */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String googleId  = oauthUser.getAttribute("sub");
            String email     = oauthUser.getAttribute("email");
            String name      = oauthUser.getAttribute("name");

            log.info("OAuth2 login: googleId={}, email={}", googleId, email);

            User user = userRepository.findByGoogleId(googleId)
                    .orElseGet(() -> userRepository.save(new User(googleId, email, name)));

            String token = jwtService.generateToken(user);
            response.sendRedirect(frontendUrl + "/auth/callback?token=" + token);
        } catch (Exception e) {
            log.error("OAuth2 login failed", e);
            response.sendRedirect(frontendUrl + "/auth/callback?error=login_failed");
        }
    }
}
