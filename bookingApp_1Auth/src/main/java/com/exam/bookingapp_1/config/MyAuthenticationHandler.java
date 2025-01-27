package com.exam.bookingapp_1.config;

import com.exam.bookingapp_1.service.BusersDetailsService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
   // private final BusersDetailsService busersDetailsService;


    //private final List<String> PATHS_TO_IGNORE_SETTING_SAMESITE = Arrays.asList("resources", <add other paths you want to exclude>);
    private final String SESSION_COOKIE_NAME = "JSESSIONID";
    private final String SESSION_PATH_ATTRIBUTE = ";Path=";
    private final String ROOT_CONTEXT = "/";
    private final String SAME_SITE_ATTRIBUTE_VALUES = ";HttpOnly;Secure;SameSite=None";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {


//        handle(request,response,authentication);
//
//        //set contextHolder to access endpoint for any authenticated user
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
//                null, userDetails.getAuthorities());
//
//
//        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//
//        //to access every endpoint authenticated
//        //clearAuthenticationAttributes(request);
//
////
////        Cookie[] cookies =  request.getCookies();
////        if (cookies != null && cookies.length > 0) {
////            List<Cookie> cookieList = Arrays.asList(cookies);
////            Cookie sessionCookie = cookieList.stream().filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName())).findFirst().orElse(null);
////            if (sessionCookie != null) {
////                String contextPath = request.getServletContext() != null && StringUtils.isNotBlank(request.getServletContext().getContextPath()) ? request.getServletContext().getContextPath() : ROOT_CONTEXT;
////                response.setHeader(HttpHeaders.SET_COOKIE, sessionCookie.getName() + "=" + sessionCookie.getValue() + SESSION_PATH_ATTRIBUTE + contextPath + SAME_SITE_ATTRIBUTE_VALUES);
////            }
////        }
////
//
//
//        //response.setHeader("Set-Cookie", "JSESSIONID="+session.getId());
//        chain.doFilter(request, response);
       //AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        handle(request,response,authentication);

        //set contextHolder to access endpoint for any authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        //authToken.setDetails(userDetails);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
//        //to access every endpoint authenticated
//        //clearAuthenticationAttributes(request);

    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            log.info(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        //roleTargetUrlMap.put("USER", "/json_calendar?username=" + authentication.getName());
        //roleTargetUrlMap.put("USER","/depyb?username=" + authentication.getName()) ;
        roleTargetUrlMap.put("USER","/depya?username=" + authentication.getName()) ;

        //roleTargetUrlMap.put("USER","/auth");
        roleTargetUrlMap.put("ADMIN", "/admin");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }


}
