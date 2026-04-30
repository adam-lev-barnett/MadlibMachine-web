package adam_barnett.madlibs.madlib_machine.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://*.vercel.app", "http://localhost:*", "https://madlib-machine.app", "https://www.madlib-machine.app")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }
}