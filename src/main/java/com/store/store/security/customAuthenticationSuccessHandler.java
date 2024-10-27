package com.store.store.security;
import com.store.store.entity.User;
import com.store.store.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class customAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private UserService userService;
    @Autowired
    public customAuthenticationSuccessHandler(UserService theUserService) {
        userService = theUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String userName = authentication.getName();
        User user =null;
for (User theUser :userService.findByUserId(userName)){
  user  =theUser;
}
        // now place in the session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        // forward to home page
        response.sendRedirect(request.getContextPath() + "/");
    }
}
