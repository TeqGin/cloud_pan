package com.example.cloud.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author TeqGin
 * 拦截前缀为/user和/file的请求
 * */
@WebFilter(filterName = "sessionFilter",urlPatterns = {"/user/*", "/file/*"})
public class LoginFilter implements Filter {
    //不进行拦截的请求
    String [] excludeUris =new String []{"/user/login","/user/sign_up","/info/about",
            "/user/verify", "/user/forget", "/user/veri_sign_up","/user/verify_forget",
            "/user/send_verify_code"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        //获取请求路径
        String uri  = request.getRequestURI();

        if (isNotNeedFilter(uri)){
            System.out.println("filter uri:" + uri +"  allow");
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            //判断是否登陆，若已登陆则放行
            if (session != null && session.getAttribute("user") != null){
                System.out.println("filter uri:" + uri +"  allow");
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                //未登录，返回登陆页面
                System.out.println("filter uri:" + uri +"  reject");
                response.sendRedirect(request.getContextPath() + "/user/login");

            }
        }
    }

    /**
     *
     * @param uri 验证的路径
     * @return 如果该路径不必被拦截则返回true，反之返回false
     */
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
