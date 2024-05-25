package uz.pdp.learningcenterapp.config.failures;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.pdp.learningcenterapp.entity.enums.RoleType;

import java.io.IOException;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(RoleType.ROLE_ADMIN.name())) {
                response.sendRedirect("/admin");
                return;
            } else if (authority.getAuthority().equals(RoleType.ROLE_STUDENT.name())) {
                response.sendRedirect("/student");
                return;
            }
            response.sendRedirect("/");
        }

        response.sendRedirect("/");
    }
}

