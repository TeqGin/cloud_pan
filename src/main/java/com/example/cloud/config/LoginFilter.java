package com.example.cloud.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;


@WebFilter(filterName = "sessionFilter",urlPatterns = {"/user/*", "/file/*"})
public class LoginFilter implements Filter {
    String [] excludeUris =new String []{"/user/login","/user/sign_up","/info/about",
            "/user/verify", "/user/forget", "/user/veri_sign_up","/user/verify_forget","/user/send_verify_code"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri  = request.getRequestURI();


        if (isNotNeedFilter(uri)){
            System.out.println("filter uri:" + uri +"  allow");
            filterChain.doFilter(servletRequest,servletResponse);

        }else {
            if (session != null && session.getAttribute("user") != null){
                System.out.println("filter uri:" + uri +"  allow");
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                System.out.println("filter uri:" + uri +"  reject");
                response.sendRedirect(request.getContextPath() + "/user/login");

            }
        }
    }

    public boolean isNotNeedFilter(String uri){
        for(String i: excludeUris){
            if (i.equals(uri)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
